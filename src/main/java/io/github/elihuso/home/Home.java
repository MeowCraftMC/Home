package io.github.elihuso.home;

import io.github.elihuso.home.config.ConfigManager;
import io.github.elihuso.home.config.HomeSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class Home extends JavaPlugin {

    private ConfigManager config = new ConfigManager(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("home").setTabCompleter(this);
        getCommand("sethome").setTabCompleter(this);
        getCommand("delhome").setTabCompleter(this);
        getCommand("listhome").setTabCompleter(this);
        getCommand("bed").setTabCompleter(this);
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        String name = "";
        if (args.length == 0)
            name = "home";
        else {
            if (!config.AllowMultiple()) {
                player.sendMessage(ChatColor.RED + "This server doesn't allowed multiple homes!");
                return false;
            }
            name = args[0];
        }
        if (command.getName().equalsIgnoreCase("home")) {
            Location target = HomeSet.getHome(this, player.getUniqueId(), name);
            if (target == null) {
                player.sendMessage(ChatColor.RED + "No such home!");
                return false;
            }
            player.teleport(target);
            player.sendMessage(ChatColor.GREEN + "Teleport to home " + (name.equalsIgnoreCase("home") ? "" : ChatColor.WHITE + name) + ChatColor.GREEN + ".");
            return true;
        }
        if (command.getName().equalsIgnoreCase("sethome")) {
            if (HomeSet.listHome(this, player.getUniqueId()).length >= config.Number()) {
                player.sendMessage(ChatColor.RED + "Too many home! Delete some to add new home.");
                return false;
            }
            if (!HomeSet.setHome(this, player.getUniqueId(), name, player.getLocation())) return false;
            player.sendMessage(ChatColor.GREEN + "Set your location as home " + (name.equalsIgnoreCase("home") ? "" : ChatColor.WHITE + name) + ChatColor.GREEN + ".");
            return true;
        }
        if (command.getName().equalsIgnoreCase("delhome")) {
            if (!HomeSet.delHome(this, player.getUniqueId(), name)) return false;
            player.sendMessage(ChatColor.YELLOW + "Deleted.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("listhome")) {
            String[] homes = HomeSet.listHome(this, player.getUniqueId());
            if (homes.length == 0) {
                player.sendMessage(ChatColor.RED + "You have never set a home!");
                return false;
            }
            player.sendMessage("Homes you set:\n----------------------------------------------------------------");
            for (String v : homes) {
                Location location = HomeSet.getHome(this, player.getUniqueId(), v);
                player.sendMessage(ChatColor.WHITE + v +
                        ChatColor.AQUA + "  world: " + ChatColor.WHITE + location.getWorld().getName() +
                        ChatColor.GREEN + "  x: " + ChatColor.WHITE + location.getBlockX() +
                        ChatColor.GREEN + "  y: " + ChatColor.WHITE + location.getBlockY() +
                        ChatColor.GREEN + "  z: " + ChatColor.WHITE + location.getBlockZ());
            }
        }
        if (command.getName().equalsIgnoreCase("bed")) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player.getUniqueId());
            Location location = player.getBedSpawnLocation() == null ? offlinePlayer.getBedSpawnLocation() : player.getBedSpawnLocation();
            if (location == null) {
                player.sendMessage(ChatColor.RED + "Failed to find your bed; You may never sleep or your bed is unavailable.");
                return false;
            }
            player.teleport(location);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
        List<String> list = new ArrayList<>();
        if (!config.AllowMultiple()) return list;
        if (!(sender instanceof Player)) return list;
        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("home") || command.getName().equalsIgnoreCase("delhome")) {
            String[] homes = HomeSet.listHome(this, player.getUniqueId());
            list.addAll(Arrays.asList(homes));
        }
        return list;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
