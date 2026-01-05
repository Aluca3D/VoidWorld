package io.papermc.voidWorld

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class OneBlockGenerator(private val plugin: JavaPlugin) : Listener {
    private var oneBlockLocation: Location? = null
    private var scheduler: BukkitScheduler = plugin.server.scheduler

    fun setOneBlock() {
        val world = Bukkit.getWorld("world")
        if (world == null) {
            plugin.logger.warning("Overworld not found, cannot place OneBlock!")
            return
        }

        val spawn = world.spawnLocation.block.location

        if (spawn.block.type != Material.AIR) {
            plugin.logger.info("OneBlock already exists at spawn, skipping placement.")
        } else {
            plugin.logger.info("Placed OneBlock at spawn: $spawn")
            spawn.block.type = Material.DIRT
        }
        oneBlockLocation = spawn.clone()
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        if (event.getBlock().location == oneBlockLocation) {
            scheduler.runTaskLater(plugin, Runnable { oneBlockLocation?.block?.type = Material.DIRT }, 1L)
        }
    }
}
