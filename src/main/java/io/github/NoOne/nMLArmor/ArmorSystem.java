package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLItems.ItemRarity;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLItems.ItemType;
import io.github.NoOne.nMLPlayerStats.statSystem.StatChangeEvent;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static io.github.NoOne.nMLItems.ItemStat.*;

public class ArmorSystem {
    private NMLArmor nmlArmor;

    public ArmorSystem(NMLArmor nmlArmor) {
        this.nmlArmor = nmlArmor;
    }

    public ItemStack generateArmor(Player receiver, ItemRarity rarity, ItemType type, ItemType armorPiece, int level) {
        ItemStack armor = new ItemStack(ItemType.getItemTypeMaterial(type, armorPiece));
        ItemMeta meta = armor.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        List<String> lore = new ArrayList<>();

        pdc.set(ItemSystem.makeItemTypeKey(type), PersistentDataType.INTEGER, 1);
        pdc.set(ItemSystem.makeItemTypeKey(armorPiece), PersistentDataType.INTEGER, 1);
        pdc.set(ItemSystem.makeItemRarityKey(rarity), PersistentDataType.INTEGER, 1);
        pdc.set(ItemSystem.getLevelKey(), PersistentDataType.INTEGER, level);
        meta.setUnbreakable(true);
        armor.setItemMeta(meta);

        String name = ItemSystem.generateItemName(type, armorPiece, rarity);
        meta.setDisplayName(name);
        pdc.set(ItemSystem.getOriginalNameKey(), PersistentDataType.STRING, name);

        lore.add("§o§fLv. " + level + "§r " +  ItemRarity.getItemRarityColor(rarity) + ChatColor.BOLD + ItemRarity.getItemRarityString(rarity).toUpperCase() + " " + ItemType.getItemTypeString(type).toUpperCase() + " " + ItemType.getItemTypeString(armorPiece).toUpperCase());
        lore.add("");
        meta.setLore(lore);
        armor.setItemMeta(meta);

        generateArmorStats(armor, type, rarity, level);
        ItemSystem.updateUnusableItemName(armor, ItemSystem.isItemUsable(armor, receiver));

        return armor;
    }

    public void generateArmorStats(ItemStack armor, ItemType type, ItemRarity rarity, int level) {
        List<ItemStat> possibleFirstDefenseTypes = null;
        List<ItemStat> possibleSecondDefenseTypes = null;

        switch (type) {
            case LIGHT -> {
                possibleFirstDefenseTypes = new ArrayList<>(List.of(OVERHEALTH));
                possibleSecondDefenseTypes = new ArrayList<>(List.of(PHYSICALRESIST, FIRERESIST, COLDRESIST, EARTHRESIST, LIGHTNINGRESIST, AIRRESIST, LIGHTRESIST, DARKRESIST));
            }
            case MEDIUM -> {
                possibleFirstDefenseTypes = new ArrayList<>(List.of(EVASION));
                possibleSecondDefenseTypes = new ArrayList<>(List.of(PHYSICALRESIST, FIRERESIST, COLDRESIST, EARTHRESIST, LIGHTNINGRESIST, AIRRESIST, LIGHTRESIST, DARKRESIST));
            }
            case HEAVY -> {
                possibleFirstDefenseTypes = new ArrayList<>(List.of(DEFENSE));
                possibleSecondDefenseTypes = new ArrayList<>(List.of(PHYSICALRESIST, FIRERESIST, COLDRESIST, EARTHRESIST, LIGHTNINGRESIST, AIRRESIST, LIGHTRESIST, DARKRESIST));
            }
        }

        ItemStat firstStat = possibleFirstDefenseTypes.get(ThreadLocalRandom.current().nextInt(possibleFirstDefenseTypes.size()));
        int firstStatValue = level * 2;
        ItemStat secondStat = possibleSecondDefenseTypes.get(ThreadLocalRandom.current().nextInt(possibleSecondDefenseTypes.size()));
        int secondDefense = level;

        switch (rarity) {
            case COMMON -> {
                ItemSystem.setStat(armor, firstStat, firstStatValue);
            }
            case UNCOMMON, RARE -> {
                if (firstStat == secondStat) {
                    ItemSystem.setStat(armor, firstStat, firstStatValue + secondDefense);
                } else {
                    ItemSystem.setStat(armor, firstStat, firstStatValue);
                    ItemSystem.setStat(armor, secondStat, secondDefense);
                }
            }
            case MYTHICAL -> {
                firstStatValue = level * 3;

                if (firstStat == secondStat) {
                    ItemSystem.setStat(armor, firstStat, firstStatValue + secondDefense);
                } else {
                    ItemSystem.setStat(armor, firstStat, firstStatValue);
                    ItemSystem.setStat(armor, secondStat, secondDefense);
                }
            }
        }

        ItemSystem.updateLoreWithStats(armor);
    }

