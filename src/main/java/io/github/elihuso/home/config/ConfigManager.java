package io.github.elihuso.home.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {
    private final FileConfiguration config;

    public ConfigManager(Plugin plugin) {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    public int Number() {
        return config.getInt("number", 10);
    }

    public boolean AllowMultiple() {
        return config.getBoolean("allowMultiple", true);
    }

    public boolean AllowBed() {
        return config.getBoolean("allowBed", true);
    }
}
