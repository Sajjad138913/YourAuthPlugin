package com.yourname.authplugin.listeners;

import com.yourname.authplugin.AuthManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;

public class AuthListener implements Listener {
    
    private final AuthManager authManager;
    
    public AuthListener(AuthManager authManager) {
        this.authManager = authManager;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (!authManager.isPlayerRegistered(player)) {
            player.sendMessage(ChatColor.YELLOW + "برای بازی کردن باید ثبت‌نام کنید!");
            player.sendMessage(ChatColor.YELLOW + "استفاده: /register <پسورد> <تکرار پسورد>");
        } else {
            player.sendMessage(ChatColor.YELLOW + "برای بازی کردن باید وارد شوید!");
            player.sendMessage(ChatColor.YELLOW + "استفاده: /login <پسورد>");
        }
        
        // مخفی کردن پیام join اصلی
        event.setJoinMessage(null);
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // مخفی کردن پیام quit اصلی
        event.setQuitMessage(null);
        
        // حذف از لیست لاگین‌ها (اختیاری)
        authManager.getLoggedInPlayers().remove(event.getPlayer().getUniqueId());
    }
    
    // جلوگیری از فعالیت‌ها تا زمانی که لاگین نکرده
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!authManager.isPlayerLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "برای این کار باید وارد شوید!");
        }
    }
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!authManager.isPlayerLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "برای این کار باید وارد شوید!");
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!authManager.isPlayerLoggedIn(event.getPlayer())) {
            // فقط اجازه حرکت کوچک رو میده (برای جلوگیری از افتادن)
            if (event.getFrom().distance(event.getTo()) > 1) {
                event.setTo(event.getFrom());
            }
        }
    }
    
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!authManager.isPlayerLoggedIn(event.getPlayer())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "برای چت کردن باید وارد شوید!");
        }
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!authManager.isPlayerLoggedIn(player)) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (!authManager.isPlayerLoggedIn(player)) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "برای این کار باید وارد شوید!");
            }
        }
    }
}