package io.papermc.voidWorld.mobs

import io.papermc.paper.event.player.AsyncChatEvent
import io.papermc.voidWorld.ConfigLoader
import io.papermc.voidWorld.YamlSettings
import io.papermc.voidWorld.mobs.helper.OEntityTags
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class HideTagChecker(
  private val plugin: JavaPlugin,
) : Listener {
  private val scheduler: BukkitScheduler = plugin.server.scheduler
  private val config = ConfigLoader(plugin).loadYamlConfig()
  private val settings = YamlSettings(config)

  private val radius: Double = settings.hide.radius
  private val period: Long = settings.hide.period

  private val hiddenTag: String = OEntityTags.HIDDEN_TAG
  private val seeingTag: String = OEntityTags.SEEING_TAG

  @EventHandler
  fun onChat(event: AsyncChatEvent) {
    val sender = event.player

    if (!sender.scoreboardTags.contains(hiddenTag)) return

    event.message(
      Component
        .text("[HIDDEN] ")
        .color(NamedTextColor.GRAY)
        .append(event.message()),
    )

    event.viewers().removeIf { audience ->
      val viewer = audience as? Player ?: return@removeIf true

      !viewer.scoreboardTags.contains(seeingTag) &&
        !viewer.scoreboardTags.contains(hiddenTag)
    }
  }

  fun checker() {
    scheduler.runTaskTimer(
      plugin,
      Runnable {
        plugin.server.onlinePlayers.forEach { player ->
          updateVisibility(player)

          val nearby = player.getNearbyEntities(radius, radius, radius)
          nearby.forEach { entity -> updateVisibility(entity) }
        }
      },
      0L,
      period,
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
