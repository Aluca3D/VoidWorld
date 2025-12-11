package io.papermc.voidWorld.mobs.helper;

import io.papermc.voidWorld.helper.VWDimension;

import java.util.List;

public record DropDefinition(
        ItemStackConfiguration itemStackConfiguration,
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
