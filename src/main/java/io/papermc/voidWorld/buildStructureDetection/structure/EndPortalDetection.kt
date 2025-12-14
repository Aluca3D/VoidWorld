package io.papermc.voidWorld.buildStructureDetection.structure

import io.papermc.voidWorld.buildStructureDetection.BlockMatcher
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.EndPortalFrame
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.weather.LightningStrikeEvent

class EndPortalDetection : Listener {

    companion object {
        private val cryingObsidian = Material.CRYING_OBSIDIAN
        private val obsidian = Material.OBSIDIAN
        private val endStoneBricks = Material.END_STONE_BRICKS
        private val endPortalFrame = Material.END_PORTAL_FRAME
        private val endPortal = Material.END_PORTAL
        private val air = Material.AIR

        private val allCopperVariants = setOf(
            Material.COPPER_BLOCK,
            Material.EXPOSED_COPPER,
            Material.WEATHERED_COPPER,
            Material.OXIDIZED_COPPER,
            Material.WAXED_COPPER_BLOCK,
            Material.WAXED_EXPOSED_COPPER,
            Material.WAXED_WEATHERED_COPPER,
            Material.WAXED_OXIDIZED_COPPER
        )

        private val lightningRodVariants = setOf(
            Material.LIGHTNING_ROD,
            Material.EXPOSED_LIGHTNING_ROD,
            Material.WEATHERED_LIGHTNING_ROD,
            Material.OXIDIZED_LIGHTNING_ROD,
            Material.WAXED_LIGHTNING_ROD,
            Material.WAXED_EXPOSED_LIGHTNING_ROD,
            Material.WAXED_WEATHERED_LIGHTNING_ROD,
            Material.WAXED_OXIDIZED_LIGHTNING_ROD
        )

        private val PORTAL_BUILD_PATTERN = listOf(
            BlockMatcher(2, 1, endStoneBricks),
            BlockMatcher(2, 0, endStoneBricks),
            BlockMatcher(2, -1, endStoneBricks),
            BlockMatcher(1, 2, endStoneBricks),
            BlockMatcher(1, 1, cryingObsidian),
            BlockMatcher(1, 0, cryingObsidian),
            BlockMatcher(1, -1, cryingObsidian),
            BlockMatcher(1, -2, endStoneBricks),
            BlockMatcher(0, 2, endStoneBricks),
            BlockMatcher(0, 1, cryingObsidian),
            BlockMatcher(0, 0, cryingObsidian),
            BlockMatcher(0, -1, cryingObsidian),
            BlockMatcher(0, -2, endStoneBricks),
            BlockMatcher(-1, 2, endStoneBricks),
            BlockMatcher(-1, 1, cryingObsidian),
            BlockMatcher(-1, 0, cryingObsidian),
            BlockMatcher(-1, -1, cryingObsidian),
            BlockMatcher(-1, -2, endStoneBricks),
            BlockMatcher(-2, 1, endStoneBricks),
            BlockMatcher(-2, 0, endStoneBricks),
            BlockMatcher(-2, -1, endStoneBricks)
        )

        private val PORTAL_DESTROY_PATTERN = listOf(
            arrayOf(
                BlockMatcher(4, 4, allCopperVariants),
                BlockMatcher(4, 3, endPortalFrame),
                BlockMatcher(4, 2, endPortalFrame),
                BlockMatcher(4, 1, endPortalFrame),
                BlockMatcher(4, 0, allCopperVariants),
                BlockMatcher(3, 4, endPortalFrame),
                BlockMatcher(3, 3, air),
                BlockMatcher(3, 2, air),
                BlockMatcher(3, 1, air),
                BlockMatcher(3, 0, endPortalFrame),
                BlockMatcher(2, 4, endPortalFrame),
                BlockMatcher(2, 3, air),
                BlockMatcher(2, 2, air),
                BlockMatcher(2, 1, air),
                BlockMatcher(2, 0, endPortalFrame),
                BlockMatcher(1, 4, endPortalFrame),
                BlockMatcher(1, 3, air),
                BlockMatcher(1, 2, air),
                BlockMatcher(1, 1, air),
                BlockMatcher(1, 0, endPortalFrame),
                BlockMatcher(0, 4, allCopperVariants),
                BlockMatcher(0, 3, endPortalFrame),
                BlockMatcher(0, 2, endPortalFrame),
                BlockMatcher(0, 1, endPortalFrame),
                BlockMatcher(0, 0, allCopperVariants)
            ),
            arrayOf(
                BlockMatcher(4, 0, allCopperVariants),
                BlockMatcher(4, -1, endPortalFrame),
                BlockMatcher(4, -2, endPortalFrame),
                BlockMatcher(4, -3, endPortalFrame),
                BlockMatcher(4, -4, allCopperVariants),
                BlockMatcher(3, 0, endPortalFrame),
                BlockMatcher(3, -1, air),
                BlockMatcher(3, -2, air),
                BlockMatcher(3, -3, air),
                BlockMatcher(3, -4, endPortalFrame),
                BlockMatcher(2, 0, endPortalFrame),
                BlockMatcher(2, -1, air),
                BlockMatcher(2, -2, air),
                BlockMatcher(2, -3, air),
                BlockMatcher(2, -4, endPortalFrame),
                BlockMatcher(1, 0, endPortalFrame),
                BlockMatcher(1, -1, air),
                BlockMatcher(1, -2, air),
                BlockMatcher(1, -3, air),
                BlockMatcher(1, -4, endPortalFrame),
                BlockMatcher(0, 0, allCopperVariants),
                BlockMatcher(0, -1, endPortalFrame),
                BlockMatcher(0, -2, endPortalFrame),
                BlockMatcher(0, -3, endPortalFrame),
                BlockMatcher(0, -4, allCopperVariants)
            ),
            arrayOf(
                BlockMatcher(0, 4, allCopperVariants),
                BlockMatcher(0, 3, endPortalFrame),
                BlockMatcher(0, 2, endPortalFrame),
                BlockMatcher(0, 1, endPortalFrame),
                BlockMatcher(0, 0, allCopperVariants),
                BlockMatcher(-1, 4, endPortalFrame),
                BlockMatcher(-1, 3, air),
                BlockMatcher(-1, 2, air),
                BlockMatcher(-1, 1, air),
                BlockMatcher(-1, 0, endPortalFrame),
                BlockMatcher(-2, 4, endPortalFrame),
                BlockMatcher(-2, 3, air),
                BlockMatcher(-2, 2, air),
                BlockMatcher(-2, 1, air),
                BlockMatcher(-2, 0, endPortalFrame),
                BlockMatcher(-3, 4, endPortalFrame),
                BlockMatcher(-3, 3, air),
                BlockMatcher(-3, 2, air),
                BlockMatcher(-3, 1, air),
                BlockMatcher(-3, 0, endPortalFrame),
                BlockMatcher(-4, 4, allCopperVariants),
                BlockMatcher(-4, 3, endPortalFrame),
                BlockMatcher(-4, 2, endPortalFrame),
                BlockMatcher(-4, 1, endPortalFrame),
                BlockMatcher(-4, 0, allCopperVariants)
            ),
            arrayOf(
                BlockMatcher(0, 0, allCopperVariants),
                BlockMatcher(0, -1, endPortalFrame),
                BlockMatcher(0, -2, endPortalFrame),
                BlockMatcher(0, -3, endPortalFrame),
                BlockMatcher(0, -4, allCopperVariants),
                BlockMatcher(-1, 0, endPortalFrame),
                BlockMatcher(-1, -1, air),
                BlockMatcher(-1, -2, air),
                BlockMatcher(-1, -3, air),
                BlockMatcher(-1, -4, endPortalFrame),
                BlockMatcher(-2, 0, endPortalFrame),
                BlockMatcher(-2, -1, air),
                BlockMatcher(-2, -2, air),
                BlockMatcher(-2, -3, air),
                BlockMatcher(-2, -4, endPortalFrame),
                BlockMatcher(-3, 0, endPortalFrame),
                BlockMatcher(-3, -1, air),
                BlockMatcher(-3, -2, air),
                BlockMatcher(-3, -3, air),
                BlockMatcher(-3, -4, endPortalFrame),
                BlockMatcher(-4, 0, allCopperVariants),
                BlockMatcher(-4, -1, endPortalFrame),
                BlockMatcher(-4, -2, endPortalFrame),
                BlockMatcher(-4, -3, endPortalFrame),
                BlockMatcher(-4, -4, allCopperVariants)
            )
        )
    }

