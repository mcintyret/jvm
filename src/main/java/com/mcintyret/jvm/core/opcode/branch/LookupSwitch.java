package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.util.ByteIterator;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.exec.OperationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * User: tommcintyre
 * Date: 6/25/14
 */
class LookupSwitch extends OpCode {

    @Override
    public void execute(OperationContext ctx) {
        ByteIterator bi = ctx.getByteIterator();
        int startPos = bi.getPos();

        // first get rid of the padding
        bi.seek(4 - (startPos % 4));

        int defaultOffset = bi.nextInt();

        int npairs = bi.nextInt();

        Map<Integer, Integer> lookupMap = new HashMap<>(npairs);

        for (int i = 0; i < npairs; i++) {
            lookupMap.put(bi.nextInt(), bi.nextInt());
        }

        Integer res = lookupMap.get(ctx.getStack().pop());

        int offset = res == null ? defaultOffset : res;

        bi.seek(offset - (bi.getPos() - startPos + 1)); // -1 because we popped 1 to know it was a LookupSwitch!
    }

    @Override
    public byte getByte() {
        return (byte) 0xAB;
    }
}
