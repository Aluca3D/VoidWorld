package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeInterface;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

public class ShapelessRecipesGenerator implements VWRecipeInterface {
    @Override
    public void registerRecipes(VWRecipeHelper recipeHelper) {

        recipeHelper.genShapelessRecipe(
                "gravel_from_gunpowder",
                Material.GRAVEL, 2,
                2, Material.DIRT,
                2, Material.GUNPOWDER
        );

        recipeHelper.genShapelessRecipe(
                "gravel_from_bone_meal",
                Material.GRAVEL, 1,
                3, Material.DIRT,
                1, Material.BONE_MEAL
        );

        recipeHelper.genShapelessRecipe(
                "grass_block_from_bonemeal",
                Material.GRASS_BLOCK, 1,
                1, Material.DIRT,
                8, Material.BONE_MEAL
        );

        recipeHelper.genShapelessRecipe(
                "cobblestone_from_gravel",
                Material.COBBLESTONE, 2,
                4, Material.GRAVEL
        );

        recipeHelper.genShapelessRecipe(
                "obsidian_from_magma_water",
                Material.OBSIDIAN, 1,
                1, Material.MAGMA_BLOCK,
                1, Material.WATER_BUCKET
        );

        recipeHelper.genShapelessRecipe(
                "lava_bucket_from_magma_bucket",
                Material.LAVA_BUCKET, 1,
                1, Material.MAGMA_BLOCK,
                1, Material.BUCKET
        );

        recipeHelper.genShapelessRecipe(
                "cobbled_deepslate_from_blackstone",
                Material.COBBLED_DEEPSLATE, 4,
                9, Material.BLACKSTONE
        );

        recipeHelper.genShapelessRecipe(
                "cobweb_from_string",
                Material.COBWEB, 1,
                9, Material.STRING
        );

        recipeHelper.genShapelessRecipe(
                "red_sand_from_dye",
                Material.RED_SAND, 4,
                4, Material.SAND,
                1, new RecipeChoice.MaterialChoice(
                        Material.RED_DYE,
                        Material.ORANGE_DYE
                )
        );
    }
}
