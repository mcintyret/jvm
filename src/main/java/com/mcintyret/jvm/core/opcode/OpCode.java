package com.mcintyret.jvm.core.opcode;

import com.mcintyret.jvm.core.exec.OperationContext;

public abstract class OpCode {

    public abstract void execute(OperationContext ctx);

    public abstract byte getByte();

    @Override
    public final String toString() {
        return getClass().getSimpleName().toLowerCase();
    }

}
