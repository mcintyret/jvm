package com.mcintyret.jvm.load;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public class ZipClassFileResource implements ClassFileResource {

    private final ZipFile file;

    private final ZipEntry entry;

    public ZipClassFileResource(ZipFile file, ZipEntry entry) {
        this.file = file;
        this.entry = entry;
    }

    @Override
    public String getName() {
        return entry.getName();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(file.getInputStream(entry));
    }
}
