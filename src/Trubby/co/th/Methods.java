package Trubby.co.th;

import java.util.List;

import net.minecraft.server.v1_7_R4.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Methods {
	
	public static void openFurnace(Player p){
		EntityPlayer player = ((CraftPlayer) p).getHandle();
		EntityFurnace furnace = new EntityFurnace(player);
		
		//name
		furnace.a(ChatColor.RED + "" + ChatColor.BOLD + "UPGRADE " + ChatColor.BLACK + "WEAPON/ARMOR");
		
		//open
		player.openFurnace(furnace);
	}
	
	@SuppressWarnings("deprecation")
	public static void checkReady(final Inventory inv, final Player p){
		
		//if NULL
		if(inv.getItem(0) == null || inv.getItem(1) == null){
			
			if(inv.getItem(2) != null){
				inv.setItem(2, new ItemStack(Material.AIR));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Upgrade.instance, new Runnable() {
					@Override
					public void run() {
						p.updateInventory();
						applyBurn(inv, false);
					}
				}, 2L);
			}
			
			Utils.Debug(p, "null");
			return;
		}
		
		if(inv.getItem(0).getType() == Material.IRON_SWORD && inv.getItem(1).getType() == Material.EMERALD){
			
			inv.setItem(2, new ItemStack(Upgrade.instance.accept_anvil));
			Utils.Debug(p, "set");
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Upgrade.instance, new Runnable() {
				@Override
				public void run() {
					p.updateInventory();
					applyBurn(inv, true);
				}
			}, 2L);
			
		}else{
			if(inv.getItem(2) != null){
				inv.setItem(2, new ItemStack(Material.AIR));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Upgrade.instance, new Runnable() {
					@Override
					public void run() {
						p.updateInventory();
						applyBurn(inv, false);
					}
				}, 2L);
			}
			Utils.Debug(p, "delete");
		}
		
		return;
	}
	
	public static void applyBurn(Inventory inv, boolean doFire){
		Furnace fur = (Furnace) inv.getHolder();
		if(doFire){
			fur.setBurnTime(Short.MAX_VALUE);
		}else{
			fur.setBurnTime((short) 0);
		}
		
	}
	
	public static ItemStack upgrade(Inventory inv, Player p){
		
		ItemStack is = inv.getItem(0);
		if(is.hasItemMeta()){
			if(is.getItemMeta().hasLore()){
				
				//Check old upgrade
				int oldUp = 1;
				
				if(is.getItemMeta().getDisplayName().contains("+")){
					oldUp = oldUp + Integer.parseInt(is.getItemMeta().getDisplayName().split(ChatColor.YELLOW + "")[1].replace("+", "").replace(" ", ""));
				}
				
				
				/** FAILED UPGRADE **/
				if(!Upgrade.chance.getChance(oldUp)){
					
					p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.7f, 0.7f);
					p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
					
					p.sendMessage(ChatColor.RED + "FAILED UPGRADE! " + is.getItemMeta().getDisplayName());
					return null;
				}
				
				
				/** SUCCESSFUL UPGRADE **/
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
				
				p.playSound(p.getLocation(), Sound.LEVEL_UP, 1f, 1f);
				p.playSound(p.getLocation(), Sound.ANVIL_LAND, 0.7f, 1f);
				
				p.sendMessage(ChatColor.GREEN + "SUCCESSFUL UPGRADE! " + is.getItemMeta().getDisplayName());
				p.sendMessage(ChatColor.GRAY + "(" + min + "-" + max + ") " + "==>" + " " + ChatColor.WHITE + "(" + amin + "-" + amax + ")" );
				//Give back to player
				p.getInventory().addItem(is);
				
			}
		}
		return null;
	}
	
	public String getColor(int level){
		if(level <= 3){
			return ChatColor.WHITE + "";
		}else if(level <= 6){
			return ChatColor.YELLOW + "";
		}else if(level <= 9){
			return ChatColor.GOLD + "";
		}else{
			return ChatColor.RED + "";
		}
	}
	
}
