package io.papermc.voidWorld

import io.papermc.voidWorld.OConfigLoader.loadConfigFile
import io.papermc.voidWorld.buildStructureDetection.structure.EndPortalDetection
import io.papermc.voidWorld.mobs.config.MobLootDropConfig
import io.papermc.voidWorld.mobs.config.MobVariationSpawnConfig
import io.papermc.voidWorld.mobs.listeners.MobLootDrop
import io.papermc.voidWorld.mobs.listeners.MobVariationSpawn
import io.papermc.voidWorld.mobs.listeners.WanderingTraderTrades
import io.papermc.voidWorld.recipes.RecipeGenerator
import io.papermc.voidWorld.recipes.config.BlastingRecipeConfig
import io.papermc.voidWorld.recipes.config.CampfireRecipeConfig
import io.papermc.voidWorld.recipes.config.FurnaceRecipeConfig
import io.papermc.voidWorld.recipes.config.ShapedRecipeConfig
import io.papermc.voidWorld.recipes.config.ShapelessRecipeConfig
import io.papermc.voidWorld.recipes.config.SmokingRecipeConfig
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler

@Suppress("UNUSED_PARAMETER")
class VoidWorld : JavaPlugin() {
  private var scheduler: BukkitScheduler = this.server.scheduler

  override fun onLoad() {
    logger.info("VoidWorld loaded!")

    // Check if Datapack is Loaded
    val pack = this.server.datapackManager.getPack(pluginMeta.name + "/provided")
    if (pack != null) {
      if (pack.isEnabled) {
        this.logger.info("The VoidWorld datapack loaded successfully!")
      } else {
        this.logger.warning("The VoidWorld datapack failed to loaded successfully!")
      }
    }
  }

  override fun onEnable() {
    logger.info("VoidWorld enabled!")

    // OneBlock
    val oneBlock = OneBlockGenerator(this)
    scheduler.runTask(this, Runnable { oneBlock.setOneBlock() })

    // Structure Detection
    val endPortalDetection = EndPortalDetection()

    // Mobs
    // / Variation/Loot
    val lootNode = loadConfigFile(this, "config/mobs/mob-loot.json")
    val lootConfig = MobLootDropConfig(this, lootNode)
    val mobLoot = MobLootDrop(this, lootConfig)

    val variationNode = loadConfigFile(this, "config/mobs/mob-variation.json")
    val spawnConfig = MobVariationSpawnConfig(this, variationNode)
    val mobVariation = MobVariationSpawn(this, spawnConfig)

    // / Wandering Trader
    val wanderingTrader = WanderingTraderTrades(this)

    // Recipes
    val recipeGenerator = RecipeGenerator(this)

    val blastingRecipeNode = loadConfigFile(this, "config/recipes/blasting.json")
    val campfireRecipeNode = loadConfigFile(this, "config/recipes/campfire.json")
    val furnaceRecipeNode = loadConfigFile(this, "config/recipes/furnace.json")
    val shapedRecipeNode = loadConfigFile(this, "config/recipes/shaped.json")
    val shapelessRecipeNode = loadConfigFile(this, "config/recipes/shapeless.json")
    val smokingRecipeNode = loadConfigFile(this, "config/recipes/smoking.json")

    BlastingRecipeConfig(recipeGenerator, blastingRecipeNode).loadRecipes()
    CampfireRecipeConfig(recipeGenerator, campfireRecipeNode).loadRecipes()
    FurnaceRecipeConfig(recipeGenerator, furnaceRecipeNode).loadRecipes()
    ShapedRecipeConfig(recipeGenerator, shapedRecipeNode).loadRecipes()
    ShapelessRecipeConfig(recipeGenerator, shapelessRecipeNode).loadRecipes()
    SmokingRecipeConfig(recipeGenerator, smokingRecipeNode).loadRecipes()

    // Register Event Listeners
    registerEventListeners(
      mutableListOf(
        oneBlock,
        mobLoot,
        mobVariation,
        wanderingTrader,
        endPortalDetection,
      ).toList().toMutableList(),
    )
  }

  override fun onDisable() {
    logger.info("VoidWorld disabled!")
  }

  private fun registerEventListeners(listeners: MutableList<Listener>) {
    for (listener in listeners) {
      Bukkit.getPluginManager().registerEvents(listener, this)
    }
  }
}
