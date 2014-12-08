package com.mcintyret.jvm.core.opcode;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.reflections.Reflections;

public class OpCodes {

    private static final Map<Byte, OpCode> OP_CODE_MAP = loadOpCodes();

    private static Map<Byte, OpCode> loadOpCodes() {
        Reflections reflections = new Reflections("com.mcintyret.jvm.core.opcode");

        Map<Byte, OpCode> map = new HashMap<>();
        for (Class<? extends OpCode> clazz : reflections.getSubTypesOf(OpCode.class)) {
            if (!Modifier.isAbstract(clazz.getModifiers())) {
                try {
                    OpCode opCode = newInstance(clazz);
                    if (map.put(opCode.getByte(), opCode) != null) {
                        throw new AssertionError("Duplicate OpCode: " + opCode.getByte() + " for " + clazz.getSimpleName());
                    }
                } catch (Throwable t) {
                    throw new IllegalStateException("Unable to create OpCode " + clazz, t);
                }
            }
        }
        return Collections.unmodifiableMap(map);
    }

    private static OpCode newInstance(Class<? extends OpCode> clazz) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = clazz.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return (OpCode) constructor.newInstance();
    }

    public static OpCode getOpcode(byte b) {
        OpCode opCode = OP_CODE_MAP.get(b);
        if (opCode == null) {
            throw new IllegalArgumentException("No opcode corresponds to byte '" + String.format("%02X", b) + "'");
        }
        return opCode;
    }

    public static void main(String[] args) {
        Map<Byte, OpCode> orderedOpCodes = new TreeMap<>(OP_CODE_MAP);

        System.out.println(orderedOpCodes.size() + " opcodes implemented:");
        for (OpCode opCode : orderedOpCodes.values()) {
            System.out.println(opCode.getByte() + ": " + opCode.toString());
        }
    }

}
