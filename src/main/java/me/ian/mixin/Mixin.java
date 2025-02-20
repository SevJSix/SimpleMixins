package me.ian.mixin;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class Mixin {
    private final Class<?> targetClass;
    private final String methodName;
    private final Class<?>[] parameterTypes;
    private final Class<?> returnType;
    private final Class<?> adviceClass;
    private final boolean isConstructor;

    public Mixin(Class<?> targetClass, String methodName, Class<?>[] parameterTypes, Class<?> returnType, Class<?> adviceClass, boolean isConstructor) {
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
        this.adviceClass = adviceClass;
        this.isConstructor = isConstructor;
    }

    public ElementMatcher.Junction<? super TypeDescription> getTargetMatcher() {
        return ElementMatchers.is(targetClass);
    }

    public ElementMatcher.Junction<MethodDescription> getMethodMatcher() {
        if (isConstructor) {
            // Match constructors by parameter types
            return ElementMatchers.isConstructor()
                    .and(ElementMatchers.takesArguments(parameterTypes));
        } else {
            // Existing method matcher logic
            return ElementMatchers.named(methodName)
                    .and(ElementMatchers.takesArguments(parameterTypes))
                    .and(ElementMatchers.returns(returnType));
        }
    }

    public Class<?> getAdviceClass() { return adviceClass; }

}
