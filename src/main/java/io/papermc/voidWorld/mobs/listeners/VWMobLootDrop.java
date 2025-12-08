package io.papermc.voidWorld.mobs.listeners;

import io.papermc.voidWorld.mobs.helper.DropDefinition;
import io.papermc.voidWorld.mobs.config.VWMobLootDropConfig;
import io.papermc.voidWorld.mobs.helper.ItemStackConfiguration;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class VWMobLootDrop implements Listener {

    private final Random random = new Random();
    private final JavaPlugin plugin;
    private final VWMobLootDropConfig config;

    public VWMobLootDrop(JavaPlugin plugin, VWMobLootDropConfig config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        World.Environment dimension = entity.getWorld().getEnvironment();

        List<DropDefinition> drops = config.getDrops(entity.getType());
        if (drops.isEmpty()) return;

        int lootingLevel = 0;

        if (entity.getKiller() != null) {
            lootingLevel = entity.getKiller()
                    .getInventory()
                    .getItemInMainHand()
                    .getEnchantmentLevel(Enchantment.LOOTING);
        }

        for (DropDefinition dropDefinition : drops) {

            List<String> tags = dropDefinition.tags();

            if (tags != null && !tags.isEmpty()) {

                boolean matches = false;

                for (String tag : tags) {
                    if (tag == null || tag.isBlank()) continue;

                    if (entity.getScoreboardTags().contains(tag)) {
                        matches = true;
                        break;
                    }
                }

                if (!matches) continue;
            }

            if (dropDefinition.useDimension() && !dropDefinition.inDimension().getEnvironment().equals(dimension)) continue;

            double chance = dropDefinition.chance();

            if (dropDefinition.lootingEnabled() && lootingLevel > 0) {
                chance += dropDefinition.extraChancePerLevel() * lootingLevel;
            }

            if (random.nextDouble() > chance) continue;

            int amount = dropDefinition.minAmount() +
                    random.nextInt(dropDefinition.maxAmount() - dropDefinition.minAmount() + 1);

            if (dropDefinition.lootingEnabled() && lootingLevel > 0) {
                amount += dropDefinition.extraAmountPerLevel() * lootingLevel;
            }

            if (amount > 0) {
                ItemStack itemResult = ItemStackConfiguration.build(dropDefinition.itemStackConfiguration());
                itemResult.setAmount(amount);
                event.getDrops().add(itemResult);
            }
        }
    }
}
