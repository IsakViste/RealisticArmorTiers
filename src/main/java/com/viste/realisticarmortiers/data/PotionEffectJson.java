package com.viste.realisticarmortiers.data;

public class PotionEffectJson {
    private final String id;
    private int amplifier;

    public PotionEffectJson(String id, int amplifier) {
        this.id = id;
        this.amplifier = amplifier;
    }

    public String getId() {
        return id;
    }

    public int getAmplifier() {
        return amplifier--;
    }
}
