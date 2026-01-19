package io.papermc.voidWorld

import io.papermc.voidWorld.OConfigLoader.loadConfigFile
import io.papermc.voidWorld.buildStructureDetection.structure.BuddingAmethystDetection
import io.papermc.voidWorld.buildStructureDetection.structure.EndPortalDetection
import io.papermc.voidWorld.mobs.HideTagChecker
import io.papermc.voidWorld.mobs.config.MobLootDropConfig
import io.papermc.voidWorld.mobs.config.MobVariationSpawnConfig
import io.papermc.voidWorld.mobs.config.WanderingTraderConfig
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
    oneBlock.setOneBlock()
    oneBlock.placeOneBlock()

    // Structure Detection
    registerListener(EndPortalDetection())
    registerListener(BuddingAmethystDetection())

    // Mobs
    setupMobs()

    // Recipes
    setupRecipes()

    // Tag checker
    HideTagChecker(this).hideTagChecker()
  }

  override fun onDisable() {
    logger.info("VoidWorld disabled!")
  }

  private fun setupMobs() {
    loadMobLoot()
    loadMobVariation()
    loadWanderingTrader()
  }

  private fun setupRecipes() {
    val generator = RecipeGenerator(this)

    loadBlasting(generator)
    loadCampfire(generator)
    loadFurnace(generator)
    loadShaped(generator)
    loadShapeless(generator)
    loadSmoking(generator)
  }

  private fun loadMobLoot() {
    val node = loadConfigFile(this, "config/mobs/mLoot.json")
    val config = MobLootDropConfig(this, node)
    val listener = MobLootDrop(this, config)

    config.loadConfig()
    registerListener(listener)
  }

  private fun loadMobVariation() {
    val node = loadConfigFile(this, "config/mobs/mVariation.json")
    val config = MobVariationSpawnConfig(this, node)
    val listener = MobVariationSpawn(this, config)

    config.loadConfig()
    registerListener(listener)
  }

  private fun loadWanderingTrader() {
    val node = loadConfigFile(this, "config/mobs/wTrades.json")
    val config = WanderingTraderConfig(this, node)
    val listener = WanderingTraderTrades(this, config)

    registerListener(listener)
  }

  private fun loadBlasting(generator: RecipeGenerator) {
    val node = loadConfigFile(this, "config/recipes/blasting.json")
    BlastingRecipeConfig(generator, node).loadRecipes()
  }

  private fun loadCampfire(generator: RecipeGenerator) {
    val node = loadConfigFile(this, "config/recipes/campfire.json")
    CampfireRecipeConfig(generator, node).loadRecipes()
  }

  private fun loadFurnace(generator: RecipeGenerator) {
    val node = loadConfigFile(this, "config/recipes/furnace.json")
    FurnaceRecipeConfig(generator, node).loadRecipes()
  }

  private fun loadShaped(generator: RecipeGenerator) {
    val node = loadConfigFile(this, "config/recipes/shaped.json")
    ShapedRecipeConfig(generator, node).loadRecipes()
  }

  private fun loadShapeless(generator: RecipeGenerator) {
    val node = loadConfigFile(this, "config/recipes/shapeless.json")
    ShapelessRecipeConfig(generator, node).loadRecipes()
  }

  private fun loadSmoking(generator: RecipeGenerator) {
    val node = loadConfigFile(this, "config/recipes/smoking.json")
    SmokingRecipeConfig(generator, node).loadRecipes()
  }

  private fun registerListener(listener: Listener) {
    Bukkit.getPluginManager().registerEvents(listener, this)
  }
}
