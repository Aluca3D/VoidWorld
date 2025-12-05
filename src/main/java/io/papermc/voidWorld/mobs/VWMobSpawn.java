package io.papermc.voidWorld.mobs;

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
    private final Map<EntityType, Integer> mobThresholds = new HashMap<>();
    private final Map<EntityType, EntityType> replacementMap = new HashMap<>();

    private final JavaPlugin plugin;

    public VWMobSpawn(JavaPlugin plugin) {
        this.plugin = plugin;
        // TODO: Add Config file for easier addition of new Variants
        addMobVariation(EntityType.SKELETON, EntityType.STRAY, 15);
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        EntityType type = entity.getType();

        if (!replacementMap.containsKey(type)) return;

        int count = mobCounts.getOrDefault(type, 0) + 1;
        mobCounts.put(type, count);

        int threshold = mobThresholds.get(type);

        if (count >= threshold) {
            EntityType replacement = replacementMap.get(type);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                entity.remove();
                entity.getWorld().spawnEntity(entity.getLocation(), replacement);
            }, 1L);

            mobCounts.put(type, 0);
        }
    }

    private void addMobVariation(EntityType target, EntityType replacement, int threshold) {
        replacementMap.put(target, replacement);
        mobThresholds.put(target, threshold);
    }
}
