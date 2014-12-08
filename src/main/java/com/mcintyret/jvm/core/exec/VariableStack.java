package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;

public interface VariableStack extends ValueReceiver {

    int popRaw();

    int popSingleWidth(SimpleType type);

    Variable pop();

    WideVariable popWide();

    int popInt();

    float popFloat();

    long popLong();

    double popDouble();

    long popDoubleWidth(SimpleType type);

    <O extends Oop> O popOop();

    void pushSingleWidth(int val, SimpleType type);

    void pushInt(int val);

    void pushLong(int l, int r);

    void pushLong(long val);

    void pushFloat(float f);

    void pushDouble(double v);

    void pushDoubleWidth(int l, int r, SimpleType type);

    void pushDoubleWidth(long val, SimpleType type);

    void pushByte(byte b);

    void pushShort(short s);

    void pushOop(Oop oop);

    void pushNull();

    void push(Variable v);

    void pushWide(WideVariable v);

    void clear(); // Required when exceptions are thrown

    @Override
    default void receiveSingleWidth(int i, SimpleType type) {
        pushSingleWidth(i, type);
    }

    @Override
    default void receiveDoubleWidth(long l, SimpleType type) {
        pushDoubleWidth(l, type);
    }



}
