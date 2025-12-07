package io.papermc.voidWorld.mobs.listeners;

import io.papermc.voidWorld.mobs.MobEquipment;
import io.papermc.voidWorld.mobs.MobVariation;
import io.papermc.voidWorld.mobs.config.VWMobSpawnConfig;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VWMobSpawn implements Listener {

    private final Map<NamespacedKey, Integer> mobCounts = new HashMap<>();
    private final Map<NamespacedKey, Integer> mobNextInterval = new HashMap<>();

    private final EnumSet<EntityDamageEvent.DamageCause> burningCauses = EnumSet.of(
            EntityDamageEvent.DamageCause.FIRE,
            EntityDamageEvent.DamageCause.FIRE_TICK,
            EntityDamageEvent.DamageCause.LAVA,
            EntityDamageEvent.DamageCause.HOT_FLOOR,
            EntityDamageEvent.DamageCause.CAMPFIRE
    );

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

        List<NamespacedKey> keys = config.getKeysForEntity(type);

        for (NamespacedKey key : keys) {
            if (config.getRandomInterval(key) == 0) continue;
            if (isNotInDimension(entity, key)) continue;

            int count = mobCounts.getOrDefault(key, 0) + 1;
            mobCounts.put(key, count);

            int nextInterval = mobNextInterval.computeIfAbsent(key, k -> config.getRandomInterval(key));

            if (count >= nextInterval) {
                EntityType replacement = config.getReplacement(key);

                MobVariation variation = config.getVariation(key);
                if (variation == null) continue;

                replaceEntity(entity, replacement, variation);

                mobCounts.put(key, 0);
                mobNextInterval.put(key, config.getRandomInterval(key));
                break;
            }
        }
    }

    @EventHandler
    public void onMobDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        EntityType type = entity.getType();
        if (!config.hasVariation(type)) return;

        List<NamespacedKey> keys = config.getKeysForEntity(type);
        for (NamespacedKey key : keys) {
            if (config.getRandomInterval(key) != 0) continue;
            if (isNotInDimension(entity, key)) continue;
            if (isNotStandingOn(entity, key)) continue;

            MobVariation variation = config.getVariation(key);
            if (variation == null) continue;

            if (!variation.isHitByLightning() && !variation.isBurning()) continue;
            if (variation.isHitByLightning() && event.getCause() != EntityDamageEvent.DamageCause.LIGHTNING) continue;

            if (variation.isBurning() && !burningCauses.contains(event.getCause())) {
                continue;
            }

            EntityType replacement = config.getReplacement(key);
            replaceEntity(entity, replacement, variation);
            break;
        }
    }

    @EventHandler
    public void onMobEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        EntityType type = entity.getType();
        if (!config.hasVariation(type)) return;

        PotionEffect effect = event.getNewEffect();
        if (effect == null) return;

        List<NamespacedKey> keys = config.getKeysForEntity(type);
        for (NamespacedKey key : keys) {
            if (config.getRandomInterval(key) != 0) continue;
            if (isNotInDimension(entity, key)) continue;
            if (isNotStandingOn(entity, key)) continue;

            MobVariation variation = config.getVariation(key);
            if (variation == null) continue;

            if (variation.hasEffect() == null) continue;
            if (Registry.MOB_EFFECT.get(variation.hasEffect().key()) != effect.getType()) continue;

            EntityType replacement = config.getReplacement(key);
            replaceEntity(entity, replacement, variation);
            break;
        }
    }

    private void replaceEntity(LivingEntity originalEntity, EntityType replacementType, MobVariation variation) {

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location location = originalEntity.getLocation();
            originalEntity.remove();

            LivingEntity spawned = (LivingEntity) location.getWorld().spawnEntity(location, replacementType);

            List<String> tags = variation.tags();

            if (tags != null) {
                for (String tag : tags) {
                    if (tag == null || tag.isBlank()) continue;
                    spawned.addScoreboardTag(tag);
                }
            }

            if (variation.name() != null) {
                spawned.customName(variation.name());
                spawned.setCustomNameVisible(true);
            }

            MobEquipment equipment = variation.equipment();
            applyEquipment(spawned, equipment);
        }, 1L);
    }

    private void applyEquipment(LivingEntity mob, MobEquipment eq) {
        if (eq == null || mob.getEquipment() == null) return;

        if (eq.mainHand() != null) mob.getEquipment().setItemInMainHand(eq.mainHand());
        if (eq.offHand() != null) mob.getEquipment().setItemInOffHand(eq.offHand());
        if (eq.helmet() != null) mob.getEquipment().setHelmet(eq.helmet());
        if (eq.chestplate() != null) mob.getEquipment().setChestplate(eq.chestplate());
        if (eq.leggings() != null) mob.getEquipment().setLeggings(eq.leggings());
        if (eq.boots() != null) mob.getEquipment().setBoots(eq.boots());
    }

    private boolean isNotInDimension(LivingEntity entity, NamespacedKey key) {
        MobVariation variation = config.getVariation(key);
        if (variation == null) return false;

        World.Environment environment = entity.getWorld().getEnvironment();
        boolean notInDimension = environment != variation.inDimension().getEnvironment();

        if (!variation.useDimension()) return false;

        return notInDimension;
    }

    private boolean isNotStandingOn(LivingEntity entity, NamespacedKey key) {
        MobVariation variation = config.getVariation(key);
        if (variation == null) return false;

        Material blockBelow = entity.getLocation()
                .subtract(0, 0.1, 0)
                .getBlock()
                .getType();

        Material standingOn = variation.standingOn();
        if (standingOn == null) return false;

        return blockBelow != standingOn;
    }
}
