package io.papermc.voidWorld.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

public class VWRecipeHelper {

    private final JavaPlugin plugin;

    public VWRecipeHelper(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void genShapedRecipe(Material result, String[] shape, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, result.toString() + "_shaped");
        ItemStack itemResult = new ItemStack(result);
        ShapedRecipe recipe = new ShapedRecipe(key, itemResult);
        recipe.shape(shape);
        for (int i = 0; i < ingredients.length; i += 2) {
            char c = (char) ingredients[i];
            Material mat = (Material) ingredients[i + 1];
            recipe.setIngredient(c, mat);
        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genShapelessRecipe(Material result, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, result.toString() + "_shapeless");
        ItemStack itemResult = new ItemStack(result);
        ShapelessRecipe recipe = new ShapelessRecipe(key, itemResult);
        for (int i = 0; i < ingredients.length; i += 2) {
            char c = (char) ingredients[i];
            Material material = (Material) ingredients[i + 1];
            recipe.addIngredient(c, material);
        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genFurnaceRecipe(Material result, Material source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.toString() + "_from_" + source.toString() + "_furnace"
        );
        ItemStack itemResult = new ItemStack(result);
        FurnaceRecipe recipe = new FurnaceRecipe(key, itemResult, source, experience, cookingTime);
        plugin.getServer().addRecipe(recipe);
    }

    public void genBlastingRecipe(Material result, Material source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.toString() + "_from_" + source.toString() + "_blasting"
        );
        ItemStack itemResult = new ItemStack(result);
        BlastingRecipe recipe = new BlastingRecipe(key, itemResult, source, experience, cookingTime);
        plugin.getServer().addRecipe(recipe);
    }

    public void genSmokingRecipe(Material result, Material source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.toString() + "_from_" + source.toString() + "_smoking"
        );
        ItemStack itemResult = new ItemStack(result);
        SmokingRecipe recipe = new SmokingRecipe(key, itemResult, source, experience, cookingTime);
        plugin.getServer().addRecipe(recipe);
    }

    public void genCampfireRecipe(Material result, Material source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.toString() + "_from_" + source.toString() + "_campfire"
        );
        ItemStack itemResult = new ItemStack(result);
        CampfireRecipe recipe = new CampfireRecipe(key, itemResult, source, experience, cookingTime);
        plugin.getServer().addRecipe(recipe);
    }

    public void genStonecuttingRecipe(Material result, Material source) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.toString() + "_from_" + source.toString() + "_stonecutting"
        );
        ItemStack itemResult = new ItemStack(result);
        StonecuttingRecipe recipe = new StonecuttingRecipe(key, itemResult, source);
        plugin.getServer().addRecipe(recipe);
    }
}
