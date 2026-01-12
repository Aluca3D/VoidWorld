package io.papermc.voidWorld.mobs.config

import io.papermc.voidWorld.mobs.helper.RItemStackConfiguration
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode

class WanderingTraderConfig(
  var plugin: JavaPlugin,
  var root: ConfigurationNode,
) {
  fun getTrades(): MutableList<MerchantRecipe> {
    val pool: MutableList<MerchantRecipe> = mutableListOf()

    val tradesNode = root.node("tradeList")

    for (trade in tradesNode.childrenList()) {
      val uses = trade.node("uses").getInt(0)
      val maxUses = trade.node("maxUses").getInt(9999)

      val costItemNode = trade.node("costItem")
      val costAmount = trade.node("costAmount").getInt(1)

      val costItemConf = RItemStackConfiguration.parseItem(costItemNode) ?: continue
      val costItem = RItemStackConfiguration.build(costItemConf) ?: continue
      costItem.amount = costAmount

      val resultItemNode = trade.node("resultItem")
      val resultAmount = trade.node("resultAmount").getInt(1)

      val resultItemConf = RItemStackConfiguration.parseItem(resultItemNode) ?: continue
      val resultItem = RItemStackConfiguration.build(resultItemConf) ?: continue
      resultItem.amount = resultAmount

      val recipe = MerchantRecipe(resultItem, uses, maxUses, false)
      recipe.addIngredient(costItem)

      pool.add(recipe)
    }

    pool.shuffle()
    return pool
  }
}
