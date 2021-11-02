package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ArmorHandler {

	 public static final ResourceLocation Armor = new ResourceLocation(Reference.MODID, "armor");

	 @SubscribeEvent
	 public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof ServerPlayerEntity)
				event.addCapability(Armor, new ArmorProvider());
	 }
}
