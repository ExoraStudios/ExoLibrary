package com.exorastudios.library.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public abstract class ExoListener implements Listener {

    protected <T extends Event> EventBuilder<T> on(Class<T> eventClass) {
        return new EventBuilder<>(eventClass, this);
    }

    public static final class EventBuilder<T extends Event> {
        private final Class<T> type;
        private final Listener listener;
        private EventPriority priority = EventPriority.NORMAL;
        private boolean ignoreCancelled = true;
        private Consumer<T> handler;

        public EventBuilder(Class<T> type, Listener listener) {
            this.type = type;
            this.listener = listener;
        }

        public EventBuilder<T> priority(EventPriority priority) {
            this.priority = priority;
            return this;
        }

        public EventBuilder<T> ignoreCancelled(boolean ignore) {
            this.ignoreCancelled = ignore;
            return this;
        }

        public void handle(Consumer<T> handler) {
            this.handler = handler;
            register();
        }

        private void register() {
            JavaPlugin plugin = JavaPlugin.getProvidingPlugin(listener.getClass());
            Bukkit.getPluginManager().registerEvent(type, listener, priority, (listener, event) -> {
                T e = type.cast(event);
                if (ignoreCancelled && e instanceof Cancellable c && c.isCancelled()) {
                    return;
                }
                handler.accept(e);
            }, plugin, ignoreCancelled);
        }
    }
}
