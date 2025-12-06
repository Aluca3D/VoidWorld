package io.papermc.voidWorld.mobs.config;

import org.bukkit.entity.EntityType;

public record MobVariation(
        EntityType replacement,
        int intervalMin,
        int intervalMax,
        String standingOn,
        boolean burning,
        boolean storm,
        String effect,
        String dimension
) {}
