package com.exorastudios.library.dependency;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class ExoDependency {

    private ExoDependency() {}

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final List<Dependency> dependencies = new ArrayList<>();
        private Dependency current;

        public Builder hardDepend(String plugin, Object hook) {
            current = new Dependency(plugin, hook, true);
            dependencies.add(current);
            return this;
        }

        public Builder softDepend(String plugin, Object hook) {
            current = new Dependency(plugin, hook, false);
            dependencies.add(current);
            return this;
        }

        public Builder onMissing(Consumer<String> onMissing) {
            if (current != null) current.onMissing = onMissing;
            return this;
        }

        public Builder ifPresent(Consumer<String> ifPresent) {
            if (current != null) current.ifPresent = ifPresent;
            return this;
        }

        public void build() {
            for (Dependency dep : dependencies) {
                Plugin plugin = Bukkit.getPluginManager().getPlugin(dep.name);
                boolean isPresent = plugin != null && plugin.isEnabled();

                if (isPresent) {
                    dep.runRegister();
                    if (dep.ifPresent != null) dep.ifPresent.accept(dep.name);
                } else if (dep.hard) {
                    if (dep.onMissing != null) dep.onMissing.accept(dep.name);
                    throw new RuntimeException("[Dependency] Missing required plugin: " + dep.name);
                } else if (dep.onMissing != null) {
                    dep.onMissing.accept(dep.name);
                }
            }
        }
    }

    private static final class Dependency {
        final String name;
        final Object hook;
        final boolean hard;
        Consumer<String> onMissing;
        Consumer<String> ifPresent;

        Dependency(String name, Object hook, boolean hard) {
            this.name = name;
            this.hook = hook;
            this.hard = hard;
        }

        void runRegister() {
            if (hook == null) return;
            try {
                Method method = hook.getClass().getMethod("register");
                method.invoke(hook);
            } catch (Exception e) {
                throw new RuntimeException("[Dependency] Failed to register hook for " + name, e);
            }
        }
    }
}
