package io.papermc.voidWorld.recipes.recipes;

import io.papermc.voidWorld.recipes.VWRecipeHelper;
import io.papermc.voidWorld.recipes.VWRecipeInterface;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

public class ShapedRecipesGenerator implements VWRecipeInterface {
    @Override
    public void registerRecipes(VWRecipeHelper recipeHelper) {

        recipeHelper.genShapedRecipe(
                "oak_sapling_from_dirt_and_bone_meal",
                Material.OAK_SAPLING, 1,
                new String[]{"DD", "BD"},
                'D', Material.DIRT,
                'B', Material.BONE_MEAL
        );

        recipeHelper.genShapedRecipe(
                "stick_from_arrows",
                Material.STICK, 1,
                new String[]{"S ", " S"},
                'S', new RecipeChoice.MaterialChoice(
                        Material.ARROW,
                        Material.SPECTRAL_ARROW,
                        Material.TIPPED_ARROW
                )
        );

        recipeHelper.genShapedRecipe(
                "ice_from_snow_blocks",
                Material.ICE, 1,
                new String[]{"SS", "SS"},
                'S', Material.SNOW_BLOCK
        );

        recipeHelper.genShapedRecipe(
                "powder_snow_bucket_from_snowballs",
                Material.POWDER_SNOW_BUCKET, 1,
                new String[]{"SSS", "SSS", " B "},
                'S', Material.SNOWBALL,
                'B', Material.BUCKET
        );

        recipeHelper.genShapedRecipe(
                "tuff_from_blackstone_and_bonemeal",
                Material.TUFF, 1,
                new String[]{" B ", "BSB", " B "},
                'S', Material.BLACKSTONE,
                'B', Material.BONE_MEAL
        );

        recipeHelper.genShapedRecipe(
                "calcite_from_diorite_and_bonemeal",
                Material.CALCITE, 1,
                new String[]{" B ", "BSB", " B "},
                'S', Material.DIORITE,
                'B', Material.BONE_MEAL
        );

        recipeHelper.genShapedRecipe(
                "amethyst_shard_from_mixture",
                Material.AMETHYST_SHARD, 4,
                new String[]{"GLG", "BWB", "GLG"},
                'G', Material.GUNPOWDER,
                'L', Material.GLOWSTONE_DUST,
                'B', Material.BONE_MEAL,
                'W', Material.WATER_BUCKET
        );

        recipeHelper.genShapedRecipe(
                "crying_obsidian_from_obsidian_and_amethyst",
                Material.CRYING_OBSIDIAN, 1,
                new String[]{" B ", "BSB", " B "},
                'S', Material.OBSIDIAN,
                'B', Material.AMETHYST_SHARD
        );

        recipeHelper.genShapedRecipe(
                "echo_shard_from_amethyst_and_sculk",
                Material.ECHO_SHARD, 1,
                new String[]{" B ", "BSB", " B "},
                'S', Material.AMETHYST_SHARD,
                'B', Material.SCULK_VEIN
        );

        recipeHelper.genShapedRecipe(
                "crimson_nylium_from_wart_and_netherrack",
                Material.CRIMSON_NYLIUM, 3,
                new String[]{"WWW", "WWW", "NNN"},
                'W', Material.NETHER_WART,
                'N', Material.NETHERRACK
        );

        recipeHelper.genShapedRecipe(
                "warped_wart_block_from_glow_ink",
                Material.WARPED_WART_BLOCK, 1,
                new String[]{" B ", "BSB", " B "},
                'S', Material.NETHER_WART_BLOCK,
                'B', Material.GLOW_INK_SAC
        );

        recipeHelper.genShapedRecipe(
                "warped_nylium_from_warped_wart",
                Material.WARPED_NYLIUM, 2,
                new String[]{"WW", "NN"},
                'W', Material.WARPED_WART_BLOCK,
                'N', Material.NETHERRACK
        );

        recipeHelper.genShapedRecipe(
                "sculk_catalyst_from_soul_sand_and_endstone",
                Material.SCULK_CATALYST, 1,
                new String[]{"SWS", "SSS", "EEE"},
                'S', Material.SOUL_SAND,
                'W', Material.WARPED_WART_BLOCK,
                'E', Material.END_STONE
        );

        recipeHelper.genShapedRecipe(
                "mycelium_from_mushroom_mix",
                Material.MYCELIUM, 2,
                new String[]{"BRB", "RBR", " D "},
                'B', Material.BROWN_MUSHROOM,
                'R', Material.RED_MUSHROOM,
                'D', Material.DIRT
        );

        recipeHelper.genShapedRecipe(
                "reinforced_deepslate_from_components",
                Material.REINFORCED_DEEPSLATE, 1,
                new String[]{"SCS", "COC", "SCS"},
                'S', Material.END_STONE_BRICK_STAIRS,
                'C', Material.DEEPSLATE_TILES,
                'O', Material.OBSIDIAN
        );

        recipeHelper.genShapedRecipe(
                "enchanted_golden_apple_from_gold_blocks",
                Material.ENCHANTED_GOLDEN_APPLE, 1,
                new String[]{"GGG", "GAG", "GGG"},
                'G', Material.GOLD_BLOCK,
                'A', Material.APPLE
        );
    }
}
