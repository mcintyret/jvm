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

    private final File file;

    public ZipClassPath(File file) {
        this.file = file;
    }

    public ZipClassPath(String file) {
        this(new File(file));
    }

    @Override
    public Iterable<InputStream> getClassFileStreams() throws IOException {
        return new ZipFileIterable(new ZipFile(file));
    }

    private static class ZipFileIterable implements Iterable<InputStream> {
        private final ZipFile zipFile;

        private ZipFileIterable(ZipFile zipFile) {
            this.zipFile = zipFile;
        }

        @Override
        public Iterator<InputStream> iterator() {
            return new Iterator<InputStream>() {

                private final Enumeration<? extends ZipEntry> it = zipFile.entries();

                @Override
                public boolean hasNext() {
                    return it.hasMoreElements();
                }

                @Override
                public InputStream next() {
                    try {
                        return zipFile.getInputStream(it.nextElement());
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            };
        }
    }

}
