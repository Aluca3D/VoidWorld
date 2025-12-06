package io.papermc.voidWorld.mobs.config;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class VWMobConfig {

    private final Map<EntityType, List<DropDefinition>> lootTable = new HashMap<>();
    private final Map<EntityType, EntityType> replacements = new HashMap<>();
    private final Map<EntityType, Integer> minInterval = new HashMap<>();
    private final Map<EntityType, Integer> maxInterval = new HashMap<>();

    public VWMobConfig(JavaPlugin plugin, File configFile) throws IOException {
        JacksonConfigurationLoader loader = JacksonConfigurationLoader.builder()
                .file(configFile)
                .build();

        ConfigurationNode root = loader.load();

        loadMobVariation(root.node("mob-variation"), plugin);
        loadMobLoot(root.node("mob-loot"), plugin);
    }

    private void loadMobVariation(ConfigurationNode node, JavaPlugin plugin) {
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            try {
                EntityType type = EntityType.valueOf(entry.getKey().toString().toUpperCase());
                ConfigurationNode mobNode = entry.getValue();

                EntityType replacement = EntityType.valueOf(
                        mobNode.node("replacement").getString().toUpperCase()
                );

                int min = mobNode.node("interval", "min").getInt(10);
                int max = mobNode.node("interval", "max").getInt(min);

                replacements.put(type, replacement);
                minInterval.put(type, min);
                maxInterval.put(type, max);

                plugin.getLogger().info(
                        "Loaded variation: " + type + " -> " + replacement + " (interval " + min + "-" + max + ")"
                );
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid mob variation entry: " + entry.getKey());
            }
        }
    }

    private void loadMobLoot(ConfigurationNode node, JavaPlugin plugin) {
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            try {
                EntityType type = EntityType.valueOf(entry.getKey().toString().toUpperCase());
                ConfigurationNode mobNode = entry.getValue();

                List<DropDefinition> drops = new ArrayList<>();
                for (ConfigurationNode dropNode : mobNode.node("drops").childrenList()) {
                    Material material = Material.valueOf(dropNode.node("material").getString().toUpperCase());
                    int min = dropNode.node("amount", "min").getInt(1);
                    int max = dropNode.node("amount", "max").getInt(min);
                    double chance = dropNode.node("chance").getDouble(1.0);

                    ConfigurationNode looting = dropNode.node("looting");
                    boolean lootingEnabled = looting.node("enabled").getBoolean(false);
                    double extraChance = looting.node("extra-chance-per-level").getDouble(0.0);
                    int extraAmount = looting.node("extra-amount-per-level").getInt(0);

                    drops.add(new DropDefinition(material, min, max, chance, lootingEnabled, extraChance, extraAmount));
                }

                lootTable.put(type, drops);
                plugin.getLogger().info("Loaded " + drops.size() + " custom drops for " + type);
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid mob loot entry: " + entry.getKey());
            }
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

    public List<DropDefinition> getDrops(EntityType type) {
        return lootTable.getOrDefault(type, Collections.emptyList());
    }
}