    public void addArmorStatsToPlayerStats(Player player, ItemStack armor) {
        if (ItemSystem.isEquippable(armor)) {
            HashMap<ItemStat, Double> statMap = ItemSystem.getAllStats(armor);
            Stats playerStats = nmlArmor.getProfileManager().getPlayerProfile(player.getUniqueId()).getStats();

            for (Map.Entry<ItemStat, Double> stat : statMap.entrySet()) {
                switch (stat.getKey()) {
                    case PHYSICALDAMAGE -> playerStats.add2Stat("physicaldamage", stat.getValue().intValue());
                    case FIREDAMAGE -> playerStats.add2Stat("firedamage", stat.getValue().intValue());
                    case COLDDAMAGE -> playerStats.add2Stat("colddamage", stat.getValue().intValue());
                    case EARTHDAMAGE -> playerStats.add2Stat("earthdamage", stat.getValue().intValue());
                    case LIGHTNINGDAMAGE -> playerStats.add2Stat("lightningdamage", stat.getValue().intValue());
                    case AIRDAMAGE -> playerStats.add2Stat("airdamage", stat.getValue().intValue());
                    case LIGHTDAMAGE -> playerStats.add2Stat("lightdamage", stat.getValue().intValue());
                    case DARKDAMAGE -> playerStats.add2Stat("darkdamage", stat.getValue().intValue());
                    case PUREDAMAGE -> playerStats.add2Stat("puredamage", stat.getValue().intValue());
                    case CRITCHANCE -> playerStats.add2Stat("critchance", stat.getValue().intValue());
                    case CRITDAMAGE -> playerStats.add2Stat("critdamage", stat.getValue().intValue());
                    case GUARD -> playerStats.add2Stat("guard", stat.getValue().intValue());
                    case EVASION -> playerStats.add2Stat("evasion", stat.getValue().intValue());
                    case DEFENSE -> playerStats.add2Stat("defense", stat.getValue().intValue());
                    case OVERHEALTH -> playerStats.add2Stat("maxoverhealth", stat.getValue());
                    case PHYSICALRESIST -> playerStats.add2Stat("physicalresist", stat.getValue().intValue());
                    case FIRERESIST -> playerStats.add2Stat("fireresist", stat.getValue().intValue());
                    case COLDRESIST -> playerStats.add2Stat("coldresist", (stat.getValue().intValue()));
                    case EARTHRESIST -> playerStats.add2Stat("earthresist", stat.getValue().intValue());
                    case LIGHTNINGRESIST -> playerStats.add2Stat("lightningresist", stat.getValue().intValue());
                    case AIRRESIST -> playerStats.add2Stat("airresist", stat.getValue().intValue());
                    case LIGHTRESIST -> playerStats.add2Stat("lightresist", stat.getValue().intValue());
                    case DARKRESIST -> playerStats.add2Stat("darkresist", stat.getValue().intValue());
                }

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, ItemStat.getStatString(stat.getKey()).toLowerCase()));
            }
        }
    }

    public void removeArmorStatsFromPlayerStats(Player player, ItemStack armor) {
        if (ItemSystem.isEquippable(armor)) {
            HashMap<ItemStat, Double> statMap = ItemSystem.getAllStats(armor);
            Stats playerStats = nmlArmor.getProfileManager().getPlayerProfile(player.getUniqueId()).getStats();

            for (Map.Entry<ItemStat, Double> stat : statMap.entrySet()) {
                switch (stat.getKey()) {
                    case PHYSICALDAMAGE -> playerStats.removeFromStat("physicaldamage", stat.getValue().intValue());
                    case FIREDAMAGE -> playerStats.removeFromStat("firedamage", stat.getValue().intValue());
                    case COLDDAMAGE -> playerStats.removeFromStat("colddamage", stat.getValue().intValue());
                    case EARTHDAMAGE -> playerStats.removeFromStat("earthdamage", stat.getValue().intValue());
                    case LIGHTNINGDAMAGE -> playerStats.removeFromStat("lightningdamage", stat.getValue().intValue());
                    case AIRDAMAGE -> playerStats.removeFromStat("airdamage", stat.getValue().intValue());
                    case LIGHTDAMAGE -> playerStats.removeFromStat("lightdamage", stat.getValue().intValue());
                    case DARKDAMAGE -> playerStats.removeFromStat("darkdamage", stat.getValue().intValue());
                    case PUREDAMAGE -> playerStats.removeFromStat("puredamage", stat.getValue().intValue());
                    case CRITCHANCE -> playerStats.removeFromStat("critchance", stat.getValue().intValue());
                    case CRITDAMAGE -> playerStats.removeFromStat("critdamage", stat.getValue().intValue());
                    case GUARD -> playerStats.removeFromStat("guard", stat.getValue().intValue());
                    case EVASION -> playerStats.removeFromStat("evasion", stat.getValue().intValue());
                    case DEFENSE -> playerStats.removeFromStat("defense", stat.getValue().intValue());
                    case OVERHEALTH -> playerStats.removeFromStat("maxoverhealth", stat.getValue());
                    case PHYSICALRESIST -> playerStats.removeFromStat("physicalresist", stat.getValue().intValue());
                    case FIRERESIST -> playerStats.removeFromStat("fireresist", stat.getValue().intValue());
                    case COLDRESIST -> playerStats.removeFromStat("coldresist", (stat.getValue().intValue()));
                    case EARTHRESIST -> playerStats.removeFromStat("earthresist", stat.getValue().intValue());
                    case LIGHTNINGRESIST -> playerStats.removeFromStat("lightningresist", stat.getValue().intValue());
                    case AIRRESIST -> playerStats.removeFromStat("airresist", stat.getValue().intValue());
                    case LIGHTRESIST -> playerStats.removeFromStat("lightresist", stat.getValue().intValue());
                    case DARKRESIST -> playerStats.removeFromStat("darkresist", stat.getValue().intValue());
                }

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, ItemStat.getStatString(stat.getKey()).toLowerCase()));
            }
        }
    }

}
