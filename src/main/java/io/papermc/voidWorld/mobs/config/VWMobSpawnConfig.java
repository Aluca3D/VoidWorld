package io.papermc.voidWorld.mobs.config;

import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class VWMobSpawnConfig {

    private final Map<EntityType, EntityType> replacements = new HashMap<>();
    private final Map<EntityType, Integer> minInterval = new HashMap<>();
    private final Map<EntityType, Integer> maxInterval = new HashMap<>();

    public VWMobSpawnConfig(JavaPlugin plugin, ConfigurationNode root) {

        ConfigurationNode section = root.node("mob-variation");

        if (section.empty()) {
            plugin.getLogger().warning("No mob-variation section found!");
            return;
        }

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : section.childrenMap().entrySet()) {

            String key = entry.getKey().toString();
            ConfigurationNode mobNode = entry.getValue();

            EntityType type = EntityType.valueOf(key.toUpperCase());

            String replacementStr = mobNode.node("replacement").getString();
            EntityType replacement = EntityType.valueOf(
                    Objects.requireNonNull(replacementStr).toUpperCase()
            );

            int min = mobNode.node("interval", "min").getInt(10);
            int max = mobNode.node("interval", "max").getInt(min);

            replacements.put(type, replacement);
            minInterval.put(type, min);
            maxInterval.put(type, max);

            plugin.getLogger().info("Variation: " + type + " -> " + replacement + " (" + min + "-" + max + ")");
        }
    }

    public boolean hasVariation(EntityType type) {
        return replacements.containsKey(type);
    }

    public EntityType getReplacement(EntityType type) {
        return replacements.get(type);
    }

    public int getRandomInterval(EntityType type) {
        int min = minInterval.get(type);
        int max = maxInterval.get(type);
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
