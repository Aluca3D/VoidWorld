package io.papermc.voidWorld;

import io.papermc.voidWorld.buildStructureDetection.VWPlayerStructureRegistry;
import io.papermc.voidWorld.buildStructureDetection.structures.EndPortalDetection;
import io.papermc.voidWorld.recipes.VWRecipeGenerator;
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

        EndPortalDetection endPortalDetection = new EndPortalDetection();

        VWPlayerStructureRegistry playerStructReg = new VWPlayerStructureRegistry(
                Arrays.asList(
                        endPortalDetection
                )
        );

        Bukkit.getPluginManager().registerEvents(playerStructReg, this);

        // Recipe generators
        ShapedRecipesGenerator shapedGen = new ShapedRecipesGenerator();
        ShapelessRecipesGenerator shapelessGen = new ShapelessRecipesGenerator();
        FurnaceRecipesGenerator furnaceGen = new FurnaceRecipesGenerator();
        BlastingRecipesGenerator blastingGen = new BlastingRecipesGenerator();
        SmokingRecipesGenerator smokingGen = new SmokingRecipesGenerator();

        // Create RecipeGeneratorList
        VWRecipeGenerator recipeGen = new VWRecipeGenerator(
                Arrays.asList(
                        shapedGen,
                        shapelessGen,
                        furnaceGen,
                        blastingGen,
                        smokingGen
                )

        );

        // Create Recipe Registry & Helper
        VWRecipeRegistry recipeRegistry = new VWRecipeRegistry(recipeGen.getRecipes());
        VWRecipeHelper recipeHelper = new VWRecipeHelper(this);

        // Register Recipes
        recipeRegistry.registerAll(recipeHelper);

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
