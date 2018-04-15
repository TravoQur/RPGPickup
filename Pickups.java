package ru.meedway.pickup;

import java.util.HashSet;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class Pickups extends JavaPlugin implements Listener
{
    Set<String> drop = new HashSet<>();
    public static FileConfiguration c;
    
    
    @Override
    public void onEnable(){
    Bukkit.getPluginManager().registerEvents(this, this);
        c = getConfig();
        c.options().copyDefaults(true);
        saveConfig();
    }
    
    @Override
    public void onDisable(){
    saveConfig();
    }
    
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] a){
    Player p = (Player)s;
    if(cmd.getName().equalsIgnoreCase("rpgpickupreload")){
      if(p.hasPermission("rpgdrop.reload")){
          this.reloadConfig();
          }else{
          p.sendMessage(ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("NoPermissions")));
          }
        }
        return false;
    }
    
    
    
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent e){
       Player p = e.getPlayer();
       ItemStack is = e.getItem().getItemStack();
       e.setCancelled(true);
       boolean checkdrop = drop.contains(p.getName());
            if (!checkdrop) {
                drop.add(p.getName());
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){

                    @Override
                    public void run() {
                        int i = e.getItem().getItemStack().getAmount();
                            if(i <= 1){
                                e.getItem().remove();
                                ItemStack items = new ItemStack(e.getItem().getItemStack());
                                items.setAmount(1);
                                p.getInventory().addItem(items);
                                drop.remove(p.getName());
                                e.setCancelled(true);
                            }else if(i > 1){
                                e.getItem().getItemStack().setAmount(i-1);
                                ItemStack items = new ItemStack(e.getItem().getItemStack());
                                items.setAmount(1);
                                p.getInventory().addItem(items);
                                drop.remove(p.getName());
                                e.setCancelled(true);
                            }
                    }
                }, getConfig().getInt("Cooldown"));
            } else {
                e.setCancelled(true);
            }
    }
    
    
}
