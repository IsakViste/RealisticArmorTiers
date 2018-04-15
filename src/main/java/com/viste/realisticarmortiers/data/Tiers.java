package com.viste.realisticarmortiers.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;

public class Tiers {	
	private List<Tier> tiers = new ArrayList<Tier>();
		
	public float checkIfTier(EntityPlayerMP player) {
		float speed = 0;
		for(int i = 0; i < tiers.size(); i++) {
			speed = speed + tiers.get(i).getSpeed(player);
		}
		return speed;
	}
	
	public void addTier(Tier tier) {
		this.tiers.add(tier);
	}
}