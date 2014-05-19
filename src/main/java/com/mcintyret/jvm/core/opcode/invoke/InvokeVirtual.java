package com.mcintyret.jvm.core.opcode.invoke;


//TODO: shouldn't extend!
class InvokeVirtual extends InvokeSpecial {

    @Override
    public byte getByte() {
        return (byte) 0xB6;
    }
}
