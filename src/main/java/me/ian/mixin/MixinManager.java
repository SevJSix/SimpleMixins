package me.ian.mixin;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class MixinManager {
    private static final List<Mixin> mixins = new ArrayList<>();
    private static ClassLoader serverClassLoader;

    public static void init(JavaPlugin plugin) {
        serverClassLoader = plugin.getServer().getClass().getClassLoader();
        ByteBuddyAgent.install();
        applyMixins(ByteBuddyAgent.getInstrumentation(), plugin);
        plugin.getLogger().info("Applied " + mixins.size() + " mixin(s)");
    }

    public static void register(Mixin mixin) {
        mixins.add(mixin);
    }

    private static void applyMixins(Instrumentation instrumentation, JavaPlugin plugin) {
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
                            plugin.getLogger().warning("Skipping classloader: " + classLoader);
                            return builder;
                        }

                        plugin.getLogger().info("Successfully transformed class: " + type.getName());
                        return builder.visit(Advice.to(mixin.getAdviceClass()).on(mixin.getMethodMatcher()));
                    });
        }

        agentBuilder.installOn(instrumentation);
    }
}