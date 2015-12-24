package com.mcintyret.jvm.core.clazz;

import com.mcintyret.jvm.core.type.SimpleType;

public interface ValueReceiver {

    void receiveSingleWidth(int i, SimpleType type);

    void receiveDoubleWidth(long l, SimpleType type);

}
