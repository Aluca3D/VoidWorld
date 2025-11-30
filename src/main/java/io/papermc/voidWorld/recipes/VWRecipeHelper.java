package io.papermc.voidWorld.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class VWRecipeHelper {

    private final JavaPlugin plugin;

    public VWRecipeHelper(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void genShapedRecipe(Material result, String keyName, String[] shape, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, keyName);
        ItemStack item = new ItemStack(result);
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape(shape);

        for (int i = 0; i < ingredients.length; i += 2) {
            char c = (char) ingredients[i];
            Material mat = (Material) ingredients[i + 1];
            recipe.setIngredient(c, mat);
        }

        plugin.getServer().addRecipe(recipe);
    }

    public void genShapelessRecipe(Material result, String keyName, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, keyName);
        ItemStack item = ItemStack.of(Material.DIAMOND_SWORD);

        ShapelessRecipe recipe = new ShapelessRecipe(key, item);

        for (int i = 0; i < ingredients.length; i += 2) {
            char c = (char) ingredients[i];
            Material material = (Material) ingredients[i + 1];
            recipe.addIngredient(c, material);
        }

        plugin.getServer().addRecipe(recipe);
    }

    //TODO: add
    // - BlastingRecipe
    // - CampfireRecipe
    // - FurnaceRecipe
    // - MerchantRecipe?
    // - SmithingRecipe?
    // - SmokingRecipe
    // - StonecuttingRecipe

}
