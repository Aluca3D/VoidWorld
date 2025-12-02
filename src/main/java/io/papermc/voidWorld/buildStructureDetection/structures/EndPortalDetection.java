package io.papermc.voidWorld.buildStructureDetection.structures;

import io.papermc.voidWorld.buildStructureDetection.VWPlayerStructureInterface;
import org.bukkit.block.Block;

public class EndPortalDetection implements VWPlayerStructureInterface {

    @Override
    public void detectStructure(Block startBlock) {
        if (isEndPortalBuild(startBlock)) {
            endPortalBuild(startBlock);
        } else if (isEndPortalDestroy(startBlock)) {
            endPortalDestroy(startBlock);
        }
    }

    private boolean isEndPortalBuild(Block startBlock) {
        return true;
    }

    private void endPortalBuild(Block startBlock) {
    }

    private boolean isEndPortalDestroy(Block startBlock) {
        return true;
    }

    private void endPortalDestroy(Block startBlock) {
    }

}
