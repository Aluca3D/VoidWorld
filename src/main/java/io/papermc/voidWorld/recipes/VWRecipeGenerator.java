package io.papermc.voidWorld.recipes;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class VWRecipeGenerator {
    private final JavaPlugin plugin;

    public VWRecipeGenerator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void registerRecipes() {
        VWRecipeHelper recipeHelper = new VWRecipeHelper(plugin);

        recipeHelper.genShapedRecipe(
                Material.OAK_SAPLING,
                new String[]{"DB", "BD"},
                'D', Material.DIRT,
                'B', Material.BONE_MEAL
        );

        recipeHelper.genFurnaceRecipe(
                Material.IRON_NUGGET,
                Material.GRAVEL,
                0.4f,
                100
        );
    }
}
