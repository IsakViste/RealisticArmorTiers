package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.Reference;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorHandler {

	 public static final ResourceLocation Armor = new ResourceLocation(Reference.MODID, "armor");

	 @SubscribeEvent
	 public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof EntityPlayerMP)
				event.addCapability(Armor, new ArmorProvider());
	 }
}
