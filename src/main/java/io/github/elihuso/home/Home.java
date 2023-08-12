package io.github.elihuso.home;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public final class Home extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(this.getDataFolder() + "/" + player.getUniqueId().toString());
        }
        catch (Exception ex) {
            try {
                config.save(this.getDataFolder() + "/" + player.getUniqueId().toString());
            }
            catch (Exception exception) {
                return false;
            }
        }
        String path = "";
        if (args.length == 0)
            path = "home";
        else
            path = args[0];
        if (command.getName().equalsIgnoreCase("home")) {
            Location target = config.getLocation(path);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "No such home!");
                return false;
            }
            player.teleport(target);
            return true;
        }
        if (command.getName().equalsIgnoreCase("sethome")) {
            config.set(path, player.getLocation());
            try {
                config.save(this.getDataFolder() + "/" + player.getUniqueId().toString());
            }
            catch (IOException e) {
                return false;
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("delhome")) {
            config.set(path, null);
            try {
                config.save(this.getDataFolder() + "/" + player.getUniqueId().toString());
            }
            catch (IOException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
