package Trubby.co.th;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener{

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if(sender instanceof Player){
			if(label.equalsIgnoreCase("up")){
				
				Player p = (Player) sender;
				
				Inventory inv = Bukkit.createInventory(p, 9, "Armor/Weapon            Upgrade");
				inv.setItem(0, new ItemStack(Material.ITEM_FRAME));
				inv.setItem(2, new ItemStack(Material.ITEM_FRAME));
				
				inv.setItem(3, new ItemStack(Material.EMERALD_BLOCK));
				inv.setItem(5, new ItemStack(Material.REDSTONE_BLOCK));
				
				inv.setItem(6, new ItemStack(Material.ITEM_FRAME));
				inv.setItem(8, new ItemStack(Material.ITEM_FRAME));
				
				p.openInventory(inv);
			}
		}
		return false;
	}
	
	@EventHandler
	public void onClickItem(InventoryClickEvent e){
		
		if(e.getInventory().getName().startsWith("Armor/Weapon")){
			
			Player p = (Player) e.getWhoClicked();
			
			/*if(e.getInventory().getType() == InventoryType.CHEST){
				e.setCancelled(true);
			}*/
			
			if(e.getCurrentItem() == null){
				return;
			}
			
			if(e.getCurrentItem().getType() == Material.EMERALD_BLOCK){
				e.setCancelled(true);
				if(e.getInventory().getItem(1).getType() == Material.IRON_SWORD && e.getInventory().getItem(7).getType() == Material.EMERALD){
					ItemStack is = e.getInventory().getItem(1);
					if(is.hasItemMeta()){
						if(is.getItemMeta().hasLore()){
							
							//Check old upgrade
							int oldUp = 1;
							
							if(is.getItemMeta().getDisplayName().contains("+")){
								oldUp = oldUp + Integer.parseInt(is.getItemMeta().getDisplayName().split(ChatColor.YELLOW + "")[1].replace("+", "").replace(" ", ""));
								p.sendMessage(oldUp+"");
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
							
							p.sendMessage("" + min + "-" + max);
							
							//Calculate new damage after upgrade
							min = min + (min/100 * 5);
							max = max + (max/100 * 5);
							
							//Set lore
							List<String> lore = is.getItemMeta().getLore();
							lore.set(time, ChatColor.GRAY + "Damage: " +ChatColor.WHITE + min + "-" + max);
							
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
							
							p.sendMessage("Yes");
							//Give back to player
							p.getInventory().addItem(is);
						}
					}
				}else{
					p.sendMessage("No");
				}
				p.closeInventory();
			}
			
			else if(e.getCurrentItem().getType() == Material.REDSTONE_BLOCK){
				e.setCancelled(true);
				p.closeInventory();
			}
		
		}else{
			return;
		}
	}
}
