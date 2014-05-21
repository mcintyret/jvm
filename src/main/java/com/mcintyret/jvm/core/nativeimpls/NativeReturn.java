package com.mcintyret.jvm.core.nativeimpls;

import com.mcintyret.jvm.core.WordStack;

/**
 * User: tommcintyre
 * Date: 5/20/14
 */
public interface NativeReturn {

    static final NativeReturn FOR_VOID = stack -> {};

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
        return FOR_VOID;
    }

    void applyToStack(WordStack stack);

}
