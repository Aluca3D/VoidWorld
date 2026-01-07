package io.papermc.voidWorld.mobs.helper

import io.papermc.voidWorld.helper.EDimension

@JvmRecord
data class RDropDefinition(
    val itemStackConfiguration: RItemStackConfiguration?,
    val minAmount: Int,
    val maxAmount: Int,
    val chance: Double,
    val lootingEnabled: Boolean?,
    val extraChancePerLevel: Double,
    val extraAmountPerLevel: Int,
    val useDimension: Boolean?,
    val inDimension: EDimension?,
    val tags: List<String>?
)