    @EventHandler
    fun onLightningStrike(event: LightningStrikeEvent) {
        val block = event.lightning.location.block
        if (block.type in lightningRodVariants) {
            tryBuildEndPortal(block)
            tryDestroyEndPortal(block)
        }
    }

    private fun tryBuildEndPortal(startBlock: Block) {
        val world = startBlock.world
        val x = startBlock.x
        val y = startBlock.y - 1
        val z = startBlock.z

        if (PORTAL_BUILD_PATTERN.any {
                !it.matches(world.getBlockAt(x + it.dx, y, z + it.dz).type)
            }) return

        for (matcher in PORTAL_BUILD_PATTERN) {
            val block = world.getBlockAt(x + matcher.dx, y, z + matcher.dz)
            val result = convertMaterial(matcher.types.single())

            block.type = result

            if (result == endPortalFrame) {
                orientPortalFrame(block, matcher.dx, matcher.dz)
            }
        }
    }

    private fun tryDestroyEndPortal(startBlock: Block) {
        val world = startBlock.world
        val x = startBlock.x
        val y = startBlock.y - 1
        val z = startBlock.z

        for (pattern in PORTAL_DESTROY_PATTERN) {
            if (pattern.any {
                    val type = world.getBlockAt(x + it.dx, y, z + it.dz).type
                    !it.matches(air) && !it.matches(type)
                }) continue

            for (matcher in pattern) {
                val block = world.getBlockAt(x + matcher.dx, y, z + matcher.dz)
                if (!matcher.matches(air)) {
                    block.type = convertMaterial(block.type)
                }
                if (block.type == endPortal) {
                    block.type = air
                }
            }
            return
        }
    }

    private fun convertMaterial(input: Material): Material =
        when (input) {
            endStoneBricks -> endPortalFrame
            endPortalFrame -> endStoneBricks
            cryingObsidian -> obsidian
            else -> input
        }

    private fun orientPortalFrame(block: Block, dx: Int, dz: Int) {
        val frame = block.blockData as? EndPortalFrame ?: return
        frame.facing = getDirectionToCenter(dx, dz)
        block.blockData = frame
    }

    private fun getDirectionToCenter(dx: Int, dz: Int): BlockFace =
        when {
            dz == 2 -> BlockFace.NORTH
            dz == -2 -> BlockFace.SOUTH
            dx == 2 -> BlockFace.WEST
            dx == -2 -> BlockFace.EAST
            else -> BlockFace.SELF
        }
}
