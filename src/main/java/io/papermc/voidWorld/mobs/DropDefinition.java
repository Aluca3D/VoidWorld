package io.papermc.voidWorld.mobs;

import io.papermc.voidWorld.helper.VWDimension;
import org.bukkit.Material;

import java.util.List;

public record DropDefinition(
        Material material,
        int minAmount,
        int maxAmount,
        double chance,
        boolean lootingEnabled,
        double extraChancePerLevel,
        int extraAmountPerLevel,
        boolean useDimension,
        VWDimension inDimension,

        List<String> tags
) {
}
