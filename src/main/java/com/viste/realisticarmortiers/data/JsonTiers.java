package com.viste.realisticarmortiers.data;

import java.util.List;

public class JsonTiers {
	public String name;
	public float speed;
	public List<String> pieces;
	public List<String> helmet;
	public List<String> chestplate;
	public List<String> leggings;
	public List<String> boots;
	
	public JsonTiers(String name, float speed, List<String> helmet, List<String> chestplate, List<String> leggings, List<String> boots) {
		this.name = name;
		this.speed = speed;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggings = leggings;
		this.boots = boots;
	}
}
