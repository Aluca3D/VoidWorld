package io.papermc.voidWorld.recipes.config

import io.papermc.voidWorld.recipes.DIngredientEntry
import io.papermc.voidWorld.recipes.RecipeGenerator
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.SmokingRecipe
import org.spongepowered.configurate.ConfigurationNode

class SmokingRecipeConfig(
  recipeGen: RecipeGenerator,
  root: ConfigurationNode,
) : FurnaceRecipeConfig(recipeGen, root) {
  override fun genRecipe(
    id: String,
    result: Material,
    ingredient: DIngredientEntry,
    experience: Float,
    cookingTime: Int,
  ) {
    val key = NamespacedKey(recipeGen.plugin, "${id.lowercase()}_smoking")
    val itemResult = ItemStack(result)

    val recipe = SmokingRecipe(key, itemResult, ingredient.choice, experience, cookingTime)
    recipeGen.addRecipe(id, key, recipe)
  }
}
