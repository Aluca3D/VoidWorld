package io.papermc.voidWorld.mobs

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class HideTagChecker(
  private val plugin: JavaPlugin,
) {
  private var scheduler: BukkitScheduler = plugin.server.scheduler

  private val hiddenTag = "hidden"
  private val seeingTag = "seeing"

  fun hideTagChecker() {
    scheduler.runTaskTimer(
      plugin,
      Runnable {
        plugin.server.onlinePlayers.forEach { player ->
          updateVisibility(player)

          val radius = 50.0 // TODO: Add Base Config
          val nearby = player.getNearbyEntities(radius, radius, radius)

          nearby.forEach { entity -> updateVisibility(entity) }
        }
      },
      0L,
      40L,
    )
  }

  private fun updateVisibility(entity: Entity) {
    val hidden = entity.scoreboardTags.contains(hiddenTag)

    plugin.server.onlinePlayers.forEach { viewer ->
      when {
        viewer.scoreboardTags.contains(seeingTag) -> {
          if (entity is Player) {
            viewer.showPlayer(plugin, entity)
          } else {
            viewer.showEntity(plugin, entity)
          }
        }

        hidden -> {
          if (entity is Player) {
            viewer.hidePlayer(plugin, entity)
          } else {
            viewer.hideEntity(plugin, entity)
          }
        }

        else -> {
          if (entity is Player) {
            viewer.showPlayer(plugin, entity)
          } else {
            viewer.showEntity(plugin, entity)
          }
        }
      }
    }
  }
}
