package com.exorastudios.library.logging;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

public final class ExoLogger {

    private ExoLogger() {
        throw new UnsupportedOperationException("ExoLogger is a utility class and cannot be instantiated.");
    }

    public static class Log {

        public static void info(@NotNull String message) {
            log(message, LogLevel.INFO);
        }

        public static void warn(@NotNull String message) {
            log(message, LogLevel.WARN);
        }

        public static void error(@NotNull String message) {
            log(message, LogLevel.ERROR);
        }

        public static void debug(@NotNull String message) {
            log(message, LogLevel.DEBUG);
        }

        public static void fatal(@NotNull String message) {
            log(message, LogLevel.FATAL);
        }

        private static void log(@NotNull String message, @NotNull LogLevel level) {
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            String formattedMessage = format(message, level);
            console.sendMessage(formattedMessage);
        }

        private static @NotNull String format(@NotNull String message, @NotNull LogLevel level) {
            return level.getColorCode() + message;
        }
    }

    @Getter
    public enum LogLevel {
        INFO("§7"),
        WARN("§e"),
        ERROR("§c"),
        DEBUG("§a"),
        FATAL("§4");

        private final String colorCode;

        LogLevel(String colorCode) {
            this.colorCode = colorCode;
        }
    }
}
