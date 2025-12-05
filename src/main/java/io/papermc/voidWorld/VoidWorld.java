package io.papermc.voidWorld;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeRegistry;
import io.papermc.voidWorld.recipes.recipes.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class VoidWorld extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("VoidWorld loaded!");
    }

    @Override
    public void onEnable() {
        getLogger().info("VoidWorld enabled!");

        World world = Bukkit.getWorlds().getFirst();

        VWRecipeHelper helper = new VWRecipeHelper(this);

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

        recipeRegistry.registerAll(helper);

        // OneBlock
        VWOneBlockGenerator oneBlock = new VWOneBlockGenerator(this);
        Bukkit.getPluginManager().registerEvents(oneBlock, this);
        oneBlock.setOneBlock(world);
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidWorld disabled!");
    }
}
