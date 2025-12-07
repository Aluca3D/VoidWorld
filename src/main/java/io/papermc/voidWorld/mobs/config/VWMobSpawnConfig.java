package io.papermc.voidWorld.mobs.config;

import io.papermc.voidWorld.helper.VWDimension;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class VWMobSpawnConfig {

    private final Map<NamespacedKey, List<MobVariation>> variations = new HashMap<>();
    private final Map<EntityType, List<NamespacedKey>> keysByEntity = new HashMap<>();

    public VWMobSpawnConfig(JavaPlugin plugin, ConfigurationNode root) {

        if (root.empty()) {
            plugin.getLogger().warning("No " + root + " found!");
            return;
        }

        for (Map.Entry<Object, ? extends ConfigurationNode> mobEntry : root.childrenMap().entrySet()) {
            String mobKey = mobEntry.getKey().toString();
            EntityType entityType = EntityType.valueOf(mobKey.toUpperCase());

            ConfigurationNode mobNode = mobEntry.getValue();
            List<? extends ConfigurationNode> replacements = mobNode.childrenList();

            plugin.getLogger().info("Variation: " + entityType);

            for (int i = 0; i < replacements.size(); i++) {
                ConfigurationNode replacementNode = replacements.get(i);

                NamespacedKey namespacedKey = new NamespacedKey(plugin, entityType.toString().toLowerCase() + "-" + i);

                List<MobVariation> list = variations.computeIfAbsent(namespacedKey, k -> new ArrayList<>());
                keysByEntity.computeIfAbsent(entityType, k -> new ArrayList<>()).add(namespacedKey);

                String replacementStr = replacementNode.node("replacement").getString();
                if (replacementStr == null) continue;

                EntityType replacement = EntityType.valueOf(replacementStr.toUpperCase());

                int min = replacementNode.node("interval", "min").getInt(0);
                int max = replacementNode.node("interval", "max").getInt(min);

                boolean isBurning = replacementNode.node("isBurning").getBoolean(false);
                boolean isHitByLightning = replacementNode.node("isHitByLightning").getBoolean(false);

                boolean useDimension = replacementNode.node("useDimension").getBoolean(false);
                String dimensionStr = replacementNode.node("inDimension").getString("OVERWORLD");
                VWDimension dimension = VWDimension.fromString(dimensionStr);

                String standingOnStr = replacementNode.node("standingOn").getString("NONE");
                Material standingOn = parseBlock(standingOnStr);

                String hasEffectStr = replacementNode.node("hasEffect").getString("NONE");
                PotionEffectType hasEffect = parseEffect(hasEffectStr);

                MobVariation variation = new MobVariation(
                        replacement,
                        min, max,
                        isBurning,
                        isHitByLightning,
                        standingOn,
                        hasEffect,
                        useDimension, dimension
                );

                list.add(variation);
                plugin.getLogger().info("-> " + replacement + " (" + min + "-" + max + ") Key: " + namespacedKey);
            }
        }
        /*
        for (EntityType type : keysByEntity.keySet()) {
            plugin.getLogger().info("EntityType in config: " + type + " | Keys: " + keysByEntity.get(type));
        }
        */
    }


    public boolean hasVariation(EntityType type) {
        return !getAllVariations(type).isEmpty();
    }

    public MobVariation getVariation(NamespacedKey key) {
        List<MobVariation> list = variations.get(key);
        if (list == null || list.isEmpty()) return null;
        return list.getFirst();
    }

    public EntityType getReplacement(NamespacedKey key) {
        List<MobVariation> list = variations.get(key);
        if (list == null || list.isEmpty()) return null;

        int index = ThreadLocalRandom.current().nextInt(list.size());
        return list.get(index).replacement();
    }

    public int getRandomInterval(NamespacedKey key) {
        List<MobVariation> list = variations.get(key);
        if (list == null || list.isEmpty()) return 0;

        MobVariation variation = list.get(ThreadLocalRandom.current().nextInt(list.size()));
        return ThreadLocalRandom.current().nextInt(variation.intervalMin(), variation.intervalMax() + 1);
    }

    public List<NamespacedKey> getKeysForEntity(EntityType type) {
        List<NamespacedKey> keys = new ArrayList<>();
        String prefix = type.name().toLowerCase() + "-";
        for (NamespacedKey key : variations.keySet()) {
            if (key.getKey().startsWith(prefix)) {
                keys.add(key);
            }
        }
        return keys;
    }

    private List<MobVariation> getAllVariations(EntityType type) {
        List<MobVariation> all = new ArrayList<>();
        String prefix = type.name().toLowerCase() + "-";
        for (Map.Entry<NamespacedKey, List<MobVariation>> entry : variations.entrySet()) {
            if (entry.getKey().getKey().startsWith(prefix)) {
                all.addAll(entry.getValue());
            }
        }
        return all;
    }

    private static Material parseBlock(String blockName) {
        if (blockName == null || blockName.equals("NONE")) return null;
        try {
            return Material.valueOf(blockName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static PotionEffectType parseEffect(String effectName) {
        if (effectName == null || effectName.equalsIgnoreCase("NONE")) return null;
        return Registry.MOB_EFFECT.get(NamespacedKey.minecraft(effectName.toLowerCase()));
    }
}
