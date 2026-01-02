package io.papermc.voidWorld.recipes

import org.bukkit.NamespacedKey
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.plugin.java.JavaPlugin

class RecipeGenerator(val plugin: JavaPlugin) {

    private val recipeKeys: MutableMap<String?, NamespacedKey?> = HashMap()

    private fun addRecipeKey(id: String?, key: NamespacedKey?) {
        if (recipeKeys.containsKey(id)) {
            plugin.logger.warning(
                "Duplicate recipe id detected: $id — previous recipe will be overwritten!"
            )
        }
        plugin.logger.info { "Recipe added with key: $key" }
        recipeKeys[id] = key
    }

    fun getRecipeKey(id: String?): NamespacedKey? {
        return recipeKeys[id]
    }

    fun addRecipe(id: String?, key: NamespacedKey?, recipe: ShapedRecipe) {
        addRecipeKey(id, key)
        plugin.server.addRecipe(recipe);
    }

    // TODO:
    //  - Add Discover recipe function
    //   - make it Tag/team locket (only specific teams/tags can see/use the recipe)
}