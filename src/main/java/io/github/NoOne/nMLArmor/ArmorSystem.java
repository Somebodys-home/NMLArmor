package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLItems.ItemRarity;
import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLItems.ItemType;
import io.github.NoOne.nMLPlayerStats.statSystem.StatChangeEvent;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
        ItemStack weapon = new ItemStack(ItemType.getItemTypeMaterial(type, armorPiece));
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        List<String> lore = new ArrayList<>();

        pdc.set(ItemSystem.makeItemTypeKey(type), PersistentDataType.INTEGER, 1);
        pdc.set(ItemSystem.makeItemTypeKey(armorPiece), PersistentDataType.INTEGER, 1);
        pdc.set(ItemSystem.makeItemRarityKey(rarity), PersistentDataType.INTEGER, 1);
        pdc.set(ItemSystem.getLevelKey(), PersistentDataType.INTEGER, level);
        weapon.setItemMeta(meta);

        String name = generateArmorName(rarity, type, armorPiece, level);
        meta.setDisplayName(name);
        pdc.set(ItemSystem.getOriginalNameKey(), PersistentDataType.STRING, name);

        lore.add(ItemRarity.getItemRarityColor(rarity) + "" + ChatColor.BOLD + ItemRarity.getItemRarityString(rarity).toUpperCase() + " " + ItemType.getItemTypeString(type).toUpperCase() + " " + ItemType.getItemTypeString(armorPiece).toUpperCase());
        lore.add("");
        meta.setLore(lore);
        weapon.setItemMeta(meta);

        generateArmorStats(weapon, type, rarity, level);
        ItemSystem.updateUnusableItemName(weapon, ItemSystem.isItemUsable(weapon, receiver));

        return weapon;
    }

    public String generateArmorName(ItemRarity rarity, ItemType type, ItemType armorPiece, int level) {
        String[] nameSegments = null;
        String name = "";

        if (rarity == ItemRarity.COMMON) {

            nameSegments = new String[2];
            List<String> badAdjectives = new ArrayList<>(List.of("Garbage", "Awful", "Do Better", "Babies' First", "Oh God That", "Rotten", "Poor", "Degrading", "Forgotten", "Racist"));

            nameSegments[0] = badAdjectives.get(ThreadLocalRandom.current().nextInt(badAdjectives.size()));

        } else if (rarity == ItemRarity.UNCOMMON) {

            nameSegments = new String[2];
            List<String> goodAdjectives = new ArrayList<>(List.of("Pretty Alright", "Hand-me-downed", "Based", "W", "Neato Dorito", "Goofy Ahh", "Nobodies'"));
            int randomAdjective = ThreadLocalRandom.current().nextInt(goodAdjectives.size());

            nameSegments[0] = goodAdjectives.get(randomAdjective);

        } else if (rarity == ItemRarity.RARE) {

            nameSegments = new String[3];
            List<String> goodAdjectives = new ArrayList<>(List.of("Pretty Alright", "Solid", "Well-Made", "Lifelong", "Based", "W", "Almost Mythical", "Neato Dorito", "Goofy Ahh", "Nobodies'"));
            int randomAdjective = ThreadLocalRandom.current().nextInt(goodAdjectives.size());

            nameSegments[0] = goodAdjectives.get(randomAdjective);
            goodAdjectives.remove(randomAdjective);
            nameSegments[1] = goodAdjectives.get(ThreadLocalRandom.current().nextInt(goodAdjectives.size()));

        } else if (rarity == ItemRarity.MYTHICAL) {

            nameSegments = new String[3];
            List<String> greatAdjectives = new ArrayList<>(List.of("Amazing", "Godly", "King's", "Fabled", "Based", "W", "Legendary", "Goofy Ahh", "Nobodies'"));
            int randomAdjective = ThreadLocalRandom.current().nextInt(greatAdjectives.size());

            nameSegments[0] = greatAdjectives.get(randomAdjective);
            greatAdjectives.remove(randomAdjective);
            nameSegments[1] = greatAdjectives.get(ThreadLocalRandom.current().nextInt(greatAdjectives.size()));

        }

        assert nameSegments != null;
        if (type == ItemType.LIGHT) {
            if (armorPiece == ItemType.HELMET) {
                List<String> sword = new ArrayList<>(List.of("Cap"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.CHESTPLATE) {
                List<String> sword = new ArrayList<>(List.of("Shirt"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.LEGGINGS) {
                List<String> sword = new ArrayList<>(List.of("Pants", "GYATT"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.BOOTS) {
                List<String> sword = new ArrayList<>(List.of("Shoes"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            }
        } else if (type == ItemType.MEDIUM) {
            if (armorPiece == ItemType.HELMET) {
                List<String> sword = new ArrayList<>(List.of("Coif", "Aventail"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.CHESTPLATE) {
                List<String> sword = new ArrayList<>(List.of("Hauberk"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.LEGGINGS) {
                List<String> sword = new ArrayList<>(List.of("Chausses", "GYATT"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.BOOTS) {
                List<String> sword = new ArrayList<>(List.of("Paleos"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            }
        } else if (type == ItemType.HEAVY) {
            if (armorPiece == ItemType.HELMET) {
                List<String> sword = new ArrayList<>(List.of("Helmet"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.CHESTPLATE) {
                List<String> sword = new ArrayList<>(List.of("Chestplate"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.LEGGINGS) {
                List<String> sword = new ArrayList<>(List.of("Chausses", "GYATT"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (armorPiece == ItemType.BOOTS) {
                List<String> sword = new ArrayList<>(List.of("Boots"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            }
        }

        name += "§o§fLv. " + level + "§r " + ItemRarity.getItemRarityColor(rarity);
        for (int i = 0; i < nameSegments.length; i++) {
            if (i == nameSegments.length - 1) {
                name += nameSegments[i];
            } else {
                name += nameSegments[i] + " ";
            }
        }

        return name;
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

        ItemSystem.updateLoreWithItemStats(armor);
    }

    public void addArmorStatsToPlayerStats(Player player, ItemStack armor) {
        if (isACustomArmorPiece(armor)) {
            HashMap<ItemStat, Double> statMap = ItemSystem.getAllStats(armor);
            Stats playerStats = nmlArmor.getProfileManager().getPlayerProfile(player.getUniqueId()).getStats();

            for (Map.Entry<ItemStat, Double> stat : statMap.entrySet()) {
                switch (stat.getKey()) {
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
        if (isACustomArmorPiece(armor)) {
            HashMap<ItemStat, Double> statMap = ItemSystem.getAllStats(armor);
            Stats stats = nmlArmor.getProfileManager().getPlayerProfile(player.getUniqueId()).getStats();

            for (Map.Entry<ItemStat, Double> stat : statMap.entrySet()) {
                switch (stat.getKey()) {
                    case EVASION -> stats.removeFromStat("evasion", stat.getValue().intValue());
                    case DEFENSE -> stats.removeFromStat("defense", stat.getValue().intValue());
                    case OVERHEALTH -> stats.removeFromStat("maxoverhealth", stat.getValue());
                    case PHYSICALRESIST -> stats.removeFromStat("physicalresist", stat.getValue().intValue());
                    case FIRERESIST -> stats.removeFromStat("fireresist", stat.getValue().intValue());
                    case COLDRESIST -> stats.removeFromStat("coldresist", (stat.getValue().intValue()));
                    case EARTHRESIST -> stats.removeFromStat("earthresist", stat.getValue().intValue());
                    case LIGHTNINGRESIST -> stats.removeFromStat("lightningresist", stat.getValue().intValue());
                    case AIRRESIST -> stats.removeFromStat("airresist", stat.getValue().intValue());
                    case LIGHTRESIST -> stats.removeFromStat("lightresist", stat.getValue().intValue());
                    case DARKRESIST -> stats.removeFromStat("darkresist", stat.getValue().intValue());
                }

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, ItemStat.getStatString(stat.getKey()).toLowerCase()));
            }
        }
    }

    public boolean isACustomArmorPiece(ItemStack item) {
        if (item == null) {
            return false;
        }

        return ItemSystem.getItemTypeFromItemStack(item) == ItemType.HELMET || ItemSystem.getItemTypeFromItemStack(item) == ItemType.CHESTPLATE ||
                ItemSystem.getItemTypeFromItemStack(item) == ItemType.LEGGINGS ||  ItemSystem.getItemTypeFromItemStack(item) == ItemType.BOOTS;
    }
}
