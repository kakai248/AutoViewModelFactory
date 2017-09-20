package com.kakai.android.autoviewmodelfactory.processor.utils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

public class AnnotationProcessingUtils {

    public static boolean isConstructor(Element element) {
        return element instanceof ExecutableElement && element.getSimpleName().contentEquals("<init>");
    }
}
