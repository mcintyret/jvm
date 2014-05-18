package com.mcintyret.jvm.load;

import java.io.IOException;
import java.io.InputStream;

public interface ClassPath {

    Iterable<InputStream> getClassFileStreams() throws IOException;

}
