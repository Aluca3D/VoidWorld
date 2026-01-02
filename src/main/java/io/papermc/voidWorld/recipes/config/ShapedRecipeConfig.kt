package io.papermc.voidWorld.recipes.config

import io.papermc.voidWorld.recipes.RecipeGenerator
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.spongepowered.configurate.ConfigurationNode

class ShapedRecipeConfig(val recipeGen: RecipeGenerator, val root: ConfigurationNode) {

    private fun genShapedRecipe(
        id: String,
        result: Material,
        amount: Int,
        shape: List<String>,
        ingredients: Map<Char, Any>
    ) {
        val key = NamespacedKey(recipeGen.plugin, "${id.lowercase()}_shaped")

        val itemResult = ItemStack(result, amount)
        val recipe = ShapedRecipe(key, itemResult)

        recipe.shape(*shape.toTypedArray())

        for ((char, ingredient) in ingredients) {
            when (ingredient) {
                is Material -> recipe.setIngredient(char, ingredient)
                is RecipeChoice -> recipe.setIngredient(char, ingredient)
                else -> throw IllegalArgumentException(
                    "Invalid ingredient type for '$char': ${ingredient::class.qualifiedName}"
                )
            }
        }

        recipeGen.addRecipe(id, key, recipe)
    }

    fun loadRecipes() {
        for ((recipeId, recipeNode) in root.childrenMap()) {
            val id = recipeId.toString()

            val resultName = recipeNode.node("result").string
                ?: throw IllegalArgumentException("Recipe $id missing result")

            val result = Material.valueOf(resultName)
            val amount = recipeNode.node("amount").int // TODO: add default value of 1

            val pattern = recipeNode.node("pattern").childrenList().mapNotNull { it.string }

            val ingredientMap = mutableMapOf<Char, Any>()
            val ingredientsNode = recipeNode.node("ingredients")

            // TODO: Make helper function for easy reusability
            for ((charKey, ingredientNode) in ingredientsNode.childrenMap()) {
                val keyChar = charKey.toString()[0]

                val ingredient: Any = when {
                    ingredientNode.isList -> {
                        val materials = ingredientNode.childrenList().map {
                            Material.valueOf(it.string!!)
                        }
                        RecipeChoice.MaterialChoice(materials)
                    }

                    ingredientNode.string!!.startsWith("_") -> {
                        val tagName = ingredientNode.string!!.substring(1).lowercase()
                        val tagKey = NamespacedKey.minecraft(tagName)

                        val tag = Bukkit.getTag(
                            Tag.REGISTRY_ITEMS,
                            tagKey,
                            Material::class.java
                        ) ?: throw IllegalArgumentException("Unknown tag: $tagName")

                        RecipeChoice.MaterialChoice(tag)
                    }

                    else -> {
                        Material.valueOf(ingredientNode.string!!)
                    }
                }

                ingredientMap[keyChar] = ingredient
            }

            genShapedRecipe(id, result, amount, pattern, ingredientMap)
        }
    }

}