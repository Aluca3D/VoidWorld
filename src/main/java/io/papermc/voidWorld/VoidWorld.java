package io.papermc.voidWorld;

import io.papermc.voidWorld.buildStructureDetection.structure.EndPortalDetection;
import io.papermc.voidWorld.mobs.VWMobDrops;
import io.papermc.voidWorld.mobs.VWMobSpawn;
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

        // OneBlock
        VWOneBlockGenerator oneBlock = new VWOneBlockGenerator(this);
        Bukkit.getScheduler().runTask(this, oneBlock::setOneBlock);

        registerEventListeners(
                Arrays.asList(
                        oneBlock,
                        new VWMobDrops(),
                        new VWMobSpawn(this),
                        new EndPortalDetection()
                )
        );
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidWorld disabled!");
    }

    private void registerEventListeners(List<Listener> listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }
}
