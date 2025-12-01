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

    private String keyPart(Object source) {
        if (source instanceof Material material) {
            return material.name().toLowerCase();
        }
        if (source instanceof RecipeChoice.MaterialChoice materialChoice) {
            return materialChoice.getChoices().stream()
                    .map(material -> material.name().toLowerCase())
                    .sorted()
                    .reduce((stringA, stringB) -> stringA + "_" + stringB)
                    .orElse("material_choice");
        }
        return "custom_choice";
    }

    public void genShapedRecipe(Material result, int amount, String[] shape, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_shaped"
        );
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

    public void genShapelessRecipe(Material result, int amount, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_shapeless"
        );
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

    public void genShapedRecipe(String keySuffix, Material result, int amount, String[] shape, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + keySuffix + "_shaped"
        );
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

    public void genShapelessRecipe(String keySuffix, Material result, int amount, Object... ingredients) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + keySuffix + "_shapeless"
        );
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

    public void genFurnaceRecipe(Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_from_" + keyPart(source) + "_furnace"
        );
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

    public void genBlastingRecipe(Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_from_" + keyPart(source) + "_blasting"
        );
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

    public void genSmokingRecipe(Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_from_" + keyPart(source) + "_smoking"
        );
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

    public void genCampfireRecipe(Material result, Object source, float experience, int cookingTime) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_from_" + keyPart(source) + "_campfire"
        );
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

    public void genStonecuttingRecipe(Material result, int amount, Object source) {
        NamespacedKey key = new NamespacedKey(
                plugin, result.name().toLowerCase() + "_from_" + keyPart(source) + "_stonecutting"
        );
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
