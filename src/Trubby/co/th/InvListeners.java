package Trubby.co.th;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.scheduler.BukkitTask;

public class InvListeners implements Listener{
	
	@EventHandler
	public void onClickItem(final InventoryClickEvent e){
		
		Utils.Debug((Player) e.getWhoClicked(), "click " + e.getSlot() + " " + e.getSlotType());
		if(e.getInventory().getName().startsWith(ChatColor.RED + "" + ChatColor.BOLD + "UPGRADE")){
			
			final Player p = (Player) e.getWhoClicked();
			
			if(e.getSlotType() == SlotType.RESULT){
				
				e.setCancelled(true);
				
				//not empty
				if(e.getInventory().getItem(0) == null || e.getInventory().getItem(1) == null)return;
				
				//not upgrading
				if(Upgrade.upgrading.contains(p.getName()))return;
				
				//real iron and emerald
				if(e.getInventory().getItem(0).getType() == Material.IRON_SWORD && e.getInventory().getItem(1).getType() == Material.EMERALD){
					
					Furnace furnace = (Furnace) e.getInventory().getHolder();
					@SuppressWarnings("unused")
					BukkitTask task = new EffectTask(furnace, e.getInventory(), p).runTaskTimer(Upgrade.instance, 2, 2);
					
					Upgrade.upgrading.add(p.getName());
					
				}else{
					p.sendMessage("failed.");
				}
			}
			
			else if(e.getSlotType() == SlotType.CRAFTING || e.getSlotType() == SlotType.FUEL){
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Upgrade.instance, new Runnable() {
					@Override
					public void run() {
						Methods.checkReady(e.getInventory(), p);
					}
				}, 2L);
				
			}
			
		}else{
			return;
		}
	}

}
