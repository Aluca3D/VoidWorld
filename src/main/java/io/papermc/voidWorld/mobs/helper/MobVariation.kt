package io.papermc.voidWorld.mobs.helper

import io.papermc.voidWorld.helper.EDimension
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.potion.PotionEffectType

@JvmRecord
data class MobVariation(
    val replacement: EntityType?,
    val intervalMin: Int,
    val intervalMax: Int,
    val isBurning: Boolean,
    val isHitByLightning: Boolean,
    val standingOn: Material?,
    val hasEffect: PotionEffectType?,
    val useDimension: Boolean,
    val inDimension: EDimension?,
    val name: Component?,
    val attributes: Map<Attribute?, Double>?,
    val tags: List<String>?,
    val equipment: MobEquipment?
)
