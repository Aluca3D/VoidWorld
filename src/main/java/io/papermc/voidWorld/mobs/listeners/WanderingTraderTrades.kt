package io.papermc.voidWorld.mobs.listeners

import io.papermc.voidWorld.mobs.config.WanderingTraderConfig
import org.bukkit.entity.WanderingTrader
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

class WanderingTraderTrades(
  var plugin: JavaPlugin,
  val trades: WanderingTraderConfig,
) : Listener {
  private var scheduler: BukkitScheduler = plugin.server.scheduler

  @EventHandler
  fun onWanderingTraderSpawn(event: CreatureSpawnEvent) {
    val trader = event.entity as? WanderingTrader ?: return

    scheduler.runTaskLater(
      plugin,
      Runnable {
        val newTrades = trader.recipes.toMutableList()
        newTrades.addAll(trades.getTrades())

        trader.recipes = newTrades
      },
      1L,
    )
  }
}
