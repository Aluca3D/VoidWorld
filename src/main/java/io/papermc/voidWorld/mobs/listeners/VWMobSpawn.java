package io.papermc.voidWorld.mobs.listeners;

import io.papermc.voidWorld.mobs.config.VWMobSpawnConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class VWMobSpawn implements Listener {

    private final Map<EntityType, Integer> mobCounts = new HashMap<>();
    private final Map<EntityType, Integer> mobNextInterval = new HashMap<>();

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

        if (!config.hasVariation(type)) return;

        int count = mobCounts.getOrDefault(type, 0) + 1;
        mobCounts.put(type, count);

        int nextInterval = mobNextInterval.computeIfAbsent(
                type, config::getRandomInterval
        );

        if (count >= nextInterval) {
            EntityType replacement = config.getReplacement(type);

            Bukkit.getScheduler().runTaskLater(
                    plugin, () -> {
                        entity.remove();
                        entity.getWorld().spawnEntity(entity.getLocation(), replacement);
                    },
                    1L
            );

            mobCounts.put(type, 0);
            mobNextInterval.put(type, config.getRandomInterval(type));
        }
    }
}
