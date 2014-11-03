package Trubby.co.th;

import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

public class EffectTask extends BukkitRunnable{
	
	short count = 1;
	Furnace fur;
	Inventory inv;
	Player p;
	
	public EffectTask(Furnace furnace, Inventory inv, Player p){
		this.fur = furnace;
		this.inv = inv;
		this.p = p;
	}
	
	@Override
	public void run() {
		if(this.count < 20){
		fur.setCookTime((short) (this.count *10));
		this.count = (short) (this.count + 1);
		
		}else{
			Methods.upgrade(inv, p);
			p.closeInventory();
			
			Upgrade.upgrading.remove(p.getName());
			this.cancel();
		}
		
	}
	//BukkitTask task = new ArrowShowerTask(1,p).runTaskTimer(MMORPG.instance, 3, 3);
}
