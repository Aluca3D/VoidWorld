package io.papermc.voidWorld;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VWOneBlockGenerator implements Listener {
    private final JavaPlugin plugin;
    private Location oneBlockLocation;

    public VWOneBlockGenerator(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void setOneBlock() {
        World world = Bukkit.getWorld("world");
        if (world == null) {
            plugin.getLogger().warning("Overworld not found, cannot place OneBlock!");
            return;
        }
        
        Location spawn = world.getSpawnLocation().getBlock().getLocation();

        if (spawn.getBlock().getType() != Material.AIR) {
            plugin.getLogger().info("OneBlock already exists at spawn, skipping placement.");
        } else {
            plugin.getLogger().info("Placed OneBlock at spawn: " + spawn);
            spawn.getBlock().setType(Material.DIRT);
        }
        oneBlockLocation = spawn.clone();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getLocation().equals(oneBlockLocation)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> oneBlockLocation.getBlock().setType(Material.DIRT), 1L);
        }
    }
}
