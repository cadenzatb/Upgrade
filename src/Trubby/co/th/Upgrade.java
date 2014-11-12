package Trubby.co.th;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import Trubby.co.th.Util.Chance;

public class Upgrade extends JavaPlugin implements Listener{

	public static Upgrade instance;
	public static Chance chance;
	public boolean debug_mode = false;
	public ItemStack accept_anvil = new ItemStack(Material.ANVIL);
	
	public static ArrayList<String> upgrading = new ArrayList<>();
	
	/**
	 *	 >>>>>>>>	ENABLE	 <<<<<<<<
	 */
	
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new InvListeners(), this);
		
		ItemMeta im = accept_anvil.getItemMeta();
		im.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "UPGRADE" + ChatColor.WHITE + " (Click)");
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Are you sure?");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+1 > 100%");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+2 > 100%");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+3 > 80%");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+4 > 80%");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+5 > 70%");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+6 > 60%");
		lore.add(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "+7 > 50%");
		im.setLore(lore);
		accept_anvil.setItemMeta(im);
		
		instance = this;
		chance = new Chance();
	}
	
	/**
	 *	 >>>>>>>>	DISABLE   <<<<<<<<
	 */
	
	@SuppressWarnings("deprecation")
	@Override
	public void onDisable() {
		for(Player p : Bukkit.getOnlinePlayers()){
			p.closeInventory();
		}
	}
	
	
	/**
	 * 	>>>>>>>>	COMMANDS	 <<<<<<<<
	 */
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(sender instanceof Player){
			if(label.equalsIgnoreCase("up")){
				
				Player p = (Player) sender;
				
				p.sendMessage( "Right click Anvil to open upgrading menu.");
			}else if(label.equalsIgnoreCase("upgrade_debug")){
				
				Player p = (Player) sender;
				
				if(debug_mode == false){
					debug_mode = true;
					p.sendMessage("Debug-mode enable.");
				}else{
					debug_mode = false;
					p.sendMessage("Debug-mode disable.");
				}
					
			}else if(label.equalsIgnoreCase("test")){
				
				Player p = (Player) sender;
				
				if(p.getItemInHand().getItemMeta().getDisplayName().contains("+")){
					p.sendMessage("true");
					int i = p.getItemInHand().getItemMeta().getDisplayName().indexOf("+");
					p.sendMessage(""+i);
					p.sendMessage("" + p.getItemInHand().getItemMeta().getDisplayName().charAt(i+2));
				}
			}
		}
		return false;
	}
}
