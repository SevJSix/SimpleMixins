package me.ian.mixin;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MixinManager {
    private static final List<Mixin> mixins = new ArrayList<>();
    private static ClassLoader serverClassLoader;

    public static void init(ClassLoader classLoader) {
        serverClassLoader = classLoader;
        ByteBuddyAgent.install();
        applyMixins(ByteBuddyAgent.getInstrumentation());
        Logger.getGlobal().info("[SimpleMixins] Applied " + mixins.size() + " mixin(s)");
    }

    public static void register(Mixin mixin) {
        mixins.add(mixin);
    }

    private static void applyMixins(Instrumentation instrumentation) {
        AgentBuilder agentBuilder = new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .ignore(ElementMatchers.nameStartsWith("net.bytebuddy."));

        for (Mixin mixin : mixins) {
            agentBuilder = agentBuilder
                    .type(mixin.getTargetMatcher())
                    .and(ElementMatchers.declaresMethod(mixin.getMethodMatcher()))
                    .transform((builder, type, classLoader, module, pd) -> {
                        if (classLoader != serverClassLoader) {
                            Logger.getGlobal().warning("[SimpleMixins] Skipping classloader: " + classLoader);
                            return builder;
                        }

                        Logger.getGlobal().info("[SimpleMixins] Successfully transformed class: " + type.getName());
                        return builder.visit(Advice.to(mixin.getAdviceClass()).on(mixin.getMethodMatcher()));
                    });
        }

        agentBuilder.installOn(instrumentation);
    }
}