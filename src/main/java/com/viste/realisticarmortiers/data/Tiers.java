package com.viste.realisticarmortiers.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

public class Tiers {
	
	private List<Armor> armors = new ArrayList<Armor>();
		
	public float checkIfTier(EntityPlayerMP player) {
		for(int i = 0; i < armors.size(); i++) {
			if(armors.get(i).isFullSet(player)) {
				return armors.get(i).getSpeed();
			}
		}
		return -1;
	}
	
	public void addTier(Armor armor) {
		this.armors.add(armor);
	}
}
