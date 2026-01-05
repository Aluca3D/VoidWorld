package io.papermc.voidWorld.recipes.config

import io.papermc.voidWorld.recipes.IRecipe
import io.papermc.voidWorld.recipes.IngredientEntry
import io.papermc.voidWorld.recipes.RecipeGenerator
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapelessRecipe
import org.spongepowered.configurate.ConfigurationNode

class ShapelessRecipeConfig(val recipeGen: RecipeGenerator, val root: ConfigurationNode) : IRecipe {
    private fun genRecipe(
        id: String,
        result: Material,
        amount: Int,
        ingredients: List<IngredientEntry>
    ) {
        val key = NamespacedKey(recipeGen.plugin, "${id.lowercase()}_shapeless")

        val itemResult = ItemStack(result, amount)
        val recipe = ShapelessRecipe(key, itemResult)

        for (ingredient in ingredients) {
            repeat(ingredient.amount) {
                recipe.addIngredient(ingredient.choice)
            }
        }
        recipeGen.addRecipe(id, key, recipe)
    }

    override fun loadRecipes() {
        for ((recipeId, recipeNode) in root.childrenMap()) {
            val id = recipeId.toString()

            val resultName = recipeNode.node("result").string
                ?: throw IllegalArgumentException("Recipe $id missing result")

            val result = Material.valueOf(resultName)
            val resultAmount = recipeNode.node("amount").int

            val ingredients = mutableListOf<IngredientEntry>()
            val ingredientNode = recipeNode.node("ingredients")

            for ((keyNode, valueNode) in ingredientNode.childrenMap()) {
                val key = keyNode.toString()

                if (key.startsWith("&") || key.startsWith("_")) {

                    val amount = when {
                        key.startsWith("&") -> key.drop(1).toIntOrNull() ?: 1
                        else -> valueNode.int
                    }

                    val recipeChoice = when {
                        key.startsWith("_") -> {
                            val tagName = key.drop(1).lowercase()
                            val tagKey = NamespacedKey.fromString(tagName)!!
                            val tag = Bukkit.getTag(Tag.REGISTRY_ITEMS, tagKey, Material::class.java)
                                ?: throw IllegalArgumentException("Unknown tag: $tagName")
                            RecipeChoice.MaterialChoice(tag)
                        }

                        valueNode.isList -> {
                            val materials = valueNode.childrenList().map { Material.valueOf(it.string!!) }
                            RecipeChoice.MaterialChoice(materials)
                        }

                        else -> {
                            val material = Material.valueOf(valueNode.string!!.uppercase())
                            RecipeChoice.MaterialChoice(material)
                        }
                    }

                    ingredients.add(IngredientEntry(amount, recipeChoice))

                } else {
                    val material = Material.valueOf(key)
                    val amount = valueNode.int

                    ingredients.add(
                        IngredientEntry(
                            amount = amount,
                            choice = RecipeChoice.MaterialChoice(material)
                        )
                    )
                }
            }

            genRecipe(id, result, resultAmount, ingredients)
        }
    }
}
