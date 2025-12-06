package io.papermc.voidWorld.mobs.listeners;

import io.papermc.voidWorld.mobs.config.VWMobSpawnConfig;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VWMobSpawn implements Listener {

    private final Map<NamespacedKey, Integer> mobCounts = new HashMap<>();
    private final Map<NamespacedKey, Integer> mobNextInterval = new HashMap<>();

    private final JavaPlugin plugin;
    private final VWMobSpawnConfig config;

    public VWMobSpawn(JavaPlugin plugin, VWMobSpawnConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        EntityType type = entity.getType();
        //plugin.getLogger().info("Spawned mob: " + type);

        if (!config.hasVariation(type)) {
            //plugin.getLogger().info("No variations for: " + type);
            return;
        }

        List<NamespacedKey> keys = config.getKeysForEntity(type);
        //plugin.getLogger().info("Keys for " + type + ": " + keys);

        for (NamespacedKey key : keys) {
            int count = mobCounts.getOrDefault(key, 0) + 1;
            mobCounts.put(key, count);

            int nextInterval = mobNextInterval.computeIfAbsent(key, k -> config.getRandomInterval(key));

            //plugin.getLogger().info("Checking key: " + key + " | Count: " + count + " | NextInterval: " + nextInterval);

            if (count >= nextInterval) {
                EntityType replacement = config.getReplacement(key);

                //plugin.getLogger().info("Replacing " + type + " with " + replacement + " for key: " + key);

                Bukkit.getScheduler().runTaskLater(
                        plugin, () -> {
                            entity.remove();
                            entity.getWorld().spawnEntity(entity.getLocation(), replacement);
                        },
                        1L
                );

                mobCounts.put(key, 0);
                mobNextInterval.put(key, config.getRandomInterval(key));

                //plugin.getLogger().info("Reset count and next interval for key: " + key);
                break;
            }
        }
    }
}
