package io.papermc.voidWorld.buildStructureDetection;

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
    
    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        LightningStrike lightning = event.getLightning();
        Block struckBlock = lightning.getLocation().getBlock();

        if (struckBlock.getType() == Material.LIGHTNING_ROD) {
            tryBuildEndPortal(struckBlock);
            tryDestroyEndPortal(struckBlock);
        }
    }

    private record PatternBlock(int dx, int dz, Material type) {
    }

    private static final List<PatternBlock> PORTAL_BUILD_PATTERN = List.of(
            new PatternBlock(2, 1, endStoneBricks), new PatternBlock(2, 0, endStoneBricks), new PatternBlock(2, -1, endStoneBricks),
            new PatternBlock(1, 2, endStoneBricks), new PatternBlock(1, 1, cryingObsidian), new PatternBlock(1, 0, cryingObsidian),
            new PatternBlock(1, -1, cryingObsidian), new PatternBlock(1, -2, endStoneBricks),

            new PatternBlock(0, 2, endStoneBricks), new PatternBlock(0, 1, cryingObsidian), new PatternBlock(0, 0, cryingObsidian),
            new PatternBlock(0, -1, cryingObsidian), new PatternBlock(0, -2, endStoneBricks),

            new PatternBlock(-1, 2, endStoneBricks), new PatternBlock(-1, 1, cryingObsidian), new PatternBlock(-1, 0, cryingObsidian),
            new PatternBlock(-1, -1, cryingObsidian), new PatternBlock(-1, -2, endStoneBricks),

            new PatternBlock(-2, 1, endStoneBricks), new PatternBlock(-2, 0, endStoneBricks), new PatternBlock(-2, -1, endStoneBricks)
    );

    private void tryBuildEndPortal(Block startBlock) {
        var world = startBlock.getWorld();
        int x = startBlock.getX();
        int y = startBlock.getY() - 1;
        int z = startBlock.getZ();

        for (PatternBlock p : PORTAL_BUILD_PATTERN) {
            if (world.getBlockAt(x + p.dx(), y, z + p.dz()).getType() != p.type()) {
                return;
            }
        }

        for (PatternBlock p : PORTAL_BUILD_PATTERN) {
            Block current = world.getBlockAt(x + p.dx(), y, z + p.dz());
            Material resultType = convertMaterial(p.type());

            current.setType(resultType);

            if (resultType == endPortalFrame) {
                orientPortalFrame(current, p.dx(), p.dz());
            }
        }
    }

    private void tryDestroyEndPortal(Block startBlock) {
        int x = startBlock.getX();
        int y = startBlock.getY() - 1;
        int z = startBlock.getZ();
        var world = startBlock.getWorld();

        List<PatternBlock[]> blockSets = List.of(

                new PatternBlock[]{
                        new PatternBlock(4, 4, obsidian), new PatternBlock(4, 3, endPortalFrame), new PatternBlock(4, 2, endPortalFrame), new PatternBlock(4, 1, endPortalFrame), new PatternBlock(4, 0, obsidian),
                        new PatternBlock(3, 4, endPortalFrame), new PatternBlock(3, 3, air), new PatternBlock(3, 2, air), new PatternBlock(3, 1, air), new PatternBlock(3, 0, endPortalFrame),
                        new PatternBlock(2, 4, endPortalFrame), new PatternBlock(2, 3, air), new PatternBlock(2, 2, air), new PatternBlock(2, 1, air), new PatternBlock(2, 0, endPortalFrame),
                        new PatternBlock(1, 4, endPortalFrame), new PatternBlock(1, 3, air), new PatternBlock(1, 2, air), new PatternBlock(1, 1, air), new PatternBlock(1, 0, endPortalFrame),
                        new PatternBlock(0, 4, obsidian), new PatternBlock(0, 3, endPortalFrame), new PatternBlock(0, 2, endPortalFrame), new PatternBlock(0, 1, endPortalFrame), new PatternBlock(0, 0, obsidian),
                },

                new PatternBlock[]{
                        new PatternBlock(4, 0, obsidian), new PatternBlock(4, -1, endPortalFrame), new PatternBlock(4, -2, endPortalFrame), new PatternBlock(4, -3, endPortalFrame), new PatternBlock(4, -4, obsidian),
                        new PatternBlock(3, 0, endPortalFrame), new PatternBlock(3, -1, air), new PatternBlock(3, -2, air), new PatternBlock(3, -3, air), new PatternBlock(3, -4, endPortalFrame),
                        new PatternBlock(2, 0, endPortalFrame), new PatternBlock(2, -1, air), new PatternBlock(2, -2, air), new PatternBlock(2, -3, air), new PatternBlock(2, -4, endPortalFrame),
                        new PatternBlock(1, 0, endPortalFrame), new PatternBlock(1, -1, air), new PatternBlock(1, -2, air), new PatternBlock(1, -3, air), new PatternBlock(1, -4, endPortalFrame),
                        new PatternBlock(0, 0, obsidian), new PatternBlock(0, -1, endPortalFrame), new PatternBlock(0, -2, endPortalFrame), new PatternBlock(0, -3, endPortalFrame), new PatternBlock(0, -4, obsidian),
                },


                new PatternBlock[]{
                        new PatternBlock(0, 4, obsidian), new PatternBlock(0, 3, endPortalFrame), new PatternBlock(0, 2, endPortalFrame), new PatternBlock(0, 1, endPortalFrame), new PatternBlock(0, 0, obsidian),
                        new PatternBlock(-1, 4, endPortalFrame), new PatternBlock(-1, 3, air), new PatternBlock(-1, 2, air), new PatternBlock(-1, 1, air), new PatternBlock(-1, 0, endPortalFrame),
                        new PatternBlock(-2, 4, endPortalFrame), new PatternBlock(-2, 3, air), new PatternBlock(-2, 2, air), new PatternBlock(-2, 1, air), new PatternBlock(-2, 0, endPortalFrame),
                        new PatternBlock(-3, 4, endPortalFrame), new PatternBlock(-3, 3, air), new PatternBlock(-3, 2, air), new PatternBlock(-3, 1, air), new PatternBlock(-3, 0, endPortalFrame),
                        new PatternBlock(-4, 4, obsidian), new PatternBlock(-4, 3, endPortalFrame), new PatternBlock(-4, 2, endPortalFrame), new PatternBlock(-4, 1, endPortalFrame), new PatternBlock(-4, 0, obsidian),
                },


                new PatternBlock[]{
                        new PatternBlock(0, 0, obsidian), new PatternBlock(0, -1, endPortalFrame), new PatternBlock(0, -2, endPortalFrame), new PatternBlock(0, -3, endPortalFrame), new PatternBlock(0, -4, obsidian),
                        new PatternBlock(-1, 0, endPortalFrame), new PatternBlock(-1, -1, air), new PatternBlock(-1, -2, air), new PatternBlock(-1, -3, air), new PatternBlock(-1, -4, endPortalFrame),
                        new PatternBlock(-2, 0, endPortalFrame), new PatternBlock(-2, -1, air), new PatternBlock(-2, -2, air), new PatternBlock(-2, -3, air), new PatternBlock(-2, -4, endPortalFrame),
                        new PatternBlock(-3, 0, endPortalFrame), new PatternBlock(-3, -1, air), new PatternBlock(-3, -2, air), new PatternBlock(-3, -3, air), new PatternBlock(-3, -4, endPortalFrame),
                        new PatternBlock(-4, 0, obsidian), new PatternBlock(-4, -1, endPortalFrame), new PatternBlock(-4, -2, endPortalFrame), new PatternBlock(-4, -3, endPortalFrame), new PatternBlock(-4, -4, obsidian),
                });

        for (PatternBlock[] pattern : blockSets) {

            boolean valid = true;

            for (PatternBlock p : pattern) {
                if (p.type() == air) {
                    continue;
                }

                if (world.getBlockAt(x + p.dx(), y, z + p.dz()).getType() != p.type()) {
                    valid = false;
                    break;
                }
            }

            if (!valid) continue;

            for (PatternBlock p : pattern) {
                Block b = world.getBlockAt(x + p.dx(), y, z + p.dz());

                if (p.type() != air) {
                    b.setType(convertMaterial(b.getType()));
                }
                if (b.getType() == endPortal) {
                    b.setType(air);
                }
            }

            return;
        }
    }

    private Material convertMaterial(Material input) {
        if (input == endStoneBricks) return endPortalFrame;
        if (input == endPortalFrame) return endStoneBricks;
        if (input == cryingObsidian) return obsidian;
        if (input == obsidian) return cryingObsidian;
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
