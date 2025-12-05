package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeInterface;
import org.bukkit.Material;

public class BlastingRecipesGenerator implements VWRecipeInterface {
    @Override
    public void registerRecipes(VWRecipeHelper recipeHelper) {

        recipeHelper.genBlastingRecipe(
                "magma_from_cobble",
                Material.MAGMA_BLOCK,
                Material.COBBLESTONE,
                0.7f,
                200
        );

        recipeHelper.genBlastingRecipe(
                "soulsoil_from_soulsand",
                Material.SOUL_SOIL,
                Material.SOUL_SAND,
                0.5f,
                100
        );

        recipeHelper.genBlastingRecipe(
                "sand_from_coarse_dirt",
                Material.SAND,
                Material.COARSE_DIRT,
                0.5f,
                100
        );
    }
}
