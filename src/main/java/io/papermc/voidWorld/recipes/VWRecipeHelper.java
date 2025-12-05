package io.papermc.voidWorld.recipes;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class VWRecipeHelper {

    private final JavaPlugin plugin;

    private final Map<String, NamespacedKey> recipeKeys = new HashMap<>();

    public VWRecipeHelper(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private void addRecipeKey(String id, NamespacedKey key) {
        if (recipeKeys.containsKey(id)) {
            plugin.getLogger().warning(
                    "Duplicate recipe id detected: " + id +
                            " — previous recipe will be overwritten!"
            );
        }
        recipeKeys.put(id, key);
    }

    public NamespacedKey getRecipeKey(String id) {
        return recipeKeys.get(id);
    }

    public boolean hasUnlockedRecipe(Player player, String id) {
        NamespacedKey key = getRecipeKey(id);
        return key != null && player.hasDiscoveredRecipe(key);
    }

    public boolean unlockRecipeForPlayer(Player player, String id) {
        NamespacedKey key = getRecipeKey(id);

        if (key == null) {
            plugin.getLogger().warning("No recipe found with id: " + id);
            return false;
        }

        return player.discoverRecipe(key);
    }

    public void genShapedRecipe(String id, Material result, int amount, String[] shape, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_shaped");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result, amount);
        ShapedRecipe recipe = new ShapedRecipe(key, itemResult);
        recipe.shape(shape);
        for (int i = 0; i < ingredients.length; i += 2) {
            char keyChar = (char) ingredients[i];
            Object ingredient = ingredients[i + 1];

            if (ingredient instanceof Material mat) {
                recipe.setIngredient(keyChar, mat);

            } else if (ingredient instanceof RecipeChoice choice) {
                recipe.setIngredient(keyChar, choice);

            } else {
                throw new IllegalArgumentException(
                        "Invalid ingredient type: " + ingredient.getClass().getName()
                );
            }

        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genShapelessRecipe(String id, Material result, int amount, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_shapeless");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result, amount);
        ShapelessRecipe recipe = new ShapelessRecipe(key, itemResult);
        for (int i = 0; i < ingredients.length; i += 2) {
            int ingredientAmount = (int) ingredients[i];
            Object ingredient = ingredients[i + 1];

            if (ingredient instanceof Material mat) {
                recipe.addIngredient(ingredientAmount, mat);

            } else if (ingredient instanceof RecipeChoice choice) {
                for (int j = 0; j < ingredientAmount; j++) {
                    recipe.addIngredient(choice);
                }

            } else {
                throw new IllegalArgumentException(
                        "Invalid ingredient type: " + ingredient.getClass().getName()
                );
            }
        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genFurnaceRecipe(String id, Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_furnace");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result);
        FurnaceRecipe recipe;
        if (source instanceof Material mat) {
            recipe = new FurnaceRecipe(key, itemResult, mat, experience, cookingTime);

        } else if (source instanceof RecipeChoice choice) {
            recipe = new FurnaceRecipe(key, itemResult, choice, experience, cookingTime);

        } else {
            throw new IllegalArgumentException(
                    "Invalid ingredient type: " + source.getClass().getName()
            );
        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genBlastingRecipe(String id, Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_blasting");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result);

        BlastingRecipe recipe;

        if (source instanceof Material mat) {
            recipe = new BlastingRecipe(key, itemResult, mat, experience, cookingTime);

        } else if (source instanceof RecipeChoice choice) {
            recipe = new BlastingRecipe(key, itemResult, choice, experience, cookingTime);

        } else {
            throw new IllegalArgumentException(
                    "Invalid ingredient type: " + source.getClass().getName()
            );
        }

        plugin.getServer().addRecipe(recipe);
    }

    public void genSmokingRecipe(String id, Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_smoking");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result);
        SmokingRecipe recipe;
        if (source instanceof Material mat) {
            recipe = new SmokingRecipe(key, itemResult, mat, experience, cookingTime);

        } else if (source instanceof RecipeChoice choice) {
            recipe = new SmokingRecipe(key, itemResult, choice, experience, cookingTime);

        } else {
            throw new IllegalArgumentException(
                    "Invalid ingredient type: " + source.getClass().getName()
            );
        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genCampfireRecipe(String id, Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_campfire");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result);
        CampfireRecipe recipe;
        if (source instanceof Material mat) {
            recipe = new CampfireRecipe(key, itemResult, mat, experience, cookingTime);

        } else if (source instanceof RecipeChoice choice) {
            recipe = new CampfireRecipe(key, itemResult, choice, experience, cookingTime);

        } else {
            throw new IllegalArgumentException(
                    "Invalid ingredient type: " + source.getClass().getName()
            );
        }
        plugin.getServer().addRecipe(recipe);
    }

    public void genStonecuttingRecipe(String id, Material result, int amount, Object source) {
        NamespacedKey key = new NamespacedKey(plugin, id.toLowerCase() + "_stonecutting");

        addRecipeKey(id, key);

        ItemStack itemResult = new ItemStack(result, amount);
        StonecuttingRecipe recipe;
        if (source instanceof Material mat) {
            recipe = new StonecuttingRecipe(key, itemResult, mat);

        } else if (source instanceof RecipeChoice choice) {
            recipe = new StonecuttingRecipe(key, itemResult, choice);

        } else {
            throw new IllegalArgumentException(
                    "Invalid ingredient type: " + source.getClass().getName()
            );
        }
        plugin.getServer().addRecipe(recipe);
    }
}
