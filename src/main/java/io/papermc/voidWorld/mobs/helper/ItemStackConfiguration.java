package io.papermc.voidWorld.mobs.helper;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ItemStackConfiguration(
        Material material,
        int damage,
        String name,
        List<String> lore,
        Map<String, Integer> enchants
) {
    public static ItemStackConfiguration parseItem(ConfigurationNode node) {
        if (node == null || node.empty()) return null;

        String materialStr = node.node("material").getString();
        if (materialStr == null) return null;

        Material material = Material.matchMaterial(materialStr.toUpperCase());
        if (material == null) return null;

        String name = node.node("name").getString();

        int damage = node.node("damage").getInt(0);

        List<String> lore = new ArrayList<>();
        for (ConfigurationNode line : node.node("lore").childrenList()) {
            String string = line.getString();
            if (string != null) lore.add(string);
        }

        Map<String, Integer> enchants = new HashMap<>();
        for (Map.Entry<Object, ? extends ConfigurationNode> enchant : node.node("enchants").childrenMap().entrySet()) {
            String enchantment = enchant.getKey().toString();
            Integer level = enchant.getValue().getInt(0);
            enchants.put(enchantment, level);
        }

        return new ItemStackConfiguration(material, damage, name, lore, enchants);
    }

    public static ItemStack build(ItemStackConfiguration itemStackConfiguration) {
        if (itemStackConfiguration == null || itemStackConfiguration.material() == null) return null;

        Material material = itemStackConfiguration.material();

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        if (itemStackConfiguration.name() != null) {
            meta.displayName(MiniMessage.miniMessage().deserialize(itemStackConfiguration.name()));
        }

        if (itemStackConfiguration.lore() != null) {
            meta.lore(
                    itemStackConfiguration.lore().stream()
                            .map(line -> MiniMessage.miniMessage().deserialize(line))
                            .toList()
            );
        }

        if (itemStackConfiguration.enchants() != null) {
            itemStackConfiguration.enchants().forEach((key, level) -> {
                var enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft(key.toLowerCase()));
                if (enchantment != null) {
                    meta.addEnchant(enchantment, level, true);
                }
            });
        }

        if (itemStackConfiguration.damage() != 0) {
            if (meta instanceof Damageable damageable) {
                int maxDurability = item.getType().getMaxDurability();
                if (maxDurability > 0) {
                    int currentDamage = damageable.getDamage();

                    int damageToAdd = (int) Math.ceil(maxDurability * (itemStackConfiguration.damage() / 100.0));

                    int newDamage = currentDamage + damageToAdd;

                    if (newDamage >= maxDurability) {
                        return null;
                    } else {
                        damageable.setDamage(newDamage);
                        item.setItemMeta(damageable);
                        return item;
                    }
                }
            }
        }

        item.setItemMeta(meta);
        return item;
    }
}
