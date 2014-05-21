package com.mcintyret.jvm.load;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

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
    public Iterator<ClassFileResource> iterator() {
        return classFileFilteringIterator(new Iterator<ClassFileResource>() {

            private final Enumeration<? extends ZipEntry> it = file.entries();

            @Override
            public boolean hasNext() {
                return it.hasMoreElements();
            }

            @Override
            public ClassFileResource next() {
                return new ZipClassFileResource(file, it.nextElement());
            }
        });
    }
}
