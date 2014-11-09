package Trubby.co.th.Util;

import org.bukkit.entity.Player;

import Trubby.co.th.Upgrade;

public class Utils {

	public static void Debug(Player p, String s){
		if(Upgrade.instance.debug_mode){
			p.sendMessage(s);
		}
	}
	
}
