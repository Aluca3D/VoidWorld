package io.papermc.voidWorld.mobs.helper;

import io.papermc.voidWorld.helper.EDimension;

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
        EDimension inDimension,

        List<String> tags
) {
}
