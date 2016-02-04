package com.mcintyret.jvm.load;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryClassPath implements ClassPath {

    private final Path path;

    public DirectoryClassPath(File dir) {
        this(dir.toPath());
    }

    public DirectoryClassPath(String path) {
        this(Paths.get(path));
    }

    public DirectoryClassPath(Path path) {
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("No such file: " + path);
        }
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Should be a Directory: " + path);
        }
        this.path = path;
    }

    @Override
    public ClassFileResource get(String name) {
        Path classFile = path.resolve(name + ".class");
        return Files.exists(classFile) ? new PathClassFileResource(classFile) : null;
    }
}
