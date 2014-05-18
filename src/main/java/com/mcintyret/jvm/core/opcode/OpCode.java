package com.mcintyret.jvm.core.opcode;

public abstract class OpCode {

    public abstract void execute(OperationContext ctx);

    public abstract byte getByte();

    @Override
    public final String toString() {
        return getClass().getSimpleName().toLowerCase();
    }

}
