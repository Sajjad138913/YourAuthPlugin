package com.yourname.authplugin.commands;

import com.yourname.authplugin.AuthManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
    
    private final AuthManager authManager;
    
    public LoginCommand(AuthManager authManager) {
        this.authManager = authManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "این دستور فقط برای بازیکنان است!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (label.equalsIgnoreCase("logout")) {
            authManager.logoutPlayer(player);
            return true;
        }
        
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "استفاده: /login <پسورد>");
            return true;
        }
        
        String password = args[0];
        authManager.loginPlayer(player, password);
        return true;
    }
}