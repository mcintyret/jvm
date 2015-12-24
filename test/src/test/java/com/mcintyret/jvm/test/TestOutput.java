package com.mcintyret.jvm.test;

public class TestOutput {

    private final String stdOut;

    private final String stdErr;

    public TestOutput(String stdOut, String stdErr) {
        this.stdOut = stdOut;
        this.stdErr = stdErr;
    }

    public String getStdErr() {
        return stdErr;
    }

    public String getStdOut() {
        return stdOut;
    }
}
