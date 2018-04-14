package com.viste.realisticarmortiers.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

public class Armors {
	
	private List<Armor> armors = new ArrayList<Armor>();
		
	public int checkIfSet(EntityPlayerMP player) {
		for(int i = 0; i < armors.size(); i++) {
			if(armors.get(i).isFullSet(player)) {
				return i;
			}
		}
		return -1;
	}
	
	public List<Potion> getPotions(int found) {
		if(found >= 0) {
			return armors.get(found).getPotions();
		}
		return null;
	}
	
	public void addArmor(Armor armor) {
		this.armors.add(armor);
	}
	
	
}
