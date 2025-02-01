# SimpleMixins

A simple, lightweight API using instrumentation to transform compiled java code at runtime.

To allow the Java agent to attach properly, use these server startup script flags:
```bash
java -Djdk.attach.allowAttachSelf=true -XX:+EnableDynamicAgentLoading -noverify -jar server.jar
```

## Use in your project
Gradle Groovy:
```gradle
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("com.github.SevJSix:SimpleMixins:1.0.0")
}
```

Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.SevJSix</groupId>
        <artifactId>SimpleMixins</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>

```

## Code exmaples

Create a new mixin
```java
public class MixinExample {

    @Advice.OnMethodEnter
    public static void addBefore(@Advice.This Example example, @Advice.Argument(0) int val) {
        System.out.println("Intercepted aMethod(int): " + val);
    }
}
```

Register mixin class
```java
MixinManager.register(new me.ian.mixin.Mixin(
    Example.class, // target class
    "aMethod", // method name
    new Class[]{int.class}, // parameter types
    void.class, // return type
    MixinExample.class // mixin class
));
```

Initialize and apply mixins:
```java
MixinManager.init(classLoader); // replace 'classLoader' with your target classloader
```
