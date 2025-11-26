package com.yourname.authplugin.commands;

import com.yourname.authplugin.AuthManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
    
    private final AuthManager authManager;
    
    public RegisterCommand(AuthManager authManager) {
        this.authManager = authManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "این دستور فقط برای بازیکنان است!");
            return true;
        }
        
        Player player = (Player) sender;
        
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "استفاده: /register <پسورد> <تکرار پسورد>");
            return true;
        }
        
        String password = args[0];
        String confirmPassword = args[1];
        
        if (!password.equals(confirmPassword)) {
            player.sendMessage(ChatColor.RED + "پسوردها مطابقت ندارند!");
            return true;
        }
        
        if (password.length() < 4) {
            player.sendMessage(ChatColor.RED + "پسورد باید حداقل ۴ کاراکتر باشد!");
            return true;
        }
        
        authManager.registerPlayer(player, password);
        return true;
    }
}