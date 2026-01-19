package io.papermc.voidWorld.buildStructureDetection.structure

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BuddingAmethystDetection : Listener {
  @EventHandler
  fun onAmethystBreak(event: BlockBreakEvent) {
    val block = event.block.location.block
    val world = block.world
    val player = event.player
    val item = player.inventory.itemInMainHand

    if (item.isEmpty || item.type == Material.AIR) return
    if (block.type != Material.AMETHYST_BLOCK) return

    if (item.type.name.endsWith("_HOE", true)) {
      if (world.getBlockAt(block.x, block.y - 1, block.z).type != Material.CALCITE) return
      if (world.getBlockAt(block.x, block.y - 2, block.z).type != Material.SMOOTH_BASALT) return

      event.isCancelled = true
      block.type = Material.BUDDING_AMETHYST
    }
  }
}
