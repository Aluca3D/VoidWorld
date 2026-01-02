package io.papermc.voidWorld

import io.papermc.voidWorld.VWConfigLoader.loadConfig
import io.papermc.voidWorld.buildStructureDetection.structure.EndPortalDetection
import io.papermc.voidWorld.mobs.config.VWMobLootDropConfig
import io.papermc.voidWorld.mobs.config.VWMobVariationSpawnConfig
import io.papermc.voidWorld.mobs.listeners.VWMobLootDrop
import io.papermc.voidWorld.mobs.listeners.VWMobVariationSpawn
import io.papermc.voidWorld.recipes.RecipeGenerator
import io.papermc.voidWorld.recipes.VWRecipeHelper
import io.papermc.voidWorld.recipes.VWRecipeRegistry
import io.papermc.voidWorld.recipes.config.ShapedRecipeConfig
import io.papermc.voidWorld.recipes.recipes.*
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class VoidWorld : JavaPlugin() {
    override fun onLoad() {
        logger.info("VoidWorld loaded!")
    }

    override fun onEnable() {
        logger.info("VoidWorld enabled!")

        // Recipes OLD!!
        // TODO: Move to kotlin / config solution
        val recipeRegistry = VWRecipeRegistry(
            mutableListOf(
                ShapelessRecipesGenerator(),
                FurnaceRecipesGenerator(),
                BlastingRecipesGenerator(),
                SmokingRecipesGenerator()
            ).toList().toMutableList()
        )

        val helper = VWRecipeHelper(this)
        recipeRegistry.registerAll(helper)

        // OneBlock
        val oneBlock = VWOneBlockGenerator(this)
        Bukkit.getScheduler().runTask(this, Runnable { oneBlock.setOneBlock() })

        // Mob Variation/Loot
        val variationNode = loadConfig(this, "config/mobs/mob-variation.json")
        val lootNode = loadConfig(this, "config/mobs/mob-loot.json")

        val spawnConfig = VWMobVariationSpawnConfig(this, variationNode)
        val lootConfig = VWMobLootDropConfig(this, lootNode)

        // Recipes
        val recipeGenerator = RecipeGenerator(this)
        val shapedRecipeNode = loadConfig(this, "config/recipes/shaped.json")

        ShapedRecipeConfig(recipeGenerator, shapedRecipeNode).loadRecipes()

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
