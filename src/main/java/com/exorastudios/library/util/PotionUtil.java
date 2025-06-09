package com.exorastudios.library.util;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class PotionUtil {

    private PotionUtil() {}

    public static void addPotionEffect(LivingEntity entity, PotionEffectType type, int durationSeconds, int amplifier, boolean ambient, boolean particles) {
        if (entity == null || type == null) return;
        PotionEffect effect = new PotionEffect(type, durationSeconds * 20, amplifier, ambient, particles);
        entity.addPotionEffect(effect, true);
    }

    public static void removePotionEffect(LivingEntity entity, PotionEffectType type) {
        if (entity == null || type == null) return;
        entity.removePotionEffect(type);
    }

    public static boolean hasPotionEffect(LivingEntity entity, PotionEffectType type) {
        if (entity == null || type == null) return false;
        return entity.hasPotionEffect(type);
    }
}
