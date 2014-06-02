package com.mcintyret.jvm.core.clazz;

import java.util.Set;

import com.mcintyret.jvm.core.Utils;
import com.mcintyret.jvm.core.domain.Type;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.parse.Modifier;
import com.mcintyret.jvm.parse.attribute.Attributes;

public class Field extends Member {

    private final String name;

    private final Type type;

    private final int offset;

    public Field(Set<Modifier> modifiers, Attributes attributes, String name, Type type, int offset) {
        super(modifiers, attributes);
        this.name = name;
        this.type = type;
        this.offset = offset;
    }

    public Type getType() {
        return type;
    }

    public int getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }

    public void set(Oop thisOop, int i) {
        getValues(thisOop)[offset] = i;
    }

    private int[] getValues(Oop thisOop) {
        return thisOop == null ? getClassObject().getStaticFieldValues() : thisOop.getFields();
    }

    public void set(Oop thisOop, Oop oop) {
        set(thisOop, oop.getAddress());
    }

    public void set(Oop thisOop, long l) {
        int[] fields = getValues(thisOop);
        fields[offset] = (int) (l >> 32);
        fields[offset + 1] = (int) l & 0x0000FFFF;
    }

    public void set(Oop thisOop, int l, int r) {
        int[] fields = getValues(thisOop);
        fields[offset] = l;
        fields[offset + 1] = r;
    }

    public void get(Oop thisOop, ValueReceiver valueReceiver) {
        int[] fields = getValues(thisOop);
        if (type.getSimpleType().isDoubleWidth()) {
            valueReceiver.receiveLong(Utils.toLong(fields[offset], fields[offset + 1]));
        } else {
            valueReceiver.receiveInt(fields[offset]);
        }
    }

}
