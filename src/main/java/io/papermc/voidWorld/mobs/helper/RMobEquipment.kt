package io.papermc.voidWorld.mobs.helper

@JvmRecord
data class RMobEquipment(
  val mainHand: RItemStackConfiguration?,
  val offHand: RItemStackConfiguration?,
  val helmet: RItemStackConfiguration?,
  val chestplate: RItemStackConfiguration?,
  val leggings: RItemStackConfiguration?,
  val boots: RItemStackConfiguration?,
)
