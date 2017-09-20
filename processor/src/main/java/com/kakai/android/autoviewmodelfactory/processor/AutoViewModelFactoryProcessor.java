package com.kakai.android.autoviewmodelfactory.processor;

import android.arch.lifecycle.ViewModelProvider;

import com.kakai.android.autoviewmodelfactory.annotations.AutoViewModelFactory;
import com.kakai.android.autoviewmodelfactory.processor.utils.AnnotationProcessingUtils;
import com.kakai.android.autoviewmodelfactory.processor.utils.StringUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.FilerException;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.inject.Inject;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes({
        "com.kakai.android.autoviewmodelfactory.annotations.AutoViewModelFactory"
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AutoViewModelFactoryProcessor extends AbstractProcessor {

    private static final String CLASS_SUFFIX = "Factory";

    private static final String FACTORY_CREATE_METHOD_NAME = "create";
    private static final String FACTORY_CREATE_METHOD_PARAMETER_NAME = "modelClass";

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Set<TypeElement> viewModels;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
        viewModels = new HashSet<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        try {
            if (!findAnnotatedViewModels(roundEnvironment)) {
                return true;
            }

            for (TypeElement viewModelElement : viewModels) {
                generateViewModelFactory(viewModelElement);
            }

        } catch (IOException e) {
            error("An error has occurred.");
            e.printStackTrace();
        }

        return true;
    }

    private boolean findAnnotatedViewModels(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(AutoViewModelFactory.class)) {

            if (element.getKind() != ElementKind.CLASS) {
                error(element, "Only classes can be annotated with @%s.",
                        AutoViewModelFactory.class.getSimpleName());
                return false;
            }

            TypeElement typeElement = (TypeElement) element;
            viewModels.add(typeElement);
        }

        return true;
    }

    private void generateViewModelFactory(TypeElement viewModelElement) throws IOException {
        String packageName = elements.getPackageOf(viewModelElement).getQualifiedName().toString();
        String viewModelName = viewModelElement.getSimpleName().toString();

        // Build class
        TypeSpec.Builder factoryClass = TypeSpec
                .classBuilder(viewModelName + CLASS_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(TypeName.get(ViewModelProvider.Factory.class));

        // ViewModel dependencies
        LinkedHashMap<String, TypeName> viewModelDependencies = new LinkedHashMap<>();

        List<ParameterSpec> constructorParams = new ArrayList<>();

        // Class methods
        for (Element classElement : elements.getAllMembers(viewModelElement)) {
            if (AnnotationProcessingUtils.isConstructor(classElement)) {
                ExecutableElement methodElement = (ExecutableElement) classElement;

                // Constructor parameters
                for (VariableElement variableElement : methodElement.getParameters()) {
                    TypeName parameterTypeName = TypeName.get(variableElement.asType());
                    String parameterName = variableElement.getSimpleName().toString();

                    viewModelDependencies.put(parameterName, parameterTypeName);
                    constructorParams.add(ParameterSpec.builder(parameterTypeName, parameterName).build());
                }
            }
        }

        // Build constructor
        MethodSpec.Builder constructorMethod = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Inject.class)
                .addParameters(constructorParams);

        // Build create method
        MethodSpec.Builder createMethod = MethodSpec
                .methodBuilder(FACTORY_CREATE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(ClassName.get(Class.class), FACTORY_CREATE_METHOD_PARAMETER_NAME)
                .returns(TypeName.get(viewModelElement.asType()));

        // Add the ViewModel dependencies to the generated factory
        for (Map.Entry<String, TypeName> entry : viewModelDependencies.entrySet()) {
            // Class field
            factoryClass.addField(entry.getValue(), entry.getKey(), Modifier.PRIVATE);

            // Constructor param
            constructorMethod.addStatement("this.$N = $L", entry.getKey(), entry.getKey());
        }

        // Add the ViewModel instantiation statement to the create method
        createMethod.addStatement(
                String.format("return ($T) new $T(%s)", StringUtils.join(viewModelDependencies.keySet(), ", ")),
                viewModelElement,
                viewModelElement
        );

        factoryClass.addMethod(constructorMethod.build());
        factoryClass.addMethod(createMethod.build());

        try {
            // Write the factory to a file
            JavaFile.builder(packageName, factoryClass.build()).build().writeTo(filer);
        } catch (FilerException e) {
            System.err.println(String.format("%s: %s", e.getClass().getSimpleName(), e.getMessage()));
        }
    }

    private void error(String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}