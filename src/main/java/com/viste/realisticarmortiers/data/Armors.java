package com.viste.realisticarmortiers.data;

import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Armors {
    private List<Armor> armors = new ArrayList<>();

    public Armor getFullSetArmorThatPlayerIsWearing(ServerPlayerEntity player) {
        for(Armor armor : armors) {
            if (armor.isFullSet(player)) {
                return armor;
            }
        }

        return null;
    }

    public List<Armor> getArmors() {
        return this.armors;
    }

    public void addArmor(Armor armor) {
        this.armors.add(armor);
    }
}
