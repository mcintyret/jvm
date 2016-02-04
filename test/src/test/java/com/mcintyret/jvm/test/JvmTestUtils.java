package com.mcintyret.jvm.test;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

class JvmTestUtils {

    private static final String[] EMPTY_ARGS = new String[0];

    private static final String JAVA_JVM_HOME = "java.jvm.home";

    private static final String PROJECT_BASE_DIR = getProjectBaseDir();

    private static final String TEST_MODULE_TARGET_DIR = PROJECT_BASE_DIR + "test/target/";

    private static final String JAR_LOCATION = PROJECT_BASE_DIR + "target/javaJvm.jar";

    private static final String MAIN_CLASS = "com.mcintyret.jvm.Jvm";

    private static String getProjectBaseDir() {
        String projectBaseDir = System.getProperty("user.dir");
        if (projectBaseDir.endsWith("test")) {
            projectBaseDir = projectBaseDir.substring(0, projectBaseDir.length() - 4);
        }
        if (!projectBaseDir.endsWith("/")) {
            projectBaseDir += "/";
        }
        return projectBaseDir;
    }


    static TestOutput runTest(Class<?> testClass) {
        String mainClass = testClass.getName() + "$Test";
        String classPath = TEST_MODULE_TARGET_DIR + "test-classes/";
        return runTestClass(mainClass, classPath);
    }


    static TestOutput runTestClass(String mainClass) {
        return runTestClass(mainClass, null, EMPTY_ARGS);
    }

    static TestOutput runTestClass(String mainClass, String classPath) {
        return runTestClass(mainClass, classPath, EMPTY_ARGS);
    }

    static TestOutput runTestClass(String mainClass, String classPath, String[] args) {

        boolean debug = false;

        List<String> command = new ArrayList<>();
        command.add("java");

        if (debug) {
            command.add("-Xdebug");
            command.add("-Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=y");
        }

        command.add("-cp");
        command.add(PROJECT_BASE_DIR + "test/src/test/resources:" + JAR_LOCATION);
        command.add("-Xmx1024m");
        command.add("-D" + JAVA_JVM_HOME + "=" + System.getProperty(JAVA_JVM_HOME));
        if (classPath != null) {
            command.add("-Djvm.classpath=" + classPath);
        }
        command.add(MAIN_CLASS);
        command.add(mainClass);
        if (args != null && args.length > 0) {
            Collections.addAll(command, args);
        }

        Process jvm;
        try {
            jvm = new ProcessBuilder()
                .command(command)
                .start();
        } catch (IOException e) {
            throw new AssertionError("Error starting JVM", e);
        }

        long timeout = debug ? Long.MAX_VALUE : 10;
        try {
            if (!jvm.waitFor(timeout, TimeUnit.SECONDS)) {
                throw new AssertionError("Timed out waiting to execute " + mainClass);
            }
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }

        String output;
        String errorOutput;
        try {
            output = IOUtils.toString(jvm.getInputStream()).trim();
            errorOutput = IOUtils.toString(jvm.getErrorStream()).trim();
        } catch (IOException e) {
            throw new AssertionError("Error reading JVM output", e);
        }


        int exitValue = jvm.exitValue();
        if (exitValue != 0) {
            throw new AssertionError("JVM exited with error value: " + exitValue + ", output: " + output + ", error: " + errorOutput);
        }

        return new TestOutput(output, errorOutput);
    }

}
