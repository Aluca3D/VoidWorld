package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeInterface;
import org.bukkit.Material;

public class FurnaceRecipesGenerator implements VWRecipeInterface {
    @Override
    public void registerRecipes(VWRecipeHelper recipeHelper) {

        recipeHelper.genFurnaceRecipe(
                "iron_nugget_from_gravel",
                Material.IRON_NUGGET,
                Material.GRAVEL,
                0.4f,
                100
        );
    }
}
