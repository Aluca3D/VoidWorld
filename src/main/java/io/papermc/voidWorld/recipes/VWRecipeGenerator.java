package io.papermc.voidWorld.recipes;

import io.papermc.voidWorld.recipes.recipes.*;
import org.bukkit.plugin.java.JavaPlugin;

public class VWRecipeGenerator {
    private final JavaPlugin plugin;

    public VWRecipeGenerator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        VWRecipeHelper recipeHelper = new VWRecipeHelper(plugin);

        ShapedRecipesGenerator shapedGen = new ShapedRecipesGenerator();
        ShapelessRecipesGenerator shapelessGen = new ShapelessRecipesGenerator();
        FurnaceRecipesGenerator furnaceGen = new FurnaceRecipesGenerator();
        BlastingRecipesGenerator blastingGen = new BlastingRecipesGenerator();
        SmokingRecipesGenerator smokingGen = new SmokingRecipesGenerator();

        shapedGen.registerRecipes(recipeHelper);
        shapelessGen.registerRecipes(recipeHelper);
        furnaceGen.registerRecipes(recipeHelper);
        blastingGen.registerRecipes(recipeHelper);
        smokingGen.registerRecipes(recipeHelper);
    }
}
