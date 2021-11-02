package com.viste.realisticarmortiers.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.ServerPlayerEntity;

public class Armors {
	
	private List<Armor> armors = new ArrayList<>();
		
	public int checkIfSet(ServerPlayerEntity player) {
		for(int i = 0; i < armors.size(); i++) {
			if(armors.get(i).isFullSet(player)) {
				return i;
			}
		}
		return -1;
	}
	
	public List<PotionEffect> getPotionEffects(int found) {
		if(found >= 0) {
			return armors.get(found).getPotionEffects();
		}
		return null;
	}
	
	public void addArmor(Armor armor) {
		this.armors.add(armor);
	}
	
	
}
