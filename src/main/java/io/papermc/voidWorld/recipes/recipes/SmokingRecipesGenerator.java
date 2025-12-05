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
                "blackstone_from_cobblestone",
                Material.BLACKSTONE,
                Material.COBBLESTONE,
                0.7f,
                100
        );
        recipeHelper.genSmokingRecipe(
                "dead_bush_from_saplings",
                Material.DEAD_BUSH,
                new RecipeChoice.MaterialChoice(
                        Tag.SAPLINGS.getValues().toArray(Material[]::new)
                ),
                0.7f,
                100
        );
    }
}
