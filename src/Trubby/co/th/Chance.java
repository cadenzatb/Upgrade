package Trubby.co.th;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;

public class Chance {

	ArrayList<Short> chance = new ArrayList<>();
	Random ran = new Random();
	
	Chance(){
		/*1*/ chance.add((short) 100);
		/*2*/ chance.add((short) 100);
		/*3*/ chance.add((short) 80);
		/*4*/ chance.add((short) 80);
		/*5*/ chance.add((short) 70);
		/*6*/ chance.add((short) 60);
		/*7*/ chance.add((short) 50);
		/*8*/ chance.add((short) 40);
		/*9*/ chance.add((short) 30);
		/*10*/ chance.add((short) 20);
		/*11*/ chance.add((short) 10);
		/*12*/ chance.add((short) 5);
		/*13*/ chance.add((short) 5);
		/*14*/ chance.add((short) 2);
		/*15*/ chance.add((short) 1);
	}
	
	public boolean getChance(int level){
		
		int a = chance.get(level);
		Bukkit.broadcastMessage(""+a);
		
		if(ran.nextInt(100) <= chance.get(level)){
			return true;
		}else{
			return false;
		}
	}
	
}
