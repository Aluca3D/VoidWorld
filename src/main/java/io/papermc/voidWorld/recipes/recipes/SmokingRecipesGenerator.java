package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeInterface;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.RecipeChoice;

public class SmokingRecipesGenerator implements VWRecipeInterface {
    @Override
    public void registerRecipes(VWRecipeHelper recipeHelper) {
        recipeHelper.genSmokingRecipe(
                Material.BLACKSTONE,
                Material.COBBLESTONE,
                0.7f,
                100
        );
        recipeHelper.genSmokingRecipe(
                Material.DEAD_BUSH,
                new RecipeChoice.MaterialChoice(
                        Tag.SAPLINGS.getValues().toArray(new Material[0])
                ),
                0.7f,
                100
        );
    }
}
