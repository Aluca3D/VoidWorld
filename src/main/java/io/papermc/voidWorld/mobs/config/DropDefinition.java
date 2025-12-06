package io.papermc.voidWorld.mobs.config;

import org.bukkit.Material;

public record DropDefinition(
        Material material,
        int minAmount,
        int maxAmount,
        double chance,
        boolean lootingEnabled,
        double extraChancePerLevel,
        int extraAmountPerLevel
) {
}
