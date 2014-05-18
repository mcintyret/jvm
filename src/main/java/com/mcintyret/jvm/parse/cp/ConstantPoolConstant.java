package com.mcintyret.jvm.parse.cp;

import com.mcintyret.jvm.core.ByteIterator;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public enum ConstantPoolConstant {
    CLASS(7) {
        @Override
        public Object parse(ByteIterator bi) {
            return new CpClass(bi.nextShort());
        }
    },
    FIELD_REF(9) {
        @Override
        public Object parse(ByteIterator bi) {
            return new FieldReference(bi.nextShort(), bi.nextShort());
        }
    },
    METHOD_REF(10) {
        @Override
        public Object parse(ByteIterator bi) {
            return new MethodReference(bi.nextShort(), bi.nextShort());
        }
    },
    INTERFACE_METHOD_REF(11) {
        @Override
        public Object parse(ByteIterator bi) {
            return new InterfaceMethodReference(bi.nextShort(), bi.nextShort());
        }
    },
    STRING(8) {
        @Override
        public Object parse(ByteIterator bi) {
            return new String(bi.nextShort());
        }
    },
    INTEGER(3) {
        @Override
        public Object parse(ByteIterator bi) {
            return new Int(bi.nextInt());
        }
    },
    FLOAT(4) {
        @Override
        public Object parse(ByteIterator bi) {
            return new Float(bi.nextInt());
        }
    },
    LONG(5) {
        @Override
        public Object parse(ByteIterator bi) {
            return new Long(bi.nextInt(), bi.nextInt());
        }
    },
    DOUBLE(6) {
        @Override
        public Object parse(ByteIterator bi) {
            return new Double(bi.nextInt(), bi.nextInt());
        }
    },
    NAME_AND_TYPE(12) {
        @Override
        public Object parse(ByteIterator bi) {
            return new NameAndType(bi.nextShort(), bi.nextShort());
        }
    },
    UTF8(1) {
        @Override
        public Object parse(ByteIterator bi) {
            int length = bi.nextShort();
            return new java.lang.String(bi.nextBytes(length));
        }
    };

    private final byte b;

    private ConstantPoolConstant(int b) {
        this.b = (byte) b;
    }

    private static final Map<Byte, ConstantPoolConstant> MAP = makeMap();

    private static Map<Byte, ConstantPoolConstant> makeMap() {
        Map<Byte, ConstantPoolConstant> map = new TreeMap<>();
        for (ConstantPoolConstant cpc : values()) {
            map.put(cpc.b, cpc);
        }
        return Collections.unmodifiableMap(map);
    }

    public static ConstantPoolConstant forByte(byte b) {
        ConstantPoolConstant cpc = MAP.get(b);
        if (cpc == null) {
            throw new IllegalArgumentException("No ConstantPoolConstant for " + b);
        }
        return cpc;
    }

    public abstract Object parse(ByteIterator bi);
}
