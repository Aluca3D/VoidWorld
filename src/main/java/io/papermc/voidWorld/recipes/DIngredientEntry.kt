package io.papermc.voidWorld.recipes

import org.bukkit.inventory.RecipeChoice

data class DIngredientEntry(
    val amount: Int,
    val choice: RecipeChoice
)
