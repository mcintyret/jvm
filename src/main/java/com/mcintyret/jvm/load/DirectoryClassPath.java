package com.mcintyret.jvm.load;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
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
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Should be a Directory!");
        }
        this.path = path;
        this.recursive = recursive;
    }


    @Override
    public Iterable<InputStream> getClassFileStreams() throws IOException {
        int depth = recursive ? Integer.MAX_VALUE : 1;
        final List<InputStream> list = new ArrayList<>();
        Files.walkFileTree(path, Collections.emptySet(), depth, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                list.add(Files.newInputStream(file));
                return FileVisitResult.CONTINUE;
            }

        });
        return list;
    }
}
