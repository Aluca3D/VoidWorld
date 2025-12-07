package io.papermc.voidWorld.mobs.listeners;

import io.papermc.voidWorld.mobs.DropDefinition;
import io.papermc.voidWorld.mobs.config.VWMobLootConfig;
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

public class VWMobLoot implements Listener {

    private final Random random = new Random();
    private final JavaPlugin plugin;
    private final VWMobLootConfig config;

    public VWMobLoot(JavaPlugin plugin, VWMobLootConfig config) {
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

        for (DropDefinition def : drops) {

            if (def.useDimension() && !def.inDimension().getEnvironment().equals(dimension)) continue;

            double chance = def.chance();

            if (def.lootingEnabled() && lootingLevel > 0) {
                chance += def.extraChancePerLevel() * lootingLevel;
            }

            if (random.nextDouble() > chance) continue;

            int amount = def.minAmount() +
                    random.nextInt(def.maxAmount() - def.minAmount() + 1);

            if (def.lootingEnabled() && lootingLevel > 0) {
                amount += def.extraAmountPerLevel() * lootingLevel;
            }

            if (amount > 0) {
                event.getDrops().add(new ItemStack(def.material(), amount));
            }
        }
    }
}
