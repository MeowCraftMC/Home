package io.github.elihuso.home.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class HomeSet {
    @Nullable
    public static Location getHome(@NotNull Plugin plugin, @NotNull UUID player, @NotNull String name) {
        FileConfiguration config = load(plugin, player);
        if (config == null) return null;
        return config.getLocation(name);
    }

    public static boolean setHome(@NotNull Plugin plugin, @NotNull UUID player, @NotNull String name, @NotNull Location location) {
        FileConfiguration config = load(plugin, player);
        if (config == null) return false;
        config.set(name, location);
        return save(plugin, config, player);
    }

    public static boolean delHome(@NotNull Plugin plugin, @NotNull UUID player, @NotNull String name) {
        FileConfiguration config = load(plugin, player);
        if (config == null) return false;
        config.set(name, null);
        return save(plugin, config, player);
    }

    public static String[] listHome(@NotNull Plugin plugin, @NotNull UUID player) {
        FileConfiguration config = load(plugin, player);
        Set<String> homeSet = config.getKeys(false);
        return homeSet.toArray(String[]::new);
    }

    @Nullable
    private static FileConfiguration load(Plugin plugin, UUID player) {
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(plugin.getDataFolder() + "/" + player);
        }
        catch (Exception ex) {
            try {
                config.save(plugin.getDataFolder() + "/" + player);
            }
            catch (Exception exception) {
                return null;
            }
        }
        return config;
    }

    private static boolean save(@NotNull Plugin plugin, @NotNull FileConfiguration config, @NotNull UUID player) {
        try {
            config.save(plugin.getDataFolder() + "/" + player);
        }
        catch (IOException e) {
            return false;
        }
        return true;
    }
}
