package io.papermc.voidWorld.recipes;

import java.util.List;

public class VWRecipeRegistry {
    private final List<VWRecipeInterface> recipes;

    public VWRecipeRegistry(List<VWRecipeInterface> recipes) {
        this.recipes = recipes;
    }

    public void registerAll(VWRecipeHelper recipeHelper) {
        for (VWRecipeInterface recipe : recipes) {
            recipe.registerRecipes(recipeHelper);
        }
    }
}
