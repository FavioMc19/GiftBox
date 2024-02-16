package net.kokoricraft.giftbox.objects;

public class BoxItem {
    private final double chance;

    public BoxItem(double chance){
        this.chance = chance;
    }

    public double getChance() {
        return chance;
    }
}
