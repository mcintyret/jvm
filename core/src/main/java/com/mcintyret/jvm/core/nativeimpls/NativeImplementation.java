package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.type.MethodSignature;
import com.mcintyret.jvm.core.exec.OperationContext;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */

public interface NativeImplementation {

    NativeReturn execute(int[] args, OperationContext ctx);

    String getClassName();

    MethodSignature getMethodSignature();

}
