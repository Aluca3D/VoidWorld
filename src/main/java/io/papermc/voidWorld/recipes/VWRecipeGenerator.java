package io.papermc.voidWorld.recipes;

import java.util.List;

public class VWRecipeGenerator {
    private final List<VWRecipeInterface> recipes;

    public VWRecipeGenerator(List<VWRecipeInterface> recipes) {
        this.recipes = recipes;
    }

    public List<VWRecipeInterface> getRecipes() {
        return recipes;
    }
}
