package io.papermc.voidWorld;

import io.papermc.voidWorld.buildStructureDetection.structure.EndPortalDetection;
import io.papermc.voidWorld.mobs.VWMobDrops;
import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeRegistry;
import io.papermc.voidWorld.recipes.recipes.*;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public final class VoidWorld extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("VoidWorld loaded!");
    }

    @Override
    public void onEnable() {
        getLogger().info("VoidWorld enabled!");

        // Create Player Structure Detector
        EndPortalDetection endPortalDetection = new EndPortalDetection();

        // Recipes
        VWRecipeRegistry recipeRegistry = new VWRecipeRegistry(
                Arrays.asList(
                        new ShapedRecipesGenerator(),
                        new ShapelessRecipesGenerator(),
                        new FurnaceRecipesGenerator(),
                        new BlastingRecipesGenerator(),
                        new SmokingRecipesGenerator()
                )

        );
        VWRecipeHelper helper = new VWRecipeHelper(this);
        recipeRegistry.registerAll(helper);

        // Mobs
        ///  Loot
        VWMobDrops mobDrops = new VWMobDrops();
        /// Spawn
        /// Trade

        // OneBlock
        VWOneBlockGenerator oneBlock = new VWOneBlockGenerator(this);
        Bukkit.getScheduler().runTask(this, oneBlock::setOneBlock);

        registerEventListeners(
                Arrays.asList(
                        mobDrops,
                        oneBlock,
                        endPortalDetection
                )
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidWorld disabled!");
    }

    public JavaPlugin getPlugin() {
        return this;
    }

    private void registerEventListeners(List<Listener> listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
