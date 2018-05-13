package com.viste.realisticarmortiers.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventEquipmentSetsClearActivePotion {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
    	if(event.player instanceof EntityPlayerMP){
    		   event.player.clearActivePotions();
		}
    }
}
