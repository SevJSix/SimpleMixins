import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class Mixin {
    private final Class<?> targetClass;
    private final String methodName;
    private final Class<?> adviceClass;

    public Mixin(Class<?> targetClass, String methodName, Class<?> adviceClass) {
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.adviceClass = adviceClass;
    }

    public ElementMatcher.Junction<? super TypeDescription> getTargetMatcher() {
        return ElementMatchers.is(targetClass);
    }

    public ElementMatcher.Junction<MethodDescription> getMethodMatcher() {
        return ElementMatchers.named(methodName);
    }

    public Class<?> getAdviceClass() { return adviceClass; }

}
