package io.papermc.voidWorld.buildStructureDetection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class EndPortalDetection implements Listener {
    private final Material CO = Material.CRYING_OBSIDIAN;
    private final Material EB = Material.END_STONE_BRICKS;

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        LightningStrike lightning = event.getLightning();
        Block struckBlock = lightning.getLocation().getBlock();

        if (struckBlock.getType() == Material.LIGHTNING_ROD) {
            if (isEndPortalBuild(struckBlock)) {
                endPortalBuild(struckBlock);
            } else if (isEndPortalDestroy(struckBlock)) {
                endPortalDestroy(struckBlock);
            }
        }
    }

    private boolean isEndPortalBuild(Block startBlock) {
        int x = startBlock.getX();
        int y = startBlock.getY() - 1;
        int z = startBlock.getZ();

        return isCorrectBlock(startBlock, x + 2, y, z + 1, EB) && isCorrectBlock(startBlock, x + 2, y, z, EB) && isCorrectBlock(startBlock, x + 2, y, z - 1, EB) &&
                isCorrectBlock(startBlock, x + 1, y, z + 2, EB) && isCorrectBlock(startBlock, x + 1, y, z + 1, CO) && isCorrectBlock(startBlock, x + 1, y, z, CO) && isCorrectBlock(startBlock, x + 1, y, z - 1, CO) && isCorrectBlock(startBlock, x + 1, y, z - 2, EB) &&
                isCorrectBlock(startBlock, x, y, z + 2, EB) && isCorrectBlock(startBlock, x, y, z + 1, CO) && isCorrectBlock(startBlock, x, y, z, CO) && isCorrectBlock(startBlock, x, y, z - 1, CO) && isCorrectBlock(startBlock, x, y, z - 2, EB) &&
                isCorrectBlock(startBlock, x - 1, y, z + 2, EB) && isCorrectBlock(startBlock, x - 1, y, z + 1, CO) && isCorrectBlock(startBlock, x - 1, y, z, CO) && isCorrectBlock(startBlock, x - 1, y, z - 1, CO) && isCorrectBlock(startBlock, x - 1, y, z - 2, EB) &&
                isCorrectBlock(startBlock, x - 2, y, z + 1, EB) && isCorrectBlock(startBlock, x - 2, y, z, EB) && isCorrectBlock(startBlock, x - 2, y, z - 1, EB);
    }

    private void endPortalBuild(Block startBlock) {
    }

    private boolean isEndPortalDestroy(Block startBlock) {
        return true;
    }

    private void endPortalDestroy(Block startBlock) {
    }

    // TODO: Make Helper for such function
    private boolean isCorrectBlock(Block startBlock, int x, int y, int z, Material material) {
        return startBlock.getWorld().getBlockAt(x, y, z).getType() == material;
    }

}
