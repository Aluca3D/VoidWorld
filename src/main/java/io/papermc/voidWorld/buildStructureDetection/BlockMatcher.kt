package io.papermc.voidWorld.buildStructureDetection;

import org.bukkit.Material;

public record BlockMatcher(int dx, int dz, Material[] types) {

    public BlockMatcher(int dx, int dz, Material type) {
        this(dx, dz, new Material[]{type});
    }

    public boolean matches(Material blockType) {
        for (Material mat : types) {
            if (blockType == mat) return true;
        }
        return false;
    }
}