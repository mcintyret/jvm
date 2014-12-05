package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.exec.Variable;
import com.mcintyret.jvm.core.type.MethodSignature;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */

public interface NativeImplementation {

    NativeReturn execute(Variable[] args, OperationContext ctx);

    String getClassName();

    MethodSignature getMethodSignature();

}
