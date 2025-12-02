package io.papermc.voidWorld.buildStructureDetection;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

import java.util.List;

public class VWPlayerStructureRegistry implements Listener {
    private final List<VWPlayerStructureInterface> detectors;

    public VWPlayerStructureRegistry(List<VWPlayerStructureInterface> detectors) {
        this.detectors = detectors;
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        LightningStrike lightning = event.getLightning();
        Block struckBlock = lightning.getLocation().getBlock();

        if (struckBlock.getType() == Material.LIGHTNING_ROD) {
            for (VWPlayerStructureInterface detector : detectors) {
                detector.detectStructure(struckBlock);
            }
        }
    }

}
