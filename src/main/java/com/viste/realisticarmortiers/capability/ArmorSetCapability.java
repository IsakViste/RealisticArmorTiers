package com.viste.realisticarmortiers.capability;

import com.viste.realisticarmortiers.data.PotionEffect;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class ArmorSetCapability {
    private String setID;
    private final List<PotionEffect> setEffects = new ArrayList<>();
    private final List<PotionEffect> usedPotionEffects = new ArrayList<>();

    public static ArmorSetCapability createADefaultInstance() {
        return new ArmorSetCapability();
    }

    public void clearAll() {
        this.clearSetID();
        this.removeAllSetEffects();
        this.removeAllUsedPotionEffects();
    }

    // Set ID
    public String getSetID() {
        return this.setID;
    }

    public void setSetID(String setID) {
        this.setID = setID;
    }

    public void clearSetID() {
        this.setID = "";
    }

    // Set Effects
    public List<PotionEffect> getSetEffects() {
        return this.setEffects;
    }

    public void addSetEffect(PotionEffect setEffect) {
        this.setEffects.add(setEffect);
    }

    public void addSetEffectList(List<PotionEffect> setEffects) {
        this.setEffects.addAll(setEffects);
    }

    public void removeSetEffect(PotionEffect setEffect) {
        this.setEffects.remove(setEffect);
    }

    public void removeAllSetEffects() {
        this.setEffects.clear();
    }

    // Used Potion Effects
    public List<PotionEffect> getUsedPotionEffects() {
        return this.usedPotionEffects;
    }

    public void addUsedPotionEffect(PotionEffect usedPotionEffect) {
        this.usedPotionEffects.add(usedPotionEffect);
    }

    public void removeUsedPotionEffect(PotionEffect usedPotionEffect) {
        this.usedPotionEffects.remove(usedPotionEffect);
    }

    public void removeAllUsedPotionEffects() {
        this.usedPotionEffects.clear();
    }


    /**
     * NBT Storage for the Armor Set Capability
     */
    public static class ArmorSetCapabilityNBTStorage implements Capability.IStorage<ArmorSetCapability> {
        @Override
        public INBT writeNBT(Capability<ArmorSetCapability> capability, ArmorSetCapability instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();

            // Add set ID of the instance to the NBT
            StringNBT setIDNBT = StringNBT.valueOf(instance.getSetID());

            // Add set effects of the instance to the NBT
            ListNBT setEffectsListNBT = new ListNBT();
            for(PotionEffect setEffect : instance.getSetEffects()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putString("effect", setEffect.getId());
                tag.putInt("amplifier", setEffect.getAmplifier());
                setEffectsListNBT.add(tag);
            }

            // Add used potion effects of the instance to the NBT
            ListNBT usedPotionEffectsListNBT = new ListNBT();
            for(PotionEffect usedPotionEffect : instance.getUsedPotionEffects()) {
                CompoundNBT tag = new CompoundNBT();
                tag.putString("effect", usedPotionEffect.getId());
                tag.putInt("amplifier", usedPotionEffect.getAmplifier());
                tag.putInt("duration", usedPotionEffect.getDuration());
                usedPotionEffectsListNBT.add(tag);
            }

            compoundNBT.put("setID", setIDNBT);
            compoundNBT.put("setEffects", setEffectsListNBT);
            compoundNBT.put("usedPotionEffects", usedPotionEffectsListNBT);

            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<ArmorSetCapability> capability, ArmorSetCapability instance, Direction side, INBT nbt) {
            if (nbt.getType() == CompoundNBT.TYPE) {
                // Get set ID from the NBT to add to the instance
                String setID = ((CompoundNBT)nbt).getString("setID");
                instance.setSetID(setID);

                // Get set effects from the NBT to add to the instance
                ListNBT setEffectsListNBT = ((CompoundNBT)nbt).getList("setEffects", Constants.NBT.TAG_LIST);
                for (int i = 0; i < setEffectsListNBT.size(); i++) {
                    CompoundNBT compoundNBT = setEffectsListNBT.getCompound(i);
                    PotionEffect setEffect = new PotionEffect(
                            compoundNBT.getString("effect"),
                            -1,
                            compoundNBT.getInt("amplifier"));
                    instance.addSetEffect(setEffect);
                }

                // Get used potion effects from the NBT to add to the instance
                ListNBT usedPotionEffectsListNBT = ((CompoundNBT)nbt).getList("usedPotionEffects", Constants.NBT.TAG_LIST);
                for (int i = 0; i < usedPotionEffectsListNBT.size(); i++) {
                    CompoundNBT compoundNBT = usedPotionEffectsListNBT.getCompound(i);
                    PotionEffect setEffect = new PotionEffect(
                            compoundNBT.getString("effect"),
                            compoundNBT.getInt("duration"),
                            compoundNBT.getInt("amplifier"));
                    instance.addUsedPotionEffect(setEffect);
                }
            }
        }
    }
}