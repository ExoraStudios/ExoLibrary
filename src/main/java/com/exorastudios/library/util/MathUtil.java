package com.exorastudios.library.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public final class MathUtil {

    private MathUtil() {}


    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }


    public static double round(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }


    public static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static double randomDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    public static boolean chance(double percent) {
        return ThreadLocalRandom.current().nextDouble(0, 100) < percent;
    }


    public static double percent(double value, double total) {
        return total == 0 ? 0 : (value / total) * 100;
    }

    public static double percentOf(double percent, double total) {
        return (percent / 100.0) * total;
    }


    public static double distance2D(Location a, Location b) {
        if (!a.getWorld().equals(b.getWorld())) return -1;
        return Math.hypot(a.getX() - b.getX(), a.getZ() - b.getZ());
    }

    public static double distance3D(Location a, Location b) {
        if (!a.getWorld().equals(b.getWorld())) return -1;
        return a.distance(b);
    }


    public static Vector direction(Location from, Location to) {
        return to.toVector().subtract(from.toVector()).normalize();
    }

    public static Vector midpoint(Vector a, Vector b) {
        return new Vector((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2, (a.getZ() + b.getZ()) / 2);
    }

    public static double angleBetween(Vector a, Vector b) {
        return Math.toDegrees(Math.acos(a.clone().normalize().dot(b.clone().normalize())));
    }

    public static double magnitude(Vector v) {
        return v.length();
    }

    public static double magnitudeSquared(Vector v) {
        return v.lengthSquared();
    }


    public static int mod(int a, int b) {
        return (a % b + b) % b;
    }


    public static double lerp(double a, double b, double t) {
        return a + t * (b - a);
    }


    public static double map(double value, double inMin, double inMax, double outMin, double outMax) {
        if (inMin == inMax) return outMin;
        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }


    public static double normalize(double value, double min, double max) {
        if (min == max) return 0;
        return (value - min) / (max - min);
    }


    public static int sign(double value) {
        return (int) Math.signum(value);
    }


    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }


    public static int nextPowerOfTwo(int n) {
        if (n <= 0) return 1;
        return Integer.highestOneBit(n - 1) << 1;
    }
}
