package io.github.NoOne.nMLArmor;

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
    private NMLArmor nmlArmor;
    private ArmorSystem armorSystem;
    private ProfileManager profileManager;

    public ArmorListener(NMLArmor nmlArmor) {
        this.nmlArmor = nmlArmor;
        armorSystem = nmlArmor.getArmorSystem();
        profileManager = nmlArmor.getProfileManager();
    }

    @EventHandler()
    public void armorLevelCheck(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getInventory().getItem(event.getNewSlot());
        boolean usable = armorSystem.isArmorUsable(heldItem, player);

        if (heldItem == null || heldItem.getType() == Material.AIR) { return; }
        if (!heldItem.hasItemMeta()) { return; }
        if (armorSystem.getArmorType(heldItem) == null) { return; }
        if (!usable) {
            player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
        }

        armorSystem.updateUnusableArmorName(heldItem, usable);
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

                    armorSystem.addArmorStatsToPlayerStats(player, armor);
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

        if ((click == ClickType.LEFT || click == ClickType.RIGHT) && rawSlot >= 5 && rawSlot <= 8) {
            ItemStack previousArmor = event.getCurrentItem();

            if (armorSystem.isACustomArmorPiece(previousArmor)) {
                armorSystem.removeArmorStatsFromPlayerStats(player, previousArmor);
            }

        } else if ((click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT)) {
            ItemStack armor = event.getCurrentItem();

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
                }
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

                if (armorSystem.isACustomArmorPiece(armor) && !armorSystem.isArmorUsable(armor, player)) {
                    event.setCancelled(true);
                    player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
                }
            }
        } else if (click == ClickType.SHIFT_LEFT || click == ClickType.SHIFT_RIGHT) {
            ItemStack armor = event.getCurrentItem();
            if (armorSystem.isACustomArmorPiece(armor) && !armorSystem.isArmorUsable(armor, player)) {
                event.setCancelled(true);
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
            }
        }
    }

    @EventHandler
    public void blockRightClickEquippingUnusableArmorFromHand(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();

            if (armorSystem.isACustomArmorPiece(item) && !armorSystem.isArmorUsable(item, player)) {
                event.setCancelled(true);
                player.sendMessage("§c⚠ §nYou are too inexperienced for this item!§r§c ⚠");
            }
        }
    }
}
