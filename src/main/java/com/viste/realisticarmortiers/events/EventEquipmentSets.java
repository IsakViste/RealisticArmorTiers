package com.viste.realisticarmortiers.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.viste.realisticarmortiers.RealisticArmorTiers;
import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.common.collect.Lists;
import com.viste.realisticarmortiers.capability.ArmorProvider;
import com.viste.realisticarmortiers.capability.IArmor;
import com.viste.realisticarmortiers.data.EquipmentSetsParser;
import com.viste.realisticarmortiers.logic.Equiped;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class EventEquipmentSets {
	private final EquipmentSetsParser sets;

	public EventEquipmentSets() {
		sets = new EquipmentSetsParser();
	}

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent evt) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		List<ServerPlayerEntity> playerList = Lists.newArrayList(server.getPlayerList().getPlayers());
		IArmor armors = null;
		List<PotionEffect> setEffects = new ArrayList<>();
		boolean foundWhole = false;
		for (ServerPlayerEntity serverPlayerEntity : playerList) {
			int m;
			List<ItemStack> stacks;

			if (serverPlayerEntity.getCapability(ArmorProvider.Armor).isPresent()) {
				armors = (IArmor) serverPlayerEntity.getCapability(ArmorProvider.Armor);
				stacks = (List<ItemStack>) serverPlayerEntity.getArmorSlots();
				int numberOfStack = 0;
				for (ItemStack stack : stacks) {
					if (!stack.isEmpty()) {
						numberOfStack++;
					}
				}
				if (numberOfStack == armors.getItems().size()) {
					foundWhole = true;
					for (ItemStack stack : stacks) {
						boolean found = false;
						if (!stack.isEmpty()) {
							for (int l = 0; l < armors.getItems().size(); l++) {
								if (stack.getItem().equals(armors.getItems().get(l).getItem())) {
									found = true;
									break;
								}
							}
							if (!found) {
								foundWhole = false;
								break;
							}
						}
					}
				}
			}
			if (foundWhole) {
				setEffects = armors.getPotionEffects();
			} else {
				if (serverPlayerEntity.getCapability(ArmorProvider.Armor).isPresent()) {
					if(armors != null) {
						m = 0;
						setEffects = armors.getPotionEffects();
						while (m < setEffects.size()) {
							RegistryObject<Effect> potionEffect = RegistryObject.of(new ResourceLocation(setEffects.get(m).id), ForgeRegistries.POTIONS);
							if (potionEffect.isPresent()) {
								serverPlayerEntity.removeEffect(potionEffect.get());
							}
							m++;
						}
						armors.removeAllItems();
						Collection<EffectInstance> potionEffectsPlayer = serverPlayerEntity.getActiveEffects();
						Iterator<EffectInstance> potionEffects = potionEffectsPlayer.iterator();
						while (potionEffects.hasNext()) {
							EffectInstance o = potionEffects.next();
							ResourceLocation effectResLoc = o.getEffect().getRegistryName();
							if(effectResLoc == null) {
								RealisticArmorTiers.LOGGER.warn("Could not find ResourceLocation of " + o.getDescriptionId());
								continue;
							}
							PotionEffect usedPotion = new PotionEffect(effectResLoc.getNamespace() + ":" + o.getEffect().getRegistryName().getPath(), o.getAmplifier(), o.getDuration());

							armors.addUsedPotionEffect(usedPotion);
							potionEffects.remove();
						}

						stacks = (List<ItemStack>) serverPlayerEntity.getArmorSlots();
						for (ItemStack stack : stacks) {
							if(!stack.isEmpty()) {
								armors.addItem(stack.copy());
							}
						}
					}
				} else {
					serverPlayerEntity.removeAllEffects();
				}

				int setNumber = sets.armors.checkIfSet(serverPlayerEntity);
				if (setNumber != -1) {
					setEffects = sets.armors.getPotionEffects(setNumber);
				}

				if (serverPlayerEntity.getCapability(ArmorProvider.Armor).isPresent()) {
					if (setEffects != null && armors != null) {
						armors.addPotionEffectList(setEffects);
					}
				}
			}

			if (armors != null) {
				if (setEffects != null) {
					m = 0;
					while (m < setEffects.size()) {
						Equiped.addPotionEffect(serverPlayerEntity, armors.getPotionEffects().get(m));
						m++;
					}
				}

				if (!foundWhole) {
					setEffects = armors.getUsedPotionEffects();
					Equiped.addUsedPotionEffect(serverPlayerEntity, setEffects, armors);
				}
			}
		}
	}
}
