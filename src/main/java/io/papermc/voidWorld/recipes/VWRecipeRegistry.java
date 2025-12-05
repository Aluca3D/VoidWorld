package io.papermc.voidWorld.recipes;

import java.util.List;

public class VWRecipeRegistry {
    private final List<VWRecipeInterface> generators;

    public VWRecipeRegistry(List<VWRecipeInterface> generators) {
        this.generators = generators;
    }

    public void registerAll(VWRecipeHelper helper) {
        for (VWRecipeInterface generator : generators) {
            generator.registerRecipes(helper);
        }
    }
}
