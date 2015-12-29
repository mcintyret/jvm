package com.mcintyret.jvm.core.opcode.branch;

import com.mcintyret.jvm.core.exec.OperationContext;
import com.mcintyret.jvm.core.opcode.OpCode;
import com.mcintyret.jvm.core.util.ByteIterator;

class TableSwitch extends OpCode {
    @Override
    public void execute(OperationContext ctx) {
        ByteIterator bi = ctx.getByteIterator();
        int startPos = bi.getPos();

        // first get rid of the padding
        int mod = startPos % 4;
        if (mod > 0) {
            bi.seek(4 - mod);
        }

        int defaultOffset = bi.nextInt();

        int val = ctx.getStack().popInt();

        int low = bi.nextInt();
        int high = bi.nextInt();

        if (val < low || val > high) {
            bi.seek(defaultOffset - (bi.getPos() - startPos + 1)); // -1 because we popped 1 to know it was a LookupSwitch!
        } else {
            bi.seek(4 * (val - low));
            int offset = bi.nextInt();
            bi.seek(offset - (bi.getPos() - startPos + 1)); // -1 because we popped 1 to know it was a LookupSwitch!
        }
    }

    @Override
    public byte getByte() {
        return (byte) 0xAA;
    }
}
