package io.papermc.voidWorld.mobs.listeners

import io.papermc.voidWorld.mobs.config.MobLootDropConfig
import io.papermc.voidWorld.mobs.helper.RItemStackConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class MobLootDrop(
  private val plugin: JavaPlugin,
  private val config: MobLootDropConfig,
) : Listener {
  private val random = Random()

  @EventHandler
  fun onMobDeath(event: EntityDeathEvent) {
    val entity = event.getEntity()

    val dimension = entity.world.environment

    val drops = config.getDrops(entity.type)
    if (drops.isEmpty()) return

    var lootingLevel = 0

    if (entity.killer != null) {
      lootingLevel =
        entity.killer!!
          .inventory
          .itemInMainHand
          .getEnchantmentLevel(Enchantment.LOOTING)
    }

    for (dropDefinition in drops) {
      val tags: List<String>? = dropDefinition.tags

      if (!tags.isNullOrEmpty()) {
        var matches = false

        for (tag in tags) {
          if (tag.isBlank()) continue

          if (entity.scoreboardTags.contains(tag)) {
            matches = true
            break
          }
        }

        if (!matches) continue
      }

      if (dropDefinition.useDimension == true && dropDefinition.inDimension!!.environment != dimension) continue

      var chance = dropDefinition.chance

      if (dropDefinition.lootingEnabled == true && lootingLevel > 0) {
        chance += dropDefinition.extraChancePerLevel * lootingLevel
      }

      if (random.nextDouble() > chance) continue

      var amount =
        dropDefinition.minAmount +
          random.nextInt(dropDefinition.maxAmount - dropDefinition.minAmount + 1)

      if (dropDefinition.lootingEnabled == true && lootingLevel > 0) {
        amount += dropDefinition.extraAmountPerLevel * lootingLevel
      }

      if (amount > 0) {
        val itemResult = RItemStackConfiguration.build(dropDefinition.itemStackConfiguration)
        itemResult?.amount = amount
        event.drops.add(itemResult)
      }
    }
  }
}
