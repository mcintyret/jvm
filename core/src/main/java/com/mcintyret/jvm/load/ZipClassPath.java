package com.mcintyret.jvm.load;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public class ZipClassPath implements ClassPath {

    private final ZipFile file;

    public ZipClassPath(File file) {
        try {
            this.file = new ZipFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ZipClassPath(String file) {
        this(new File(file));
    }

    @Override
    public ClassFileResource get(String name) {
        ZipEntry entry = file.getEntry(name + ".class");
        return entry != null ? new ZipClassFileResource(file, entry) : null;
    }
}
