package io.papermc.voidWorld.mobs.config;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class VWMobLootConfig {

    private final Map<EntityType, List<DropDefinition>> lootTable = new HashMap<>();

    public VWMobLootConfig(JavaPlugin plugin) {

        ConfigurationSection root = plugin.getConfig().getConfigurationSection("mob-loot");
        if (root == null) {
            plugin.getLogger().warning("No mob-loot section found in config!");
            return;
        }

        for (String mobKey : root.getKeys(false)) {

            EntityType type;
            try {
                type = EntityType.valueOf(mobKey.toUpperCase());
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid mob type in config: " + mobKey);
                continue;
            }

            List<Map<?, ?>> dropList = root.getConfigurationSection(mobKey).getMapList("drops");
            if (dropList.isEmpty()) {
                plugin.getLogger().warning("Mob " + mobKey + " has no drops.");
                continue;
            }

            List<DropDefinition> drops = new ArrayList<>();

            for (Map<?, ?> map : dropList) {
                String materialName = map.get("material").toString();
                Material material;
                try {
                    material = Material.valueOf(materialName.toUpperCase());
                } catch (Exception e) {
                    plugin.getLogger().warning("Invalid material: " + materialName);
                    continue;
                }

                Map<?, ?> amountMap = (Map<?, ?>) map.get("amount");

                int min = 1;
                int max = 1;

                if (amountMap != null) {
                    Object minObj = amountMap.get("min");
                    Object maxObj = amountMap.get("max");
                    if (minObj instanceof Number n) min = n.intValue();
                    if (maxObj instanceof Number n) max = n.intValue();
                }

                double chance = 1.0;
                Object chanceObj = map.get("chance");
                if (chanceObj instanceof Number n) chance = n.doubleValue();

                Map<?, ?> lootingMap = (Map<?, ?>) map.get("looting");
                boolean lootingEnabled = false;
                double extraChance = 0.0;
                int extraAmount = 0;
                if (lootingMap != null) {
                    Object enabledObj = lootingMap.get("enabled");
                    if (enabledObj instanceof Boolean b) lootingEnabled = b;

                    Object extraChanceObj = lootingMap.get("extra-chance-per-level");
                    if (extraChanceObj instanceof Number n) extraChance = n.doubleValue();

                    Object extraAmountObj = lootingMap.get("extra-amount-per-level");
                    if (extraAmountObj instanceof Number n) extraAmount = n.intValue();
                }

                drops.add(new DropDefinition(
                        material, min, max, chance,
                        lootingEnabled, extraChance, extraAmount
                ));
            }

            lootTable.put(type, drops);

            plugin.getLogger().info("Loaded " + drops.size() + " custom drops for " + type);
        }
    }

    public List<DropDefinition> getDrops(EntityType type) {
        return lootTable.getOrDefault(type, Collections.emptyList());
    }
}
