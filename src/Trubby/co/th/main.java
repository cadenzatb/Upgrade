package Trubby.co.th;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{

	public boolean debug_mode = false;
	public ItemStack accept_anvil = new ItemStack(Material.ANVIL);
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		
		ItemMeta im = accept_anvil.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "UPGRADE ACCEPT" + ChatColor.GRAY + " (Click)");
		accept_anvil.setItemMeta(im);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(sender instanceof Player){
			if(label.equalsIgnoreCase("up")){
				
				Player p = (Player) sender;
				
				/*Inventory inv = Bukkit.createInventory(p, 9, "Armor/Weapon            Upgrade");
				inv.setItem(0, new ItemStack(Material.ITEM_FRAME));
				inv.setItem(2, new ItemStack(Material.ITEM_FRAME));
				
				inv.setItem(3, new ItemStack(Material.EMERALD_BLOCK));
				inv.setItem(5, new ItemStack(Material.REDSTONE_BLOCK));
				
				inv.setItem(6, new ItemStack(Material.ITEM_FRAME));
				inv.setItem(8, new ItemStack(Material.ITEM_FRAME));
				
				p.openInventory(inv);*/
				
				Inventory inv = Bukkit.getServer().createInventory(null, InventoryType.FURNACE, ChatColor.RED + "" + ChatColor.BOLD + "UPGRADE " + ChatColor.BLACK + "WEAPON/ARMOR");
				p.openInventory(inv);
			}else if(label.equalsIgnoreCase("upgrade_debug")){
				
				Player p = (Player) sender;
				
				if(debug_mode == false){
					debug_mode = true;
					p.sendMessage("Debug-mode enable.");
				}else{
					debug_mode = false;
					p.sendMessage("Debug-mode disable.");
				}
					
			}
		}
		return false;
	}
	
	@EventHandler
	public void onClickItem(final InventoryClickEvent e){
		
		debug((Player) e.getWhoClicked(), "click " + e.getSlot() + " " + e.getSlotType());
		
		if(e.getInventory().getName().startsWith(ChatColor.RED + "" + ChatColor.BOLD + "UPGRADE")){
			
			final Player p = (Player) e.getWhoClicked();
			
			/*if(e.getInventory().getType() == InventoryType.CHEST){
				e.setCancelled(true);
			}*/
			
			if(e.getSlotType() == SlotType.RESULT){
				
				e.setCancelled(true);
				
				if(e.getInventory().getItem(0) == null || e.getInventory().getItem(1) == null){
					return;
				}
				
				if(e.getInventory().getItem(0).getType() == Material.IRON_SWORD && e.getInventory().getItem(1).getType() == Material.EMERALD){
					ItemStack is = e.getInventory().getItem(0);
					if(is.hasItemMeta()){
						if(is.getItemMeta().hasLore()){
							
							//Check old upgrade
							int oldUp = 1;
							
							if(is.getItemMeta().getDisplayName().contains("+")){
								oldUp = oldUp + Integer.parseInt(is.getItemMeta().getDisplayName().split(ChatColor.YELLOW + "")[1].replace("+", "").replace(" ", ""));
								debug(p,oldUp+"");
							}
							
							//Get damage lore line
							String text = null;
							int time = -1;
							for(String lore : is.getItemMeta().getLore()){
								if(lore.startsWith(ChatColor.GRAY + "Damage")){
									text = ChatColor.stripColor(lore);
								}
								time++;
							}
							
							//Get min-max damage
							String damage = text.split(" ")[1];
							
							int min = Integer.parseInt(damage.split("-")[0]);
							int max = Integer.parseInt(damage.split("-")[1]);
							
							//Calculate new damage after upgrade
							int amin = min + (min/100 * 5);
							int amax = max + (max/100 * 5);
							
							//Set lore
							List<String> lore = is.getItemMeta().getLore();
							lore.set(time, ChatColor.GRAY + "Damage: " +ChatColor.WHITE + amin + "-" + amax);
							
							ItemMeta im = is.getItemMeta();
							im.setLore(lore);
							
							//Delete and Add +
							if(oldUp > 1){
								im.setDisplayName(im.getDisplayName().replace(ChatColor.YELLOW + " +" + (oldUp - 1), "")
										+ ChatColor.YELLOW + " +" + oldUp);
							}else{
								im.setDisplayName(im.getDisplayName() + ChatColor.YELLOW + " +" + oldUp);
							}
							
							//Done
							is.setItemMeta(im);
							p.playSound(p.getLocation(), Sound.ANVIL_USE, 1f, 1f);
							
							p.sendMessage(ChatColor.GREEN + "Successful Upgrade " + is.getItemMeta().getDisplayName());
							p.sendMessage(ChatColor.GRAY + "(" + min + "-" + max + ") " + "==>" + " " + ChatColor.WHITE + "(" + amin + "-" + amax + ")" );
							//Give back to player
							p.getInventory().addItem(is);
						}
					}
				}else{
					p.sendMessage("failed.");
				}
				p.closeInventory();
			}
			
			else if(e.getSlotType() == SlotType.CRAFTING || e.getSlotType() == SlotType.FUEL){
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						checkReady(e.getInventory(), p);
					}
				}, 2L);
				
			}
			
		}else{
			return;
		}
	}
	
	public void checkReady(Inventory inv, final Player p){
		
		if(inv.getItem(0) == null || inv.getItem(1) == null){
			
			if(inv.getItem(2) != null){
				inv.setItem(2, new ItemStack(Material.AIR));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						p.updateInventory();
					}
				}, 2L);
			}
			
			debug(p, "null");
			return;
		}
		
		if(inv.getItem(0).getType() == Material.IRON_SWORD && inv.getItem(1).getType() == Material.EMERALD){
			inv.setItem(2, new ItemStack(accept_anvil));
			//e.getInventory().addItem(new ItemStack(Material.ANVIL));
			debug(p, "set");
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				@Override
				public void run() {
					p.updateInventory();
				}
			}, 2L);
			
		}else{
			if(inv.getItem(2) != null){
				inv.setItem(2, new ItemStack(Material.AIR));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
					@Override
					public void run() {
						p.updateInventory();
					}
				}, 2L);
			}
			debug(p, "delete");
		}
		
		return;
	}
	
	public void debug(Player p, String s){
		if(debug_mode){
			p.sendMessage(s);
		}
	}
	
}
