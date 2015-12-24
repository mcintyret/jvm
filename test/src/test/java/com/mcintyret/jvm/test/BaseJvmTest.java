package com.mcintyret.jvm.test;

import org.testng.annotations.Test;

public abstract class BaseJvmTest {

    @Test
    public final void runTest() {
        verifyResults(JvmTestUtils.runTest(getClass()));
    }

    protected abstract void verifyResults(TestOutput output);


}
