package io.papermc.voidWorld;

import io.papermc.paper.datapack.Datapack;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidWorld extends JavaPlugin {
    @Override
    public void onLoad() {
        getLogger().info("VoidWorld loaded!");
    }

    private VWOneBlockGenerator oneBlock;

    @Override
    public void onEnable() {
        getLogger().info("VoidWorld enabled!");

        oneBlock = new VWOneBlockGenerator(this);
        Bukkit.getPluginManager().registerEvents(oneBlock, this);

        World world = Bukkit.getWorlds().getFirst();
        oneBlock.setOneBlock(world);


        Datapack pack = this.getServer().getDatapackManager().getPack(getPluginMeta().getName() + "/provided");
        if (pack != null) {
            if (pack.isEnabled()) {
                getLogger().info("The Datapack loaded successfully!");
            } else {
                getLogger().warning("The datapack failed to load.");
            }
        } else {
            getLogger().warning("The datapack was not found.");
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("VoidWorld disabled!");
    }
}
