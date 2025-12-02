package io.papermc.voidWorld;

import io.papermc.paper.datapack.Datapack;
import io.papermc.voidWorld.buildStructureDetection.structures.EndPortalDetection;
import io.papermc.voidWorld.recipes.VWRecipeGenerator;
import io.papermc.voidWorld.buildStructureDetection.VWPlayerStructureRegistry;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class VoidWorld extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("VoidWorld loaded!");
    }

    private VWOneBlockGenerator oneBlock;
    private VWRecipeGenerator recipeGen;

    @Override
    public void onEnable() {
        getLogger().info("VoidWorld enabled!");

        // Player Structure Detectors
        EndPortalDetection endPortalDetection = new EndPortalDetection();

        VWPlayerStructureRegistry playerStructReg = new VWPlayerStructureRegistry(
                Arrays.asList(
                        endPortalDetection
                )
        );

        Bukkit.getPluginManager().registerEvents(playerStructReg, this);

        oneBlock = new VWOneBlockGenerator(this);
        recipeGen = new VWRecipeGenerator(this);

        World world = Bukkit.getWorlds().getFirst();

        Bukkit.getPluginManager().registerEvents(oneBlock, this);

        oneBlock.setOneBlock(world);
        recipeGen.registerRecipes();

        Datapack pack = this.getServer().getDatapackManager().getPack(getPluginMeta().getName() + "/provided");
        if (pack != null) {
            if (pack.isEnabled()) {
                getLogger().info("The Datapack loaded successfully!");
            } else {
                getLogger().warning("The datapack failed to load.");
            }
        } else {
            getLogger().warning("The datapack was not found.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidWorld disabled!");
    }
}
