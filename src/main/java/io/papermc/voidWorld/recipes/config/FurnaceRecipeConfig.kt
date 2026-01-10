package io.papermc.voidWorld.recipes.config

import io.papermc.voidWorld.recipes.DIngredientEntry
import io.papermc.voidWorld.recipes.IRecipe
import io.papermc.voidWorld.recipes.RecipeGenerator
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.Tag
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice
import org.spongepowered.configurate.ConfigurationNode

open class FurnaceRecipeConfig(
  val recipeGen: RecipeGenerator,
  val root: ConfigurationNode,
) : IRecipe {
  protected open fun genRecipe(
    id: String,
    result: Material,
    ingredient: DIngredientEntry,
    experience: Float,
    cookingTime: Int,
  ) {
    val key = NamespacedKey(recipeGen.plugin, "${id.lowercase()}_furnace")
    val itemResult = ItemStack(result)

    val recipe = FurnaceRecipe(key, itemResult, ingredient.choice, experience, cookingTime)
    recipeGen.addRecipe(id, key, recipe)
  }

  override fun loadRecipes() {
    for ((recipeId, recipeNode) in root.childrenMap()) {
      val id = recipeId.toString()

      val resultName =
        recipeNode.node("result").string
          ?: throw IllegalArgumentException("Recipe $id missing result")
      val result = Material.valueOf(resultName)

      val inputNode = recipeNode.node("input")

      val inputChoice: RecipeChoice =
        when {
          inputNode.isList -> {
            val materials =
              inputNode.childrenList().map {
                Material.valueOf(it.string!!)
              }
            RecipeChoice.MaterialChoice(materials)
          }

          inputNode.string!!.startsWith("_") -> {
            val tagName = inputNode.string!!.substring(1).lowercase()
            val tagKey = NamespacedKey.minecraft(tagName)
            val tag =
              Bukkit.getTag(Tag.REGISTRY_ITEMS, tagKey, Material::class.java)
                ?: throw IllegalArgumentException("Unknown tag: $tagName")
            RecipeChoice.MaterialChoice(tag)
          }

          else -> {
            val material = Material.valueOf(inputNode.string!!.uppercase())
            RecipeChoice.MaterialChoice(material)
          }
        }

      val ingredientEntry =
        DIngredientEntry(
          amount = 1,
          choice = inputChoice,
        )

      val resultXP = recipeNode.node("xp").float
      val resultCookingTime = recipeNode.node("cookingTime").int

      genRecipe(id, result, ingredientEntry, resultXP, resultCookingTime)
    }
  }
}
