package io.papermc.voidWorld.mobs.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class VWMobSpawnConfig {
    private final Map<EntityType, EntityType> replacements = new HashMap<>();
    private final Map<EntityType, Integer> minInterval = new HashMap<>();
    private final Map<EntityType, Integer> maxInterval = new HashMap<>();

    public VWMobSpawnConfig(JavaPlugin plugin) {
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("mob-variation");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            EntityType type = EntityType.valueOf(key.toUpperCase());
            ConfigurationSection mob = section.getConfigurationSection(key);

            if (mob == null) continue;

            EntityType replacement = EntityType.valueOf(
                    Objects.requireNonNull(mob.getString("replacement")).toUpperCase()
            );

            int min = mob.getInt("interval.min", 10);
            int max = mob.getInt("interval.max", min);

            replacements.put(type, replacement);
            minInterval.put(type, min);
            maxInterval.put(type, max);

            plugin.getLogger().info(
                    "Loaded variation: " + type + " -> " + replacement + " (interval " + min + "-" + max + ")"
            );
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
