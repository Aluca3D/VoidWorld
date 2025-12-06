package io.papermc.voidWorld.mobs.config;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.*;

public class VWMobLootConfig {

    private final Map<EntityType, List<DropDefinition>> lootTable = new HashMap<>();

    public VWMobLootConfig(JavaPlugin plugin, ConfigurationNode root) {

        ConfigurationNode section = root.node("mob-loot");

        if (section.empty()) {
            System.out.println("[VW] No mob-loot section found!");
            return;
        }

        for (Map.Entry<Object, ? extends ConfigurationNode> entry : section.childrenMap().entrySet()) {

            String mobName = entry.getKey().toString();
            ConfigurationNode mobNode = entry.getValue();

            EntityType type;
            try {
                type = EntityType.valueOf(mobName.toUpperCase());
            } catch (Exception e) {
                plugin.getLogger().warning("Invalid mob: " + mobName);
                continue;
            }

            List<DropDefinition> drops = new ArrayList<>();

            List<? extends ConfigurationNode> dropNodes = mobNode.node("drops").childrenList();

            for (ConfigurationNode dropNode : dropNodes) {

                String materialName = dropNode.node("material").getString();
                Material material;

                if (materialName == null) {
                    plugin.getLogger().warning("Material name is null, skipping drop");

                    continue;
                }

                try {
                    material = Material.valueOf(materialName.toUpperCase());
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Invalid material: " + materialName);
                    continue;
                }


                int min = dropNode.node("amount", "min").getInt(1);
                int max = dropNode.node("amount", "max").getInt(1);

                double chance = dropNode.node("chance").getDouble(1.0);

                ConfigurationNode lootingNode = dropNode.node("looting");

                boolean lootingEnabled = lootingNode.node("enabled").getBoolean(false);
                double extraChance = lootingNode.node("extra-chance-per-level").getDouble(0.0);
                int extraAmount = lootingNode.node("extra-amount-per-level").getInt(0);

                drops.add(new DropDefinition(
                        material, min, max, chance,
                        lootingEnabled, extraChance, extraAmount
                ));
            }

            lootTable.put(type, drops);

            plugin.getLogger().info("Loaded " + drops.size() + " drops for " + type);
            for (DropDefinition drop : drops) {
                plugin.getLogger().info("-> Material:" + drop.material().toString());
            }
        }
    }

    public List<DropDefinition> getDrops(EntityType type) {
        return lootTable.getOrDefault(type, Collections.emptyList());
    }
}
