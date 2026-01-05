package io.papermc.voidWorld.recipes

import org.bukkit.inventory.RecipeChoice

data class IngredientEntry(
    val amount: Int,
    val choice: RecipeChoice
)
