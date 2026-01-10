package io.papermc.voidWorld.recipes.config

import io.papermc.voidWorld.recipes.DIngredientEntry
import io.papermc.voidWorld.recipes.IRecipe
import io.papermc.voidWorld.recipes.RecipeGenerator
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.bukkit.inventory.ShapedRecipe
import org.spongepowered.configurate.ConfigurationNode

class ShapedRecipeConfig(
  val recipeGen: RecipeGenerator,
  val root: ConfigurationNode,
) : IRecipe {
  private fun genRecipe(
    id: String,
    result: Material,
    amount: Int,
    shape: List<String>,
    ingredients: Map<Char, DIngredientEntry>,
  ) {
    val key = NamespacedKey(recipeGen.plugin, "${id.lowercase()}_shaped")

    val itemResult = ItemStack(result, amount)
    val recipe = ShapedRecipe(key, itemResult)

    recipe.shape(*shape.toTypedArray())

    for ((char, ingredient) in ingredients) {
      recipe.setIngredient(char, ingredient.choice)
    }

    recipeGen.addRecipe(id, key, recipe)
  }

  override fun loadRecipes() {
    for ((recipeId, recipeNode) in root.childrenMap()) {
      val id = recipeId.toString()

      val resultName =
        recipeNode.node("result").string
          ?: throw IllegalArgumentException("Recipe $id missing result")

      val result = Material.valueOf(resultName)
      val resultAmount = recipeNode.node("amount").int

      val pattern = recipeNode.node("pattern").childrenList().mapNotNull { it.string }

      val ingredientMap = mutableMapOf<Char, DIngredientEntry>()
      val ingredientsNode = recipeNode.node("ingredients")

      for ((charKey, ingredientNode) in ingredientsNode.childrenMap()) {
        val keyChar = charKey.toString()[0]

        val ingredientEntry: DIngredientEntry =
          when {
            ingredientNode.isList -> {
              val materials =
                ingredientNode.childrenList().map {
                  Material.valueOf(it.string!!)
                }
              DIngredientEntry(1, RecipeChoice.MaterialChoice(materials))
            }

            ingredientNode.string!!.startsWith("_") -> {
              val tagName = ingredientNode.string!!.substring(1).lowercase()
              val tagKey = NamespacedKey.minecraft(tagName)
              val tag =
                Bukkit.getTag(Tag.REGISTRY_ITEMS, tagKey, Material::class.java)
                  ?: throw IllegalArgumentException("Unknown tag: $tagName")
              DIngredientEntry(1, RecipeChoice.MaterialChoice(tag))
            }

            else -> {
              val material = Material.valueOf(ingredientNode.string!!.uppercase())
              DIngredientEntry(1, RecipeChoice.MaterialChoice(material))
            }
          }

        ingredientMap[keyChar] = ingredientEntry
      }

      genRecipe(id, result, resultAmount, pattern, ingredientMap)
    }
  }
}
