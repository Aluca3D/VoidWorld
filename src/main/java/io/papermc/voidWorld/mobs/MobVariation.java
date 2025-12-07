package io.papermc.voidWorld.mobs;

import io.papermc.voidWorld.helper.VWDimension;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

public record MobVariation(
        EntityType replacement,
        int intervalMin,
        int intervalMax,
        boolean isBurning,
        boolean isHitByLightning,
        Material standingOn,
        PotionEffectType hasEffect,
        boolean useDimension,
        VWDimension inDimension
) {
}
