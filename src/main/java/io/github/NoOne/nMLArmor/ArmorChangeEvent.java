package io.github.NoOne.nMLArmor;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArmorChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack armorRemoved;
    private final ItemStack armorEquipped;

    public ArmorChangeEvent(@NotNull Player player, ItemStack armorRemoved, ItemStack armor) {
        super(false);
        this.player = player;
        this.armorRemoved = armorRemoved;
        this.armorEquipped = armor;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; } // deleting this breaks things, apparently

    public Player getPlayer() { return player; }

    public ItemStack getArmorRemoved() { return armorRemoved; }

    public ItemStack getArmorEquipped() { return armorEquipped; }
}