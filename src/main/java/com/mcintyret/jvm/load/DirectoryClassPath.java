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

    public DirectoryClassPath(String path) {
        this(Paths.get(path));
    }

    public DirectoryClassPath(File dir) {
        this(dir.toPath());
    }

    public DirectoryClassPath(Path path) {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Should be a Directory!");
        }
        this.path = path;
    }


    @Override
    public Iterable<InputStream> getClassFileStreams() throws IOException {
        final List<InputStream> list = new ArrayList<>();
        Files.walkFileTree(path, Collections.emptySet(), 1, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                list.add(Files.newInputStream(file));
                return FileVisitResult.CONTINUE;
            }

        });
        return list;
    }
}
