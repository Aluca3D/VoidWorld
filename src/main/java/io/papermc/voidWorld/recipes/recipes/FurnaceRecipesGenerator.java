package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import org.bukkit.Material;

public class FurnaceRecipesGenerator {
    public void registerRecipes(VWRecipeHelper recipeHelper) {
        recipeHelper.genFurnaceRecipe(
                Material.IRON_NUGGET,
                Material.GRAVEL,
                0.4f,
                100
        );
    }
}
