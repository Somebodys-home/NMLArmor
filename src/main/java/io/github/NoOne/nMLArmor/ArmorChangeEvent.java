package io.github.NoOne.nMLArmor;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArmorChangeEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Character addremove;
    private final ItemStack armor;

    public ArmorChangeEvent(@NotNull Player player, Character addremove, ItemStack armor) {
        this.player = player;
        this.addremove = addremove;
        this.armor = armor;
    }

    @Override
    public HandlerList getHandlers() { return handlers; }

    public static HandlerList getHandlerList() { return handlers; } // deleting this breaks things, apparently

    public Player getPlayer() { return player; }

    public Character getAddremove() { return addremove; }

    public ItemStack getArmor() { return armor; }
}