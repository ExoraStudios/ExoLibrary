package com.exorastudios.library.util;

public final class ServerVersionUtil {

    private static final String VERSION = getServerVersion();

    private ServerVersionUtil() {}

    private static String getServerVersion() {
        String packageName = org.bukkit.Bukkit.getServer().getClass().getPackageName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public static String getVersion() {
        return VERSION;
    }

    public static boolean isVersionAtLeast(String version) {
        return VERSION.compareTo(version) >= 0;
    }
}
