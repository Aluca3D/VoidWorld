package io.papermc.voidWorld

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class OneBlockGenerator(
  private val plugin: JavaPlugin,
) : Listener {
  private var oneBlockLocation: Location? = null
  private var scheduler: BukkitScheduler = plugin.server.scheduler

  @EventHandler
  fun onOneBlockBreak(event: BlockBreakEvent) {
    if (oneBlockLocation == null) return

    val block = event.block
    val location = block.location
    val player = event.player

    if (oneBlockLocation != location) return

    event.isDropItems = false

    val tool = player.inventory.itemInMainHand
    val drops = block.getDrops(tool)

    for (drop in drops) {
      val spawnLoc = location.clone().add(0.5, 1.0, 0.5)
      block.world.dropItem(spawnLoc, drop)
    }
  }

  fun setOneBlock() {
    scheduler.runTask(
      plugin,
      Runnable {
        val world = Bukkit.getWorld("world")
        if (world == null) {
          plugin.logger.warning("Overworld not found, cannot place OneBlock!")
          return@Runnable
        }

        val spawn = world.spawnLocation.block.location

        if (spawn.block.type != Material.AIR) {
          plugin.logger.info("OneBlock already exists at spawn, skipping placement.")
        } else {
          plugin.logger.info("Placed OneBlock at spawn: $spawn")
          spawn.block.type = Material.GRASS_BLOCK
        }
        oneBlockLocation = spawn.clone()
      },
    )
  }

  fun placeOneBlock() {
    scheduler.runTaskTimer(
      plugin,
      Runnable {
        val location = oneBlockLocation ?: return@Runnable
        val block = location.block

        if (block.type == Material.AIR) {
          block.type = Material.DIRT
        }
      },
      0L,
      1L,
    )
  }
}
