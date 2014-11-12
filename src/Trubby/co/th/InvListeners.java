package Trubby.co.th;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import Trubby.co.th.Util.Utils;

public class InvListeners implements Listener{
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getClickedBlock().getType() == Material.ANVIL){
				e.setCancelled(true);
				Methods.openFurnace(e.getPlayer());
			}
		}
	}
	
	@EventHandler
	public void onClickItem(final InventoryClickEvent e){
		
		Utils.Debug((Player) e.getWhoClicked(), "click " + e.getSlot() + " " + e.getSlotType());
		if(e.getInventory().getName().startsWith(ChatColor.RED + "" + ChatColor.BOLD + "UPGRADE")){
			
			final Player p = (Player) e.getWhoClicked();
			
			if(Upgrade.upgrading.contains(p.getName())){
				e.setCancelled(true);
				return;
			}
		
			
			if(e.getSlotType() == SlotType.RESULT){
				
				e.setCancelled(true);
				e.setCursor(new ItemStack(Material.AIR));
				
				//not empty
				if(e.getInventory().getItem(0) == null || e.getInventory().getItem(1) == null)return;
				
				//player not in upgrading
				if(Upgrade.upgrading.contains(p.getName()))return;
				
				//real weapon & upgrade material
				if(isUpgradeable(e.getInventory().getItem(0)) && isUpgradeMat(e.getInventory().getItem(1))){
					
					ItemStack mat = e.getInventory().getItem(1);
					int matAmount = mat.getAmount();
					if(matAmount > 1){
						mat.setAmount(matAmount - 1);
						p.getInventory().addItem(mat);
						mat.setAmount(1);
						e.getInventory().setItem(1, mat);
					}
					
					Furnace furnace = (Furnace) e.getInventory().getHolder();
					
					/** UPGRADE TASK **/
					@SuppressWarnings("unused")
					BukkitTask task = new EffectTask(furnace, e.getInventory(), p).runTaskTimer(Upgrade.instance, 2, 2);
					
					p.playSound(p.getLocation(), Sound.ANVIL_USE, 0.6f, 1f);
					
					Upgrade.upgrading.add(p.getName());
					
				}else{
					p.sendMessage("failed.");
				}
			}
			
			else if(e.getSlotType() == SlotType.CRAFTING || e.getSlotType() == SlotType.FUEL){
				
				if(Upgrade.upgrading.contains(p.getName())){e.setCancelled(true); e.setCursor(new ItemStack(Material.AIR)); return;}
				
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
	
	@EventHandler
	public void onCloseInv(InventoryCloseEvent e){
		Player p = (Player) e.getPlayer();
		if(e.getInventory().getName().startsWith(ChatColor.RED + "" + ChatColor.BOLD + "UPGRADE")){
			
			if(Upgrade.upgrading.contains(p.getName())){
				return;
			}
			//return item slot 0
			if(e.getInventory().getItem(0) != null){
				p.getWorld().dropItem(p.getEyeLocation(), e.getInventory().getItem(0));
			}
			//return item slot 1
			if(e.getInventory().getItem(1) != null){
				p.getWorld().dropItem(p.getEyeLocation(), e.getInventory().getItem(1));
			}
			
		}
	}
	
	public boolean isUpgradeable(ItemStack is){
		if(!(is.getType() == Material.WOOD_SWORD || is.getType() == Material.GOLD_SWORD || is.getType() == Material.IRON_SWORD || is.getType() == Material.DIAMOND_SWORD))return false;
		
		if(is.hasItemMeta()){
			ItemMeta im = is.getItemMeta();
			if(im.hasLore()){
				for(String s : im.getLore()){
					if(s.startsWith(ChatColor.GRAY + "Damage")){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isUpgradeMat(ItemStack is){
		if(is.getType() == Material.EMERALD){
			return true;
		}
		
		return false;
	}

}
