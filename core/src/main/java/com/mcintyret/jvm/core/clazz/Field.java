package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.exec.Variables;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.Type;
import com.mcintyret.jvm.core.util.Utils;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;

import java.util.Set;

public class Field extends Member {

    private final String name;

    private final Type type;

    public Field(Set<Modifier> modifiers, Attributes attributes, String name, Type type, int offset) {
        super(modifiers, attributes, offset);
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void set(Oop thisOop, int i) {
        getValues(thisOop).put(getOffset(), type.asSimpleType(), i);
    }

    private Variables getValues(Oop thisOop) {
        return thisOop == null ? getClassObject().getStaticFieldValues() : thisOop.getFields();
    }

    public void set(Oop thisOop, Oop oop) {
        set(thisOop, oop.getAddress());
    }

    public void set(Oop thisOop, long l) {
        getValues(thisOop).putWide(getOffset(), getType().asSimpleType(), l);
    }

    public void set(Oop thisOop, int l, int r) {
        set(thisOop, Utils.toLong(l, r));
    }

    // TODO: where is this used?
    public void get(Oop thisOop, ValueReceiver valueReceiver) {
        Variables fields = getValues(thisOop);
        if (type.isDoubleWidth()) {
            valueReceiver.receiveDoubleWidth(fields.getLong(getOffset()), type.asSimpleType());
        } else {
            valueReceiver.receiveSingleWidth(fields.getRawValue(getOffset()), type.asSimpleType());
        }
    }

    public int getInt(Oop thisOop) {
        return getValues(thisOop).getRawValue(getOffset());
    }

    public Oop getOop(Oop thisOop) {
        return Heap.getOop(getInt(thisOop));
    }

    @Override
    public String toString() {
        return type + "[" + name + "]";
    }

}
