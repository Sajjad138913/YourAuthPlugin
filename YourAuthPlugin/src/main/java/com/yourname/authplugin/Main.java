package com.yourname.authplugin;

import com.yourname.authplugin.commands.LoginCommand;
import com.yourname.authplugin.commands.RegisterCommand;
import com.yourname.authplugin.listeners.AuthListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Main extends JavaPlugin {
    
    private AuthManager authManager;
    private File playersFile;
    private FileConfiguration playersConfig;
    
    @Override
    public void onEnable() {
        // ایجاد فایل کانفیگ
        setupConfig();
        
        // مقداردهی منیجر
        authManager = new AuthManager(this, playersConfig, playersFile);
        
        // ثبت ایونت‌ها
        getServer().getPluginManager().registerEvents(new AuthListener(authManager), this);
        
        // ثبت کامندها
        getCommand("login").setExecutor(new LoginCommand(authManager));
        getCommand("register").setExecutor(new RegisterCommand(authManager));
        getCommand("logout").setExecutor(new LoginCommand(authManager));
        
        getLogger().info("§aAuthPlugin فعال شد!");
    }
    
    private void setupConfig() {
        saveDefaultConfig();
        
        playersFile = new File(getDataFolder(), "players.yml");
        if (!playersFile.exists()) {
            playersFile.getParentFile().mkdirs();
            saveResource("players.yml", false);
        }
        playersConfig = YamlConfiguration.loadConfiguration(playersFile);
    }
    
    public void savePlayersConfig() {
        try {
            playersConfig.save(playersFile);
        } catch (IOException e) {
            getLogger().severe("خطا در ذخیره فایل players.yml: " + e.getMessage());
        }
    }
    
    @Override
    public void onDisable() {
        savePlayersConfig();
        getLogger().info("§cAuthPlugin غیرفعال شد!");
    }
}