package io.papermc.voidWorld;

import org.spongepowered.configurate.ConfigurationNode;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.jackson.JacksonConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class VWConfigLoader {

    public static ConfigurationNode loadConfig(JavaPlugin plugin, String fileName) {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            if (!dataFolder.mkdirs()) {
                plugin.getLogger().warning("Failed to create plugin data folder: " + dataFolder.getPath());
            }
        }

        File configFile = new File(dataFolder, fileName);

        if (!configFile.exists()) {
            try (InputStream in = plugin.getResource(fileName)) {
                if (in != null) {
                    Files.copy(in, configFile.toPath());
                } else {
                    if (!configFile.createNewFile()) {
                        plugin.getLogger().warning("Failed to create config file: " + fileName);
                    }
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Unable to create config file " + fileName + ": " + e.getMessage());
            }
        }


        JacksonConfigurationLoader loader = JacksonConfigurationLoader.builder()
                .file(configFile)
                .build();

        try {
            return loader.load();
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to load config file " + fileName + ": " + e.getMessage());
            return loader.createNode();
        }
    }
}
