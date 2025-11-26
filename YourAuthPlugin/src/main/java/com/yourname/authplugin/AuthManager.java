package com.yourname.authplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuthManager {
    
    private final Main plugin;
    private final FileConfiguration playersConfig;
    private final File playersFile;
    private final Set<UUID> loggedInPlayers;
    private final Set<UUID> registeredPlayers;
    
    public AuthManager(Main plugin, FileConfiguration playersConfig, File playersFile) {
        this.plugin = plugin;
        this.playersConfig = playersConfig;
        this.playersFile = playersFile;
        this.loggedInPlayers = new HashSet<>();
        this.registeredPlayers = new HashSet<>();
        
        // لود کاربران ثبت‌نام کرده از فایل
        loadRegisteredPlayers();
    }
    
    private void loadRegisteredPlayers() {
        if (playersConfig.contains("players")) {
            for (String uuidStr : playersConfig.getConfigurationSection("players").getKeys(false)) {
                UUID uuid = UUID.fromString(uuidStr);
                registeredPlayers.add(uuid);
            }
        }
    }
    
    public boolean isPlayerRegistered(Player player) {
        return registeredPlayers.contains(player.getUniqueId());
    }
    
    public boolean isPlayerLoggedIn(Player player) {
        return loggedInPlayers.contains(player.getUniqueId());
    }
    
    public boolean registerPlayer(Player player, String password) {
        UUID uuid = player.getUniqueId();
        
        if (isPlayerRegistered(player)) {
            player.sendMessage(ChatColor.RED + "شما قبلاً ثبت‌نام کرده‌اید!");
            return false;
        }
        
        // هش کردن پسورد با BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        // ذخیره در فایل
        playersConfig.set("players." + uuid + ".password", hashedPassword);
        playersConfig.set("players." + uuid + ".username", player.getName());
        playersConfig.set("players." + uuid + ".first-join", System.currentTimeMillis());
        
        plugin.savePlayersConfig();
        registeredPlayers.add(uuid);
        loggedInPlayers.add(uuid); // بعد از ثبت‌نام، اتوماتیک لاگین میشه
        
        player.sendMessage(ChatColor.GREEN + "ثبت‌نام موفق! اکنون وارد شدید.");
        return true;
    }
    
    public boolean loginPlayer(Player player, String password) {
        if (!isPlayerRegistered(player)) {
            player.sendMessage(ChatColor.RED + "شما ثبت‌نام نکرده‌اید! از /register استفاده کنید.");
            return false;
        }
        
        if (isPlayerLoggedIn(player)) {
            player.sendMessage(ChatColor.YELLOW + "شما قبلاً وارد شده‌اید!");
            return true;
        }
        
        String storedHash = playersConfig.getString("players." + player.getUniqueId() + ".password");
        
        if (BCrypt.checkpw(password, storedHash)) {
            loggedInPlayers.add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "ورود موفق! خوش آمدید.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "پسورد اشتباه است!");
            return false;
        }
    }
    
    public void logoutPlayer(Player player) {
        loggedInPlayers.remove(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "شما از حساب کاربری خارج شدید.");
        player.kickPlayer(ChatColor.RED + "شما از حساب کاربری خارج شدید.\nدوباره وارد سرور شوید.");
    }
    
    public Set<UUID> getLoggedInPlayers() {
        return loggedInPlayers;
    }
    
    public Set<UUID> getRegisteredPlayers() {
        return registeredPlayers;
    }
}