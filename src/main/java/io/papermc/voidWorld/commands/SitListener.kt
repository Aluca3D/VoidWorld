package io.papermc.voidWorld.commands

import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDismountEvent

class SitListener : Listener {
  @EventHandler
  fun onDismount(event: EntityDismountEvent) {
    val entity = event.dismounted

    if (
      entity.type == EntityType.ARMOR_STAND &&
      entity.scoreboardTags.contains("sit_seat")
    ) {
      event.entity.location
        .add(0.0, 1.0, 0.0)
        .apply { event.entity.teleport(this) }
      entity.remove()
    }
  }
}
