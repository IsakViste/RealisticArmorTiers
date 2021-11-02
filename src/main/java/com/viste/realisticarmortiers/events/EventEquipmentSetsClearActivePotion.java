package com.viste.realisticarmortiers.events;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;

public class EventEquipmentSetsClearActivePotion {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
    	if(event.getPlayer() instanceof ServerPlayerEntity){
    		   event.getPlayer().removeAllEffects();
		}
    }
}
