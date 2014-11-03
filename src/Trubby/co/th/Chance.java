package Trubby.co.th;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;

public class Chance {

	ArrayList<Integer> chance = new ArrayList<>();
	Random ran = new Random();
	
	Chance(){
		/*1*/ chance.add(100);
		/*2*/ chance.add(100);
		/*3*/ chance.add(80);
		/*4*/ chance.add(80);
		/*5*/ chance.add(70);
		/*6*/ chance.add(60);
		/*7*/ chance.add(50);
		/*8*/ chance.add(40);
		/*9*/ chance.add(30);
		/*10*/ chance.add(20);
		/*11*/ chance.add(10);
		/*12*/ chance.add(5);
		/*13*/ chance.add(5);
		/*14*/ chance.add(2);
		/*15*/ chance.add(1);
	}
	
	public boolean getChance(int level){
		
		int a = chance.get(level);
		
		if(ran.nextInt(100) <= chance.get(level)){
			return true;
		}else{
			return false;
		}
	}
	
}
