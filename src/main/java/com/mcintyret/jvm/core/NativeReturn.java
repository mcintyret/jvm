package com.mcintyret.jvm.core;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public interface NativeReturn {

    public static NativeReturn forInt(int i) {
        return stack -> {
            stack.push(i);
        };
    }

    public static NativeReturn forLong(long l) {
        return stack -> {
            stack.push(l);
        };
    }

    public static NativeReturn forVoid() {
        return stack -> {};
    }

    void applyToStack(WordStack stack);

}
