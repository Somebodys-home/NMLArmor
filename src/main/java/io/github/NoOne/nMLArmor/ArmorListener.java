package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArmorListener implements Listener {
    private ArmorSystem armorSystem;
    private ItemSystem itemSystem;

    public ArmorListener(NMLArmor nmlArmor) {
        armorSystem = nmlArmor.getArmorSystem();
        itemSystem = nmlArmor.getItemSystem();
    }

    @EventHandler()
    public void armorLevelCheck(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        boolean usable = itemSystem.isItemUsable(heldItem, player);

        if (heldItem == null || heldItem.getType() == Material.AIR) { return; }
        if (!heldItem.hasItemMeta()) { return; }
        if (itemSystem.getItemTypeFromItemStack(heldItem) == null) { return; }
        if (!usable) {
            player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
        }

        itemSystem.updateUnusableItemName(heldItem, usable);
    }

    @EventHandler
    public void updatePlayerStatsWhenEquippingArmor(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        ClickType click = event.getClick();
        int rawSlot = event.getRawSlot();

        if ((click == ClickType.LEFT || click == ClickType.RIGHT) && rawSlot >= 5 && rawSlot <= 8) {
            ItemStack armor = event.getCursor();

            if (armorSystem.isACustomArmorPiece(armor)) {
                armorSystem.addArmorStatsToPlayerStats(player, armor);
                itemSystem.updateUnusableItemName(armor, true);
            }
        } else if ((click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)) {
            ItemStack armor = event.getCurrentItem();

            if (armorSystem.isACustomArmorPiece(armor)) {
                PlayerInventory inv = player.getInventory();
                Material type = armor.getType();

                if ((type.name().endsWith("_HELMET") && inv.getHelmet() == null) ||
                    (type.name().endsWith("_CHESTPLATE") && inv.getChestplate() == null) ||
                    (type.name().endsWith("_LEGGINGS") && inv.getLeggings() == null) ||
                    (type.name().endsWith("_BOOTS") && inv.getBoots() == null)) {

                    if (itemSystem.isItemUsable(armor, player)) {
                        armorSystem.addArmorStatsToPlayerStats(player, armor);
                        itemSystem.updateUnusableItemName(armor, true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void updatePlayerStatsWhenUnequippingArmor(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        ClickType click = event.getClick();
        int rawSlot = event.getRawSlot();
        ItemStack armor = event.getCurrentItem();

        if ((click == ClickType.LEFT || click == ClickType.RIGHT) && rawSlot >= 5 && rawSlot <= 8) {
            if (armorSystem.isACustomArmorPiece(armor)) {
                armorSystem.removeArmorStatsFromPlayerStats(player, armor);
            }

        } else if ((click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)) {
            if (armorSystem.isACustomArmorPiece(armor)) {
                PlayerInventory inv = player.getInventory();
                Material type = armor.getType();

                boolean isCurrentlyEquipped =
                        (type.name().endsWith("_HELMET") && armor.equals(inv.getHelmet())) ||
                                (type.name().endsWith("_CHESTPLATE") && armor.equals(inv.getChestplate())) ||
                                (type.name().endsWith("_LEGGINGS") && armor.equals(inv.getLeggings())) ||
                                (type.name().endsWith("_BOOTS") && armor.equals(inv.getBoots()));

                if (isCurrentlyEquipped) {
                    armorSystem.removeArmorStatsFromPlayerStats(player, armor);
                    itemSystem.updateUnusableItemName(armor, true);
                }
            }
        } else if (click == ClickType.NUMBER_KEY && rawSlot >= 5 && rawSlot <= 8) {
            if (armorSystem.isACustomArmorPiece(armor)) {
                armorSystem.removeArmorStatsFromPlayerStats(player, armor);
            }
        }
    }


    @EventHandler
    public void blockEquippingUnusableArmor(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getClickedInventory() == null) return;

        ClickType click = event.getClick();
        if (click == ClickType.LEFT || click == ClickType.RIGHT) {
            int slot = event.getRawSlot();
            if (slot >= 5 && slot <= 8) {
                ItemStack armor = event.getCursor();

                if (armorSystem.isACustomArmorPiece(armor) && !itemSystem.isItemUsable(armor, player)) {
                    event.setCancelled(true);
                    player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
                    itemSystem.updateUnusableItemName(armor, false);
                }
            }
        } else if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT) {
            ItemStack armor = event.getCurrentItem();
            if (armorSystem.isACustomArmorPiece(armor) && !itemSystem.isItemUsable(armor, player)) {
                event.setCancelled(true);
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
                itemSystem.updateUnusableItemName(armor, false);
            }
        }
    }

    @EventHandler
    public void blockRightClickEquippingUnusableArmorFromHand(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if (armorSystem.isACustomArmorPiece(item) && !itemSystem.isItemUsable(item, player)) {
                itemSystem.updateUnusableItemName(item, false);
                event.setCancelled(true);
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
            }
        }
    }
}
