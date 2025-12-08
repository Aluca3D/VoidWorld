package io.papermc.voidWorld.mobs.helper;

public record MobEquipment(
        ItemStackConfiguration mainHand,
        ItemStackConfiguration offHand,
        ItemStackConfiguration helmet,
        ItemStackConfiguration chestplate,
        ItemStackConfiguration leggings,
        ItemStackConfiguration boots
) {}