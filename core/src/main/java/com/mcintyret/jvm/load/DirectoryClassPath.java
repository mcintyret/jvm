package com.mcintyret.jvm.load;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DirectoryClassPath implements ClassPath {

    private final Path path;

    private final boolean recursive;

    public DirectoryClassPath(String path) {
        this(path, true);
    }

    public DirectoryClassPath(File dir) {
        this(dir, true);
    }

    public DirectoryClassPath(Path path) {
        this(path, true);
    }

    public DirectoryClassPath(String path, boolean recursive) {
        this(Paths.get(path), recursive);
    }

    public DirectoryClassPath(File dir, boolean recursive) {
        this(dir.toPath(), recursive);
    }

    public DirectoryClassPath(Path path, boolean recursive) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("No such file: " + path);
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Should be a Directory: " + path);
        }
        this.path = path;
        this.recursive = recursive;
    }


    @Override
    public Iterator<ClassFileResource> iterator() {
        int depth = recursive ? Integer.MAX_VALUE : 1;
        final List<ClassFileResource> list = new ArrayList<>();
        try {
            Files.walkFileTree(path, Collections.emptySet(), depth, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    list.add(new PathClassFileResource(file));
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classFileFilteringIterator(list.iterator());
    }

    @Override
    public ClassFileResource get(String name) {
        Path classFile = path.resolve(name + ".class");
        return Files.exists(classFile) ? new PathClassFileResource(classFile) : null;
    }
}
