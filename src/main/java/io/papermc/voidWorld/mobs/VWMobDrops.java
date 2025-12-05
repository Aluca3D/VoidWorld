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
            if (Math.random() < 0.20) {
                event.getDrops().add(new ItemStack(Material.END_STONE, 1));
            }
        }
    }

}
