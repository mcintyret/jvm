package com.mcintyret.jvm.load;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: tommcintyre
 * Date: 5/21/14
 */
public interface ClassFileResource {

    String getName();

    InputStream getInputStream() throws IOException;

}
