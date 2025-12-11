package io.papermc.voidWorld.mobs.helper;

import io.papermc.voidWorld.helper.VWDimension;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Map;

public record MobVariation(
        EntityType replacement,
        int intervalMin,
        int intervalMax,
        boolean isBurning,
        boolean isHitByLightning,
        Material standingOn,
        PotionEffectType hasEffect,
        boolean useDimension,
        VWDimension inDimension,

        Component name,
        Map<Attribute, Double> attributes,
        List<String> tags,
        MobEquipment equipment
) {
}
