package io.papermc.voidWorld.buildStructureDetection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class EndPortalDetection  implements Listener {

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
