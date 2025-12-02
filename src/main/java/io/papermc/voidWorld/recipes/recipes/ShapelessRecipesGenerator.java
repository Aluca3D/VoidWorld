package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

public class ShapelessRecipesGenerator {
    public void registerRecipes(VWRecipeHelper recipeHelper) {
        recipeHelper.genShapelessRecipe(
                "_from_gunpowder",
                Material.GRAVEL, 2,
                2, Material.DIRT,
                2, Material.GUNPOWDER
        );
        recipeHelper.genShapelessRecipe(
                "_from_bone_meal",
                Material.GRAVEL, 1,
                3, Material.DIRT,
                1, Material.BONE_MEAL
        );
        recipeHelper.genShapelessRecipe(
                Material.GRASS_BLOCK, 1,
                1, Material.DIRT,
                8, Material.BONE_MEAL
        );
        recipeHelper.genShapelessRecipe(
                Material.COBBLESTONE, 2,
                4, Material.GRAVEL
        );
        recipeHelper.genShapelessRecipe(
                Material.OBSIDIAN, 1,
                1, Material.MAGMA_BLOCK,
                1, Material.WATER_BUCKET
        );
        recipeHelper.genShapelessRecipe(
                Material.LAVA_BUCKET, 1,
                1, Material.MAGMA_BLOCK,
                1, Material.BUCKET
        );
        recipeHelper.genShapelessRecipe(
                Material.COBBLED_DEEPSLATE, 4,
                9, Material.BLACKSTONE
        );
        recipeHelper.genShapelessRecipe(
                Material.COBWEB, 1,
                9, Material.STRING
        );
        recipeHelper.genShapelessRecipe(
                Material.RED_SAND, 4,
                4, Material.SAND,
                1, new RecipeChoice.MaterialChoice(
                        Material.RED_DYE,
                        Material.ORANGE_DYE
                )
        );
    }
}
