package com.kakai.android.autoviewmodelfactory.processor.utils;

import java.util.Collection;
import java.util.stream.Collectors;

public class StringUtils {

    public static String join(Collection<String> collection, String delimiter) {
        return collection.stream().collect(Collectors.joining(delimiter));
    }
}
