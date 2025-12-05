package io.papermc.voidWorld.mobs;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class VWMobDrops implements Listener {

    @EventHandler
    public void onMobDeath(EntityDeathEvent event){
        LivingEntity entity = event.getEntity();

        if (entity.getType() == EntityType.ENDERMAN) {
            // TODO:
            //  - Add Functions to make it interact with Looting enchantment
            //  - Add Config file for easier addition of new loot with "use Looting" bool
            if (Math.random() < 0.20) {
                event.getDrops().add(new ItemStack(Material.END_STONE, 1));
            }
        }
    }

}
