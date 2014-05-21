package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.domain.MethodSignature;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */

public interface NativeExecution {

    NativeReturn execute(int[] args);

    String getClassName();

    MethodSignature getMethodSignature();

}
