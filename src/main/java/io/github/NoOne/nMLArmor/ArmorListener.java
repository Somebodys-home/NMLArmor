package io.github.NoOne.nMLArmor;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import io.github.NoOne.nMLItems.ItemSystem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorListener implements Listener {
    private ArmorStatsManager armorStatsManager;

    public ArmorListener(NMLArmor nmlArmor) {
        armorStatsManager = nmlArmor.getArmorStatsManager();
    }

    @EventHandler
    public void updatePlayerStatsWithArmor(PlayerArmorChangeEvent event) {
        armorStatsManager.removeArmorStatsFromPlayerStats(event.getPlayer(), event.getOldItem());
        armorStatsManager.addArmorStatsToPlayerStats(event.getPlayer(), event.getNewItem());
        ItemSystem.updateUnusableItemName(event.getOldItem(), true);
    }

    @EventHandler()
    public void armorLevelCheck(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        boolean usable = ItemSystem.isItemUsable(heldItem, player);

        if (heldItem == null || heldItem.getType() == Material.AIR) { return; }
        if (!heldItem.hasItemMeta()) { return; }
        if (ItemSystem.getItemType(heldItem) == null) { return; }
        if (!usable) {
            player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
        }

        ItemSystem.updateUnusableItemName(heldItem, usable);
    }

    @EventHandler
    public void blockManuallyEquippingUnusableArmor(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryAction action = event.getAction();
        ItemStack armor = event.getCursor();
        int slot = event.getSlot();

        if ((slot >= 36 && slot <= 40) && (action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.PLACE_SOME)) {
            if (ItemSystem.isEquippable(armor) && !ItemSystem.isItemUsable(armor, player)) {
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
                event.setCancelled(true);
                ItemSystem.updateUnusableItemName(armor, false);
            }
        }
    }

    @EventHandler
    public void blockShiftEquippingUnusableArmor(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ClickType click = event.getClick();
        ItemStack armor = event.getCurrentItem();

        if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT) {
            if (ItemSystem.isEquippable(armor) && !ItemSystem.isItemUsable(armor, player)) {
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
                event.setCancelled(true);
                ItemSystem.updateUnusableItemName(armor, false);
            }
        }
    }

    @EventHandler
    public void blockRightClickEquippingUnusableArmorFromHand(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if (ItemSystem.isEquippable(item) && !ItemSystem.isItemUsable(item, player)) {
                ItemSystem.updateUnusableItemName(item, false);
                event.setCancelled(true);
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
            }
        }
    }
}
