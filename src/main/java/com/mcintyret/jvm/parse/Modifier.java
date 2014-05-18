package com.mcintyret.jvm.parse;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum Modifier {
    PUBLIC(0x00000001),
    PRIVATE(0x00000002),
    PROTECTED(0x00000004),
    STATIC(0x00000008),
    FINAL(0x00000010),
    SYNCHRONIZED(0x00000020),
    VOLATILE(0x00000040),
    TRANSIENT(0x00000080),
    NATIVE(0x00000100),
    INTERFACE(0x00000200),
    ABSTRACT(0x00000400),
    STRICT(0x00000800),
    BRIDGE(0x00000040),
    VARARGS(0x00000080),
    SYNTHETIC(0x00001000),
    ANNOTATION(0x00002000),
    ENUM(0x00004000),
    MANDATED(0x00008000);

    private final int val;

    private Modifier(int val) {
        this.val = val;
    }

    public static Set<Modifier> translate(int modifiers) {
        if (modifiers == 0) {
            return Collections.emptySet();
        }
        Set<Modifier> mods = EnumSet.noneOf(Modifier.class);
        for (Modifier mod : values()) {
            if ((modifiers & mod.val) != 0) {
                mods.add(mod);
            }
        }
        return mods;
    }

    public int getVal() {
        return val;
    }
}
