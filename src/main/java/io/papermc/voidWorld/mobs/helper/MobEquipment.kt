package io.papermc.voidWorld.mobs.helper

@JvmRecord
data class MobEquipment(
    val mainHand: ItemStackConfiguration?,
    val offHand: ItemStackConfiguration?,
    val helmet: ItemStackConfiguration?,
    val chestplate: ItemStackConfiguration?,
    val leggings: ItemStackConfiguration?,
    val boots: ItemStackConfiguration?
) 