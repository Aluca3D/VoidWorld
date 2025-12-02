package io.papermc.voidWorld;

import io.papermc.voidWorld.recipes.VWRecipeGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidWorld extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("VoidWorld loaded!");
    }

    @Override
    public void onEnable() {
        getLogger().info("VoidWorld enabled!");

        World world = Bukkit.getWorlds().getFirst();

        VWOneBlockGenerator oneBlock = new VWOneBlockGenerator(this);
        VWRecipeGenerator recipeGen = new VWRecipeGenerator(this);


        Bukkit.getPluginManager().registerEvents(oneBlock, this);

        oneBlock.setOneBlock(world);
        recipeGen.registerRecipes();
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidWorld disabled!");
    }
}
