package Trubby.co.th;

import org.bukkit.entity.Player;

public class Utils {

	public static void Debug(Player p, String s){
		if(Upgrade.instance.debug_mode){
			p.sendMessage(s);
		}
	}
	
}
