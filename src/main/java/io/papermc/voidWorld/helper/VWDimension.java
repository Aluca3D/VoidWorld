package io.papermc.voidWorld.helper;

import org.bukkit.World;

public enum VWDimension {
    OVERWORLD(World.Environment.NORMAL),
    NETHER(World.Environment.NETHER),
    END(World.Environment.THE_END);

    private final World.Environment env;

    VWDimension(World.Environment env) {
        this.env = env;
    }

    public World.Environment getEnvironment() {
        return env;
    }

    public static VWDimension fromString(String name) {
        if (name == null) return null;
        try {
            return VWDimension.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
