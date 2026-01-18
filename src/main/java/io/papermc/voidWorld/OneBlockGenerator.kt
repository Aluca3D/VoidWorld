package io.papermc.voidWorld

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class OneBlockGenerator(
  private val plugin: JavaPlugin,
) {
  private var oneBlockLocation: Location? = null
  private var scheduler: BukkitScheduler = plugin.server.scheduler

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
          spawn.block.type = Material.DIRT
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
