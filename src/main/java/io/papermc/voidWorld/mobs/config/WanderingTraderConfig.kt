package io.papermc.voidWorld.mobs.config

import io.papermc.voidWorld.mobs.helper.RItemStackConfiguration
import org.bukkit.inventory.MerchantRecipe
import org.bukkit.plugin.java.JavaPlugin
import org.spongepowered.configurate.ConfigurationNode
import kotlin.random.Random

class WanderingTraderConfig(
  var plugin: JavaPlugin,
  var root: ConfigurationNode,
) {
  fun getTrades(): MutableList<MerchantRecipe> {
    val pool: MutableList<MerchantRecipe> = mutableListOf()

    val tradesNode = root.node("tradeList")

    val minNewTrades = root.node("min").getInt(1)
    val mayNewTrades = root.node("max").getInt(minNewTrades + 1)
    val randomValue = Random.nextInt(minNewTrades, mayNewTrades + 1)

    for (trade in tradesNode.childrenList()) {
      val uses = trade.node("uses").getInt(0)
      val maxUses = trade.node("maxUses").getInt(9999)

      val resultItemNode = trade.node("resultItem")
      val resultAmount = resultItemNode.node("amount").getInt(1)

      val resultItemConf = RItemStackConfiguration.parseItem(resultItemNode) ?: continue
      val resultItem = RItemStackConfiguration.build(resultItemConf) ?: continue
      resultItem.amount = resultAmount

      val costItemsNode = trade.node("costItems")
      val costItemNodes = costItemsNode.childrenList()

      if (costItemNodes.isEmpty()) {
        continue
      }

      if (costItemNodes.size > 2) {
        plugin.logger.warning(
          "Trade has more than 2 cost items! Only first 2 will be used.",
        )
      }

      val recipe = MerchantRecipe(resultItem, uses, maxUses, false)

      for (costNode in costItemNodes.take(2)) {
        val costAmount = costNode.node("amount").getInt(1)

        val costItemConf =
          RItemStackConfiguration.parseItem(costNode) ?: continue

        val costItem =
          RItemStackConfiguration.build(costItemConf) ?: continue

        costItem.amount = costAmount

        recipe.addIngredient(costItem)
      }

      pool.add(recipe)
    }

    pool.shuffle()
    return pool.take(randomValue).toMutableList()
  }
}
