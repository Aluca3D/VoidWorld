package io.papermc.voidWorld.recipes.config

import io.papermc.voidWorld.recipes.IngredientEntry
import io.papermc.voidWorld.recipes.RecipeGenerator
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.CampfireRecipe
import org.bukkit.inventory.ItemStack
import org.spongepowered.configurate.ConfigurationNode

class CampfireRecipeConfig(recipeGen: RecipeGenerator, root: ConfigurationNode) :
    FurnaceRecipeConfig(recipeGen, root) {

    override fun genRecipe(
        id: String,
        result: Material,
        ingredient: IngredientEntry,
        experience: Float,
        cookingTime: Int,
    ) {
        val key = NamespacedKey(recipeGen.plugin, "${id.lowercase()}_campfire")
        val itemResult = ItemStack(result)

        val recipe = CampfireRecipe(key, itemResult, ingredient.choice, experience, cookingTime)
        recipeGen.addRecipe(id, key, recipe)
    }
}