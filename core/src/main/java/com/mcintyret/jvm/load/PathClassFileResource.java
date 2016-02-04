package com.mcintyret.jvm.load;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class PathClassFileResource implements ClassFileResource {

    private final Path path;

    public PathClassFileResource(Path path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return path.toString();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(Files.newInputStream(path));
    }
}
