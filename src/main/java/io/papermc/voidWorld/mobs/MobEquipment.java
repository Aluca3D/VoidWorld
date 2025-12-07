package io.papermc.voidWorld.mobs;

import org.bukkit.inventory.ItemStack;

public record MobEquipment(
        ItemStack mainHand,
        ItemStack offHand,
        ItemStack helmet,
        ItemStack chestplate,
        ItemStack leggings,
        ItemStack boots
) {}