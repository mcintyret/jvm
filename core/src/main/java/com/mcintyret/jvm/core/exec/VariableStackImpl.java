package com.mcintyret.jvm.core.exec;

import com.mcintyret.jvm.core.Heap;
import com.mcintyret.jvm.core.clazz.ValueReceiver;
import com.mcintyret.jvm.core.oop.Oop;
import com.mcintyret.jvm.core.type.SimpleType;
import com.mcintyret.jvm.core.util.Utils;

import java.util.NoSuchElementException;

public class VariableStackImpl implements ValueReceiver, VariableStack {

    public static final VariableStack EMPTY_STACK = new VariableStackImpl(Variables.EMPTY_VARIABLES);

    private Variables stack;

    private int head = 0;

    public VariableStackImpl(int size) {
        this(new Variables(size));
    }

    private VariableStackImpl(Variables variables) {
        this.stack = variables;
    }

    public void push(int val, SimpleType type) {
        if (head == stack.length()) {
            resize();
        }
        int pos = head++;
        stack.put(pos, type, val);
    }

    public int popRaw() {
        return popInternal(null, false);
    }

    public int popInternal(SimpleType type, boolean checkType) {
        if (head == 0) {
            throw new NoSuchElementException();
        }
        int pos = --head;
        int ret = checkType ? stack.getCheckedValue(pos, type) : stack.getRawValue(pos);
        stack.clear(pos);
        return ret;
    }

    @Override
    public int popSingleWidth(SimpleType type) {
        return popInternal(type, true);
    }

    @Override
    public Variable pop() {
        if (head == 0) {
            throw new NoSuchElementException();
        }
        int pos = --head;
        Variable ret = Variable.forType(stack.getType(pos), stack.getRawValue(pos));
        stack.clear(pos);
        return ret;
    }

    @Override
    public WideVariable popWide() {
        if (head <= 1) {
            throw new NoSuchElementException();
        }
        int pos = head - 2;
        WideVariable wv = new WideVariable(stack.getType(pos), Utils.toLong(stack.getRawValue(pos), stack.getRawValue(pos + 1)));
        stack.clear(head - 1);
        stack.clear(head - 2);

        head = pos;

        return wv;
    }

    @Override
    public int popInt() {
        return popSingleWidth(SimpleType.INT);
    }

    @Override
    public float popFloat() {
        return Float.intBitsToFloat(popSingleWidth(SimpleType.FLOAT));
    }

    @Override
    public long popLong() {
        return popDoubleWidth(SimpleType.LONG);
    }

    @Override
    public double popDouble() {
        return Double.longBitsToDouble(popDoubleWidth(SimpleType.DOUBLE));
    }

    @Override
    public long popDoubleWidth(SimpleType type) {
        if (head <= 1) {
            throw new NoSuchElementException();
        }
        long val = Utils.toLong(stack.getCheckedValue(head - 2, type), stack.getCheckedValue(head - 1, type));
        head -= 2;
        return val;
    }

    @Override
    public <O extends Oop> O popOop() {
        return (O) Heap.getOop(popSingleWidth(SimpleType.REF));
    }

    @Override
    public void pushSingleWidth(int val, SimpleType type) {
        if (head >= stack.length()) {
            resize();
        }
        stack.put(head++, type, val);
    }

    @Override
    public void pushInt(int val) {
        pushSingleWidth(val, SimpleType.INT);
    }

    @Override
    public void pushLong(int l, int r) {
        pushDoubleWidth(l, r, SimpleType.LONG);
    }

    @Override
    public void pushLong(long val) {
        pushDoubleWidth(val, SimpleType.LONG);
    }

    @Override
    public void pushFloat(float f) {
        pushSingleWidth(Float.floatToIntBits(f), SimpleType.FLOAT);
    }

    @Override
    public void pushDouble(double d) {
        pushDoubleWidth(Double.doubleToLongBits(d), SimpleType.DOUBLE);
    }

    @Override
    public void pushDoubleWidth(int l, int r, SimpleType type) {
        if (head >= stack.length() - 1) {
            resize();
        }
        stack.put(head++, type, l);
        stack.put(head++, type, r);
    }

    @Override
    public void pushDoubleWidth(long val, SimpleType type) {
        int l = (int) (val >> 32);
        int r = (int) val;
        pushDoubleWidth(l, r, type);
    }

    @Override
    public void pushByte(byte b) {
        pushSingleWidth(b, SimpleType.BYTE);
    }

    @Override
    public void pushShort(short s) {
        pushSingleWidth(s, SimpleType.SHORT);
    }

    @Override
    public void pushOop(Oop oop) {
        pushSingleWidth(oop.getAddress(), SimpleType.REF);
    }

    @Override
    public void pushNull() {
        pushSingleWidth(Heap.NULL_POINTER, SimpleType.REF);
    }

    @Override
    public void push(Variable v) {
        pushSingleWidth(v.getValue(), v.getType());
    }

    @Override
    public void pushWide(WideVariable v) {
        pushDoubleWidth(v.getLeft(), v.getRight(), v.getType());
    }

    @Override
    public void clear() {
        makeNewStack(stack.length(), false);
    }

    @Override
    public SimpleType peekType() {
        return stack.getType(head - 1);
    }

    @Override
    public Variables asVariables() {
        return stack;
    }

    private void resize() {
        makeNewStack(stack.length() * 2, true);
    }

    private void makeNewStack(int newSize, boolean copy) {
        stack = copy ? stack.copy(newSize) : new Variables(newSize);
    }
}
