package com.mcintyret.jvm.parse.attribute;

import com.mcintyret.jvm.core.ByteIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Parser<T> {

    default List<T> parseMulti(ByteIterator bi) {
        int size = bi.nextShort();
        List<T> list = size == 0 ? Collections.emptyList() : new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
//            System.out.println(i);
            T t = parse(bi);
//            System.out.println("Parsed: " + t);
            list.add(t);
        }
        return list;
    }

    T parse(ByteIterator bi);

}
