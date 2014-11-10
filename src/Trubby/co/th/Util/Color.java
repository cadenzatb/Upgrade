package Trubby.co.th.Util;

import org.bukkit.ChatColor;

public class Color {
	
	public static ChatColor getColor(int i){
		
		if(i <= 5){
			return ChatColor.YELLOW;
		}else if(i <= 8){
			return ChatColor.GOLD;
		}else if(i <= 10){
			return ChatColor.RED;
		}else{
			return ChatColor.ITALIC;
		}
		
	}

}
