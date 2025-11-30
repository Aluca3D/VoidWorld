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

    public void setOneBlock(World world) {
        Location spawn = world.getSpawnLocation();
        oneBlockLocation = spawn.clone();
        oneBlockLocation.getBlock().setType(Material.DIRT);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getLocation().equals(oneBlockLocation)) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> oneBlockLocation.getBlock().setType(Material.DIRT), 1L);
        }
    }
}
