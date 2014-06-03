package com.mcintyret.jvm.parse.attribute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mcintyret.jvm.core.ByteIterator;

public interface Parser<T> {

    default List<T> parseMulti(ByteIterator bi) {
        int size = bi.nextShort();
        List<T> list = size == 0 ? Collections.emptyList() : new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(parse(bi));
        }
        return list;
    }

    T parse(ByteIterator bi);

}
