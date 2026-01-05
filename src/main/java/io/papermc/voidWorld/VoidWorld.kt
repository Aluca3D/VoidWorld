package io.papermc.voidWorld

import io.papermc.voidWorld.VWConfigLoader.loadConfig
import io.papermc.voidWorld.buildStructureDetection.structure.EndPortalDetection
import io.papermc.voidWorld.mobs.config.VWMobLootDropConfig
import io.papermc.voidWorld.mobs.config.VWMobVariationSpawnConfig
import io.papermc.voidWorld.mobs.listeners.VWMobLootDrop
import io.papermc.voidWorld.mobs.listeners.VWMobVariationSpawn
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

class VoidWorld : JavaPlugin() {

    private var scheduler: BukkitScheduler = this.server.scheduler

    override fun onLoad() {
        logger.info("VoidWorld loaded!")
    }

    override fun onEnable() {
        logger.info("VoidWorld enabled!")

        // OneBlock
        val oneBlock = VWOneBlockGenerator(this)
        scheduler.runTask(this, Runnable { oneBlock.setOneBlock() })

        // Mob Variation/Loot
        val variationNode = loadConfig(this, "config/mobs/mob-variation.json")
        val lootNode = loadConfig(this, "config/mobs/mob-loot.json")

        val spawnConfig = VWMobVariationSpawnConfig(this, variationNode)
        val lootConfig = VWMobLootDropConfig(this, lootNode)

        // Recipes
        val recipeGenerator = RecipeGenerator(this)

        val blastingRecipeNode = loadConfig(this, "config/recipes/blasting.json")
        val campfireRecipeNode = loadConfig(this, "config/recipes/campfire.json")
        val furnaceRecipeNode = loadConfig(this, "config/recipes/furnace.json")
        val shapedRecipeNode = loadConfig(this, "config/recipes/shaped.json")
        val shapelessRecipeNode = loadConfig(this, "config/recipes/shapeless.json")
        val smokingRecipeNode = loadConfig(this, "config/recipes/smoking.json")

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
                VWMobLootDrop(this, lootConfig),
                VWMobVariationSpawn(this, spawnConfig),
                EndPortalDetection()
            ).toList().toMutableList()
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
