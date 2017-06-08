package com.viste.realisticarmortiers;

public class Reference {
	
	public static final String MODID = "realisticarmortiers";
	public static final String NAME = "Realistic Armor Tiers";
	public static final String VERSION = "1.0.2";
	
	public static final String CLIENT_PROXY = "com.viste.realisticarmortiers.proxy.ClientProxy";
	public static final String SERVER_PROXY = "com.viste.realisticarmortiers.proxy.CommonProxy";
	
	public static final String CONFIG_PATH = "/RealisticArmorTiers/";
	
	public static final String JSON_TIERS_FILE = "equipment_tiers.json";
	public static final String ASSET_TIERS_PATH = "/assets/" + MODID + "/" + JSON_TIERS_FILE;	
	public static final String JSON_CONFIG_TIERS_PATH = CONFIG_PATH + JSON_TIERS_FILE;
	
	public static final String JSON_SETS_FILE = "equipment_sets.json";
	public static final String ASSET_SETS_PATH = "/assets/" + MODID + "/" + JSON_SETS_FILE;
	public static final String JSON_CONFIG_SETS_PATH = CONFIG_PATH + JSON_SETS_FILE;
}
