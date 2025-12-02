package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import org.bukkit.Material;

public class BlastingRecipesGenerator {
    public void registerRecipes(VWRecipeHelper recipeHelper) {
        recipeHelper.genBlastingRecipe(
                Material.MAGMA_BLOCK,
                Material.COBBLESTONE,
                0.7f,
                200
        );
        recipeHelper.genBlastingRecipe(
                Material.SOUL_SOIL,
                Material.SOUL_SAND,
                0.5f,
                100
        );
        recipeHelper.genBlastingRecipe(
                Material.SAND,
                Material.COARSE_DIRT,
                0.5f,
                100
        );
    }
}
