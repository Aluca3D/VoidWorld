package io.papermc.voidWorld.buildStructureDetection.structure;

import io.papermc.voidWorld.buildStructureDetection.BlockMatcher;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.EndPortalFrame;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.List;

public class EndPortalDetection implements Listener {
    private static final Material cryingObsidian = Material.CRYING_OBSIDIAN;
    private static final Material obsidian = Material.OBSIDIAN;
    private static final Material endStoneBricks = Material.END_STONE_BRICKS;
    private static final Material endPortalFrame = Material.END_PORTAL_FRAME;
    private static final Material endPortal = Material.END_PORTAL;
    private static final Material air = Material.AIR;
    private static final Material[] allCopperVariants = {
            Material.COPPER_BLOCK,
            Material.EXPOSED_COPPER,
            Material.WEATHERED_COPPER,
            Material.OXIDIZED_COPPER,
            Material.WAXED_COPPER_BLOCK,
            Material.WAXED_EXPOSED_COPPER,
            Material.WAXED_WEATHERED_COPPER,
            Material.WAXED_OXIDIZED_COPPER
    };

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        LightningStrike lightning = event.getLightning();
        Block struckBlock = lightning.getLocation().getBlock();

        if (struckBlock.getType() == Material.LIGHTNING_ROD) {
            tryBuildEndPortal(struckBlock);
            tryDestroyEndPortal(struckBlock);
        }
    }

    private static final List<BlockMatcher> PORTAL_BUILD_PATTERN = List.of(

            new BlockMatcher(2, 1, endStoneBricks), new BlockMatcher(2, 0, endStoneBricks), new BlockMatcher(2, -1, endStoneBricks),
            new BlockMatcher(1, 2, endStoneBricks), new BlockMatcher(1, 1, cryingObsidian), new BlockMatcher(1, 0, cryingObsidian),
            new BlockMatcher(1, -1, cryingObsidian), new BlockMatcher(1, -2, endStoneBricks),
            new BlockMatcher(0, 2, endStoneBricks), new BlockMatcher(0, 1, cryingObsidian), new BlockMatcher(0, 0, cryingObsidian),
            new BlockMatcher(0, -1, cryingObsidian), new BlockMatcher(0, -2, endStoneBricks),
            new BlockMatcher(-1, 2, endStoneBricks), new BlockMatcher(-1, 1, cryingObsidian), new BlockMatcher(-1, 0, cryingObsidian),
            new BlockMatcher(-1, -1, cryingObsidian), new BlockMatcher(-1, -2, endStoneBricks),
            new BlockMatcher(-2, 1, endStoneBricks), new BlockMatcher(-2, 0, endStoneBricks), new BlockMatcher(-2, -1, endStoneBricks)
    );

    private static final List<BlockMatcher[]> PORTAL_DESTROY_PATTERN = List.of(

            new BlockMatcher[]{
                    new BlockMatcher(4, 4, allCopperVariants), new BlockMatcher(4, 3, endPortalFrame), new BlockMatcher(4, 2, endPortalFrame), new BlockMatcher(4, 1, endPortalFrame), new BlockMatcher(4, 0, allCopperVariants),
                    new BlockMatcher(3, 4, endPortalFrame), new BlockMatcher(3, 3, air), new BlockMatcher(3, 2, air), new BlockMatcher(3, 1, air), new BlockMatcher(3, 0, endPortalFrame),
                    new BlockMatcher(2, 4, endPortalFrame), new BlockMatcher(2, 3, air), new BlockMatcher(2, 2, air), new BlockMatcher(2, 1, air), new BlockMatcher(2, 0, endPortalFrame),
                    new BlockMatcher(1, 4, endPortalFrame), new BlockMatcher(1, 3, air), new BlockMatcher(1, 2, air), new BlockMatcher(1, 1, air), new BlockMatcher(1, 0, endPortalFrame),
                    new BlockMatcher(0, 4, allCopperVariants), new BlockMatcher(0, 3, endPortalFrame), new BlockMatcher(0, 2, endPortalFrame), new BlockMatcher(0, 1, endPortalFrame), new BlockMatcher(0, 0, allCopperVariants),
            },

            new BlockMatcher[]{
                    new BlockMatcher(4, 0, allCopperVariants), new BlockMatcher(4, -1, endPortalFrame), new BlockMatcher(4, -2, endPortalFrame), new BlockMatcher(4, -3, endPortalFrame), new BlockMatcher(4, -4, allCopperVariants),
                    new BlockMatcher(3, 0, endPortalFrame), new BlockMatcher(3, -1, air), new BlockMatcher(3, -2, air), new BlockMatcher(3, -3, air), new BlockMatcher(3, -4, endPortalFrame),
                    new BlockMatcher(2, 0, endPortalFrame), new BlockMatcher(2, -1, air), new BlockMatcher(2, -2, air), new BlockMatcher(2, -3, air), new BlockMatcher(2, -4, endPortalFrame),
                    new BlockMatcher(1, 0, endPortalFrame), new BlockMatcher(1, -1, air), new BlockMatcher(1, -2, air), new BlockMatcher(1, -3, air), new BlockMatcher(1, -4, endPortalFrame),
                    new BlockMatcher(0, 0, allCopperVariants), new BlockMatcher(0, -1, endPortalFrame), new BlockMatcher(0, -2, endPortalFrame), new BlockMatcher(0, -3, endPortalFrame), new BlockMatcher(0, -4, allCopperVariants),
            },

            new BlockMatcher[]{
                    new BlockMatcher(0, 4, allCopperVariants), new BlockMatcher(0, 3, endPortalFrame), new BlockMatcher(0, 2, endPortalFrame), new BlockMatcher(0, 1, endPortalFrame), new BlockMatcher(0, 0, allCopperVariants),
                    new BlockMatcher(-1, 4, endPortalFrame), new BlockMatcher(-1, 3, air), new BlockMatcher(-1, 2, air), new BlockMatcher(-1, 1, air), new BlockMatcher(-1, 0, endPortalFrame),
                    new BlockMatcher(-2, 4, endPortalFrame), new BlockMatcher(-2, 3, air), new BlockMatcher(-2, 2, air), new BlockMatcher(-2, 1, air), new BlockMatcher(-2, 0, endPortalFrame),
                    new BlockMatcher(-3, 4, endPortalFrame), new BlockMatcher(-3, 3, air), new BlockMatcher(-3, 2, air), new BlockMatcher(-3, 1, air), new BlockMatcher(-3, 0, endPortalFrame),
                    new BlockMatcher(-4, 4, allCopperVariants), new BlockMatcher(-4, 3, endPortalFrame), new BlockMatcher(-4, 2, endPortalFrame), new BlockMatcher(-4, 1, endPortalFrame), new BlockMatcher(-4, 0, allCopperVariants),
            },

            new BlockMatcher[]{
                    new BlockMatcher(0, 0, allCopperVariants), new BlockMatcher(0, -1, endPortalFrame), new BlockMatcher(0, -2, endPortalFrame), new BlockMatcher(0, -3, endPortalFrame), new BlockMatcher(0, -4, allCopperVariants),
                    new BlockMatcher(-1, 0, endPortalFrame), new BlockMatcher(-1, -1, air), new BlockMatcher(-1, -2, air), new BlockMatcher(-1, -3, air), new BlockMatcher(-1, -4, endPortalFrame),
                    new BlockMatcher(-2, 0, endPortalFrame), new BlockMatcher(-2, -1, air), new BlockMatcher(-2, -2, air), new BlockMatcher(-2, -3, air), new BlockMatcher(-2, -4, endPortalFrame),
                    new BlockMatcher(-3, 0, endPortalFrame), new BlockMatcher(-3, -1, air), new BlockMatcher(-3, -2, air), new BlockMatcher(-3, -3, air), new BlockMatcher(-3, -4, endPortalFrame),
                    new BlockMatcher(-4, 0, allCopperVariants), new BlockMatcher(-4, -1, endPortalFrame), new BlockMatcher(-4, -2, endPortalFrame), new BlockMatcher(-4, -3, endPortalFrame), new BlockMatcher(-4, -4, allCopperVariants),
            }
    );

    private void tryBuildEndPortal(Block startBlock) {
        var world = startBlock.getWorld();
        int x = startBlock.getX();
        int y = startBlock.getY() - 1;
        int z = startBlock.getZ();

        for (BlockMatcher blockMatcher : PORTAL_BUILD_PATTERN) {
            if (world.getBlockAt(x + blockMatcher.dx(), y, z + blockMatcher.dz()).getType() != blockMatcher.types()[0]) {
                return;
            }
        }

        for (BlockMatcher blockMatcher : PORTAL_BUILD_PATTERN) {
            Block current = world.getBlockAt(x + blockMatcher.dx(), y, z + blockMatcher.dz());
            Material resultType = convertMaterial(blockMatcher.types()[0]);

            current.setType(resultType);

            if (resultType == endPortalFrame) {
                orientPortalFrame(current, blockMatcher.dx(), blockMatcher.dz());
            }
        }
    }

    private void tryDestroyEndPortal(Block startBlock) {
        int x = startBlock.getX();
        int y = startBlock.getY() - 1;
        int z = startBlock.getZ();
        var world = startBlock.getWorld();

        for (BlockMatcher[] blockMatchers : PORTAL_DESTROY_PATTERN) {

            boolean valid = true;

            for (BlockMatcher blockMatcher : blockMatchers) {
                Material current = world.getBlockAt(x + blockMatcher.dx(), y, z + blockMatcher.dz()).getType();

                if (!blockMatcher.matches(current)) {
                    valid = false;
                    break;
                }
            }

            if (!valid) continue;

            for (BlockMatcher blockMatcher : blockMatchers) {
                Block block = world.getBlockAt(x + blockMatcher.dx(), y, z + blockMatcher.dz());

                if (!blockMatcher.matches(air)) {
                    block.setType(convertMaterial(block.getType()));
                }

                if (block.getType() == endPortal) {
                    block.setType(air);
                }
            }

            return;
        }
    }

    private Material convertMaterial(Material input) {
        if (input == endStoneBricks) return endPortalFrame;
        if (input == endPortalFrame) return endStoneBricks;
        if (input == cryingObsidian) return obsidian;
        return input;
    }

    private void orientPortalFrame(Block block, int dx, int dz) {
        BlockData data = block.getBlockData();
        if (!(data instanceof EndPortalFrame frame)) return;

        frame.setFacing(getDirectionToCenter(dx, dz));
        block.setBlockData(frame);
    }

    private BlockFace getDirectionToCenter(int dx, int dz) {
        if (dz == 2) return BlockFace.NORTH;
        else if (dz == -2) return BlockFace.SOUTH;
        else if (dx == 2) return BlockFace.WEST;
        else if (dx == -2) return BlockFace.EAST;
        return BlockFace.SELF;
    }
}
