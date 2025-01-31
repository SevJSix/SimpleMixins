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

Example usage in your project:
```java
me.ian.mixin.MixinManager.register(new me.ian.mixin.Mixin(
    EntityPlayer.class, // target class
    "playerTick", // method name
    MixinEntityPlayer.class // mixin class
));
```

Initialize mixins:
```java
me.ian.mixin.MixinManager.init(javaPlugin); // Initialize mixins. Replace 'javaPlugin' with your plugin instance.
```
