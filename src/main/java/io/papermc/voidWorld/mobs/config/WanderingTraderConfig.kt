package io.papermc.voidWorld.mobs.config

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode

class WanderingTraderConfig(
  var plugin: JavaPlugin,
  // var root: ConfigurationNode,
) {
  fun getTrades(): MutableList<MerchantRecipe> {
    val pool: MutableList<MerchantRecipe> = mutableListOf()

    pool.add(
      createTrade(
        Material.DIRT,
        1,
        Material.DIRT,
        1,
      ),
    )

    pool.shuffle()

    return pool
  }

  private fun createTrade(
    costMat: Material,
    cost: Int,
    resultMat: Material,
    resultAmount: Int,
  ): MerchantRecipe {
    val trade = MerchantRecipe(
      ItemStack(resultMat, resultAmount),
      20,
    )

    trade.addIngredient(ItemStack(costMat, cost))
    trade.priceMultiplier = 0f

    return trade
  }
}
