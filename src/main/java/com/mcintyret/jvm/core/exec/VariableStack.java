package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;

public interface VariableStack extends ValueReceiver {

    int popRaw();

    int popChecked(SimpleType type);

    Variable pop();

    int popInt();

    float popFloat();

    long popLong();

    double popDouble();

    <O extends Oop> O popOop();

    void pushChecked(int val, SimpleType type);

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

    default void receiveInt(int i) {
        pushInt(i);
    }

    default void receiveLong(long l) {
        pushLong(l);
    }

    void clear(); // Required when exceptions are thrown


}
