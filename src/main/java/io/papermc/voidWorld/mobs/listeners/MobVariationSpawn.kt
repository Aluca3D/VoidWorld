package io.papermc.voidWorld.mobs.listeners

import io.papermc.voidWorld.mobs.config.MobVariationSpawnConfig
import io.papermc.voidWorld.mobs.helper.RItemStackConfiguration
import io.papermc.voidWorld.mobs.helper.RMobEquipment
import io.papermc.voidWorld.mobs.helper.RMobVariation
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.util.*
import java.util.function.BiConsumer

class MobVariationSpawn(
  private val plugin: JavaPlugin,
  private val config: MobVariationSpawnConfig,
) : Listener {
  private val mobCounts: MutableMap<NamespacedKey?, Int?> = HashMap()
  private val mobNextInterval: MutableMap<NamespacedKey?, Int?> = HashMap()

  private var scheduler: BukkitScheduler = plugin.server.scheduler

  private val burningCauses: EnumSet<DamageCause?> =
    EnumSet.of(
      DamageCause.FIRE,
      DamageCause.FIRE_TICK,
      DamageCause.LAVA,
      DamageCause.HOT_FLOOR,
      DamageCause.CAMPFIRE,
    )

  @EventHandler
  fun onMobSpawn(event: EntitySpawnEvent) {
    val (entity, keys) = getType(event) ?: return

    scheduler.runTaskLater(
      plugin,
      Runnable {
        for (key in keys) {
          if (config.getRandomInterval(key) == 0) continue
          if (isNotInDimension(entity, key)) continue
          if (!hasTags(entity, key)) continue

          val count = mobCounts.getOrDefault(key, 0)!! + 1
          mobCounts[key] = count

          val nextInterval =
            mobNextInterval.computeIfAbsent(key) { _: NamespacedKey? -> config.getRandomInterval(key) }!!

          if (count >= nextInterval) {
            val replacement = config.getReplacement(key)!!

            val variation = config.getVariation(key) ?: continue

            replaceEntity(entity, replacement, variation)

            mobCounts[key] = 0
            mobNextInterval[key] = config.getRandomInterval(key)
            break
          }
        }
      },
      1L,
    )
  }

  @EventHandler
  fun onMobDamage(event: EntityDamageEvent) {
    val (entity, keys) = getType(event) ?: return

    scheduler.runTaskLater(
      plugin,
      Runnable {
        for (key in keys) {
          if (config.getRandomInterval(key) != 0) continue
          if (isNotInDimension(entity, key)) continue
          if (isNotStandingOn(entity, key)) continue
          if (!hasTags(entity, key)) continue

          val variation = config.getVariation(key) ?: continue

          if (!variation.isHitByLightning!! && !variation.isBurning!!) continue
          if (variation.isHitByLightning && event.cause != DamageCause.LIGHTNING) continue

          if (variation.isBurning == true && !burningCauses.contains(event.cause)) {
            continue
          }

          val replacement = config.getReplacement(key)!!
          replaceEntity(entity, replacement, variation)
          break
        }
      },
      1L,
    )
  }

  @EventHandler
  fun onMobEffect(event: EntityPotionEffectEvent) {
    val (entity, keys) = getType(event) ?: return

    val effect = event.newEffect ?: return

    scheduler.runTaskLater(
      plugin,
      Runnable {
        for (key in keys) {
          if (config.getRandomInterval(key) != 0) continue
          if (isNotInDimension(entity, key)) continue
          if (isNotStandingOn(entity, key)) continue
          if (!hasTags(entity, key)) continue

          val variation = config.getVariation(key) ?: continue

          if (variation.hasEffect == null) continue
          if (Registry.MOB_EFFECT.get(variation.hasEffect.key()) !== effect.type) continue

          val replacement = config.getReplacement(key)!!
          replaceEntity(entity, replacement, variation)
          break
        }
      },
      1L,
    )
  }

  private fun getType(event: EntityEvent): Pair<LivingEntity, List<NamespacedKey>>? {
    val entity = event.entity as? LivingEntity ?: return null

    val type = entity.type
    if (!config.hasVariation(type)) return null

    val keys = config.getKeysForEntity(type)
    if (keys.isEmpty()) return null

    return Pair(entity, keys)
  }

  private fun replaceEntity(
    originalEntity: LivingEntity,
    replacementType: EntityType,
    variation: RMobVariation,
  ) {
    scheduler.runTaskLater(
      plugin,
      Runnable {
        val location = originalEntity.location
        originalEntity.remove()

        val spawned = location.world.spawnEntity(location, replacementType) as LivingEntity
        modifyEntity(spawned, variation)
      },
      1L,
    )
  }

  private fun addPassenger(
    spawned: LivingEntity,
    variation: RMobVariation,
  ) {
    if (spawned.passengers.isNotEmpty()) return

    val passengerType = variation.passenger?.replacement ?: return

    val entity = spawned.world.spawnEntity(spawned.location, passengerType) as LivingEntity
    modifyEntity(entity, variation.passenger)

    spawned.addPassenger(entity)
    if (variation.passenger.passenger != null) {
      addPassenger(entity, variation.passenger)
    }
  }

  private fun modifyEntity(
    spawned: LivingEntity,
    variation: RMobVariation,
  ) {
    val tags: List<String>? = variation.giveTags

    if (tags != null) {
      for (tag in tags) {
        if (tag.isBlank()) continue
        spawned.addScoreboardTag(tag)
      }
    }

    if (variation.name != null) {
      spawned.customName(variation.name)
      spawned.isCustomNameVisible = true
    }

    variation.attributes?.forEach(
      (
        BiConsumer { attribute: Attribute?, value: Double? ->
          var instance = spawned.getAttribute(attribute!!)
          if (instance == null) {
            spawned.registerAttribute(attribute)
            instance = spawned.getAttribute(attribute)
          }
          instance?.baseValue = value!!
        }
      ),
    )

    val equipment = variation.equipment
    applyEquipment(spawned, equipment)

    addPassenger(spawned, variation)
  }

  private fun applyEquipment(
    mob: LivingEntity,
    eq: RMobEquipment?,
  ) {
    if (eq == null || mob.equipment == null) return

    if (eq.mainHand != null) mob.equipment!!.setItemInMainHand(RItemStackConfiguration.build(eq.mainHand))

    if (eq.offHand != null) mob.equipment!!.setItemInOffHand(RItemStackConfiguration.build(eq.offHand))

    if (eq.helmet != null) mob.equipment!!.setHelmet(RItemStackConfiguration.build(eq.helmet))

    if (eq.chestplate != null) mob.equipment!!.setChestplate(RItemStackConfiguration.build(eq.chestplate))

    if (eq.leggings != null) mob.equipment!!.setLeggings(RItemStackConfiguration.build(eq.leggings))

    if (eq.boots != null) mob.equipment!!.setBoots(RItemStackConfiguration.build(eq.boots))
  }

  private fun isNotInDimension(
    entity: LivingEntity,
    key: NamespacedKey,
  ): Boolean {
    val variation = config.getVariation(key) ?: return false

    val environment = entity.world.environment
    val notInDimension = environment != variation.inDimension!!.environment

    variation.useDimension?.let { if (!it) return false }

    return notInDimension
  }

  private fun isNotStandingOn(
    entity: LivingEntity,
    key: NamespacedKey,
  ): Boolean {
    val variation = config.getVariation(key) ?: return false

    val blockBelow =
      entity.location
        .subtract(0.0, 0.1, 0.0)
        .block
        .type

    val standingOn = variation.standingOn ?: return false

    return blockBelow != standingOn
  }

  private fun hasTags(
    entity: LivingEntity,
    key: NamespacedKey,
  ): Boolean {
    val variation = config.getVariation(key) ?: return false

    val tags = variation.hasTags ?: emptyList()
    val entityTags = entity.scoreboardTags

    return if (tags.isEmpty()) {
      entityTags.isEmpty()
    } else {
      tags.all { it in entityTags }
    }
  }
}
