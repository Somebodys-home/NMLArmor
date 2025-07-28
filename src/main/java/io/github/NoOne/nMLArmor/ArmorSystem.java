package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLDefenses.DefenseLore;
import io.github.NoOne.nMLDefenses.DefenseManager;
import io.github.NoOne.nMLDefenses.DefenseType;
import io.github.NoOne.nMLDefenses.NMLDefenses;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import io.github.NoOne.nMLPlayerStats.statSystem.StatChangeEvent;
import io.github.NoOne.nMLPlayerStats.statSystem.Stats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static io.github.NoOne.nMLDefenses.DefenseType.*;

public class ArmorSystem {
    private NMLArmor nmlArmor;
    private DefenseManager defenseManager;
    private NamespacedKey originalNameKey;
    private NamespacedKey levelKey;

    public ArmorSystem(NMLArmor nmlArmor) {
        this.nmlArmor = nmlArmor;
        defenseManager = nmlArmor.getNmlDefenses().getDefenseManager();
        originalNameKey = new NamespacedKey(nmlArmor, "original_name");
        levelKey = new NamespacedKey(nmlArmor, "level");
    }

    public ItemStack generateArmor(Player receiver, ArmorRarity rarity, ArmorType type, String armorPiece, int level) {
        ItemStack weapon = new ItemStack(ArmorType.getArmorTypeMaterial(type, armorPiece));
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        List<String> lore = new ArrayList<>();

        pdc.set(makeArmorTypeKey(type), PersistentDataType.INTEGER, 1);
        pdc.set(makeArmorRarityKey(rarity), PersistentDataType.INTEGER, 1);
        pdc.set(levelKey, PersistentDataType.INTEGER, level);
        weapon.setItemMeta(meta);

        String name = generateArmorName(rarity, type, armorPiece, level);
        meta.setDisplayName(name);
        pdc.set(originalNameKey, PersistentDataType.STRING, name);

        lore.add(ArmorRarity.getArmorRarityColor(rarity) + "" + ChatColor.BOLD + ArmorRarity.getArmorRarityString(rarity).toUpperCase() + " " + ArmorType.getArmorTypeString(type).toUpperCase() + " " + armorPiece.toUpperCase());
        lore.add("");
        meta.setLore(lore);
        weapon.setItemMeta(meta);

        generateArmorStats(weapon, type, rarity, level);
        updateUnusableArmorName(weapon, isArmorUsable(weapon, receiver));

        return weapon;
    }

    public String generateArmorName(ArmorRarity rarity, ArmorType type, String armorPiece, int level) {
        String[] nameSegments = null;
        String name = "";

        if (rarity == ArmorRarity.COMMON) {

            nameSegments = new String[2];
            List<String> badAdjectives = new ArrayList<>(List.of("Garbage", "Awful", "Do Better", "Babies' First", "Oh God That", "Rotten", "Poor", "Degrading", "Forgotten", "Racist"));

            nameSegments[0] = badAdjectives.get(ThreadLocalRandom.current().nextInt(badAdjectives.size()));

        } else if (rarity == ArmorRarity.UNCOMMON) {

            nameSegments = new String[2];
            List<String> goodAdjectives = new ArrayList<>(List.of("Pretty Alright", "Hand-me-downed", "Based", "W", "Neato Dorito", "Goofy Ahh", "Nobodies'"));
            int randomAdjective = ThreadLocalRandom.current().nextInt(goodAdjectives.size());

            nameSegments[0] = goodAdjectives.get(randomAdjective);

        } else if (rarity == ArmorRarity.RARE) {

            nameSegments = new String[3];
            List<String> goodAdjectives = new ArrayList<>(List.of("Pretty Alright", "Solid", "Well-Made", "Lifelong", "Based", "W", "Almost Mythical", "Neato Dorito", "Goofy Ahh", "Nobodies'"));
            int randomAdjective = ThreadLocalRandom.current().nextInt(goodAdjectives.size());

            nameSegments[0] = goodAdjectives.get(randomAdjective);
            goodAdjectives.remove(randomAdjective);
            nameSegments[1] = goodAdjectives.get(ThreadLocalRandom.current().nextInt(goodAdjectives.size()));

        } else if (rarity == ArmorRarity.MYTHICAL) {

            nameSegments = new String[3];
            List<String> greatAdjectives = new ArrayList<>(List.of("Amazing", "Godly", "King's", "Fabled", "Based", "W", "Legendary", "Goofy Ahh", "Nobodies'"));
            int randomAdjective = ThreadLocalRandom.current().nextInt(greatAdjectives.size());

            nameSegments[0] = greatAdjectives.get(randomAdjective);
            greatAdjectives.remove(randomAdjective);
            nameSegments[1] = greatAdjectives.get(ThreadLocalRandom.current().nextInt(greatAdjectives.size()));

        }

        assert nameSegments != null;
        if (type == ArmorType.LIGHT) {
            if (Objects.equals(armorPiece, "helmet")) {
                List<String> sword = new ArrayList<>(List.of("Cap"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "chestplate")) {
                List<String> sword = new ArrayList<>(List.of("Shirt"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "leggings")) {
                List<String> sword = new ArrayList<>(List.of("Pants"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "boots")) {
                List<String> sword = new ArrayList<>(List.of("Shoes"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            }
        } else if (type == ArmorType.MEDIUM) {
            if (Objects.equals(armorPiece, "helmet")) {
                List<String> sword = new ArrayList<>(List.of("Coif", "Aventail"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "chestplate")) {
                List<String> sword = new ArrayList<>(List.of("Hauberk"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "leggings")) {
                List<String> sword = new ArrayList<>(List.of("Chausses"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "boots")) {
                List<String> sword = new ArrayList<>(List.of("Paleos"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            }
        } else if (type == ArmorType.HEAVY) {
            if (Objects.equals(armorPiece, "helmet")) {
                List<String> sword = new ArrayList<>(List.of("Helmet"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "chestplate")) {
                List<String> sword = new ArrayList<>(List.of("Chestplate"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "leggings")) {
                List<String> sword = new ArrayList<>(List.of("Chausses"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            } else if (Objects.equals(armorPiece, "boots")) {
                List<String> sword = new ArrayList<>(List.of("Boots"));
                nameSegments[nameSegments.length - 1] = sword.get(ThreadLocalRandom.current().nextInt(sword.size()));
            }
        }

        name += "§o§fLv. " + level + "§r " + ArmorRarity.getArmorRarityColor(rarity);
        for (int i = 0; i < nameSegments.length; i++) {
            if (i == nameSegments.length - 1) {
                name += nameSegments[i];
            } else {
                name += nameSegments[i] + " ";
            }
        }

        return name;
    }

    public void generateArmorStats(ItemStack armor, ArmorType type, ArmorRarity rarity, int level) {
        List<DefenseType> possibleFirstDefenseTypes = null;
        List<DefenseType> possibleSecondDefenseTypes = null;

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

        DefenseType firstType = possibleFirstDefenseTypes.get(ThreadLocalRandom.current().nextInt(possibleFirstDefenseTypes.size()));
        int firstDefense = level * 2;
        DefenseType secondType = possibleSecondDefenseTypes.get(ThreadLocalRandom.current().nextInt(possibleSecondDefenseTypes.size()));
        int secondDefense = level;

        switch (rarity) {
            case COMMON -> {
                defenseManager.setDefense(armor, firstType, firstDefense);
            }
            case UNCOMMON, RARE -> {
                if (firstType == secondType) {
                    defenseManager.setDefense(armor, firstType, firstDefense + secondDefense);
                } else {
                    defenseManager.setDefense(armor, firstType, firstDefense);
                    defenseManager.setDefense(armor, secondType, secondDefense);
                }
            }
            case MYTHICAL -> {
                firstDefense = level * 3;

                if (firstType == secondType) {
                    defenseManager.setDefense(armor, firstType, firstDefense + secondDefense);
                } else {
                    defenseManager.setDefense(armor, firstType, firstDefense);
                    defenseManager.setDefense(armor, secondType, secondDefense);
                }
            }
        }

        DefenseLore.updateLoreWithElementalDefense(armor, armor.getItemMeta());
    }

    public void addArmorStatsToPlayerStats(Player player, ItemStack armor) {
        if (isACustomArmorPiece(armor)) {
            HashMap<DefenseType, Double> defenseMap = nmlArmor.getNmlDefenses().getDefenseManager().getAllDefenseStats(armor);
            Stats stats = nmlArmor.getProfileManager().getPlayerProfile(player.getUniqueId()).getStats();

            for (Map.Entry<DefenseType, Double> stat : defenseMap.entrySet()) {
                switch (stat.getKey()) {
                    case EVASION -> stats.add2Stat("evasion", stat.getValue().intValue());
                    case DEFENSE -> stats.add2Stat("defense", stat.getValue().intValue());
                    case OVERHEALTH -> stats.add2Stat("maxoverhealth", stat.getValue());
                    case PHYSICALRESIST -> stats.add2Stat("physicalresist", stat.getValue().intValue());
                    case FIRERESIST -> stats.add2Stat("fireresist", stat.getValue().intValue());
                    case COLDRESIST -> stats.add2Stat("coldresist", (stat.getValue().intValue()));
                    case EARTHRESIST -> stats.add2Stat("earthresist", stat.getValue().intValue());
                    case LIGHTNINGRESIST -> stats.add2Stat("lightningresist", stat.getValue().intValue());
                    case AIRRESIST -> stats.add2Stat("airresist", stat.getValue().intValue());
                    case LIGHTRESIST -> stats.add2Stat("lightresist", stat.getValue().intValue());
                    case DARKRESIST -> stats.add2Stat("darkresist", stat.getValue().intValue());
                }

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, DefenseType.getDefenseString(stat.getKey()).toLowerCase()));
            }
        }
    }

    public void removeArmorStatsFromPlayerStats(Player player, ItemStack armor) {
        if (isACustomArmorPiece(armor)) {
            HashMap<DefenseType, Double> defenseMap = nmlArmor.getNmlDefenses().getDefenseManager().getAllDefenseStats(armor);
            Stats stats = nmlArmor.getProfileManager().getPlayerProfile(player.getUniqueId()).getStats();

            for (Map.Entry<DefenseType, Double> stat : defenseMap.entrySet()) {
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

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, DefenseType.getDefenseString(stat.getKey()).toLowerCase()));
            }
        }
    }

    public void updateUnusableArmorName(ItemStack weapon, boolean unusable) {
        ItemMeta meta = weapon.getItemMeta();
        String originalName = getOriginalItemName(weapon);
        String editedName;

        if (!unusable) {
            editedName = originalName.replaceAll("§[0-9a-fk-or]", "");
            editedName = "§c§m" + editedName;
        } else {
            editedName = originalName;
        }

        meta.setDisplayName(editedName);
        weapon.setItemMeta(meta);
    }

    public boolean isArmorUsable(ItemStack weapon, Player player) {
        if (weapon == null || !weapon.hasItemMeta()) return false;

        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        Integer itemLevel = pdc.get(levelKey, PersistentDataType.INTEGER);

        if (itemLevel == null) return false;

        int playerLevel = new ProfileManager(NMLArmor.getNmlPlayerStats()).getPlayerProfile(player.getUniqueId()).getStats().getLevel();
        return playerLevel >= itemLevel;
    }

    public NamespacedKey makeArmorTypeKey(ArmorType type) {
        return new NamespacedKey(nmlArmor, ArmorType.getArmorTypeString(type));
    }

    public ArmorType getArmorType(ItemStack armor) {
        ItemMeta meta = armor.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        for (ArmorType type : ArmorType.values()) {
            if (pdc.has(makeArmorTypeKey(type), PersistentDataType.INTEGER)) return type;
        }

        return null;
    }

    public NamespacedKey makeArmorRarityKey(ArmorRarity rarity) {
        return new NamespacedKey(nmlArmor, ArmorRarity.getArmorRarityString(rarity));
    }

    public ArmorRarity getArmorRarity(ItemStack armor) {
        ItemMeta meta = armor.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        for (ArmorRarity rarity : ArmorRarity.values()) {
            if (pdc.has(makeArmorRarityKey(rarity), PersistentDataType.INTEGER)) return rarity;
        }

        return null;
    }

    public String getOriginalItemName(ItemStack weapon) {
        if (weapon == null || weapon.getType().isAir()) return null;

        ItemMeta meta = weapon.getItemMeta();
        if (meta == null) return null;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(originalNameKey, PersistentDataType.STRING)) return null;

        return pdc.get(originalNameKey, PersistentDataType.STRING);
    }

    public boolean isACustomArmorPiece(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return false;
        if (!item.hasItemMeta()) { return false; }
        if (getArmorType(item) == null) { return false; }

        return true;
    }
}
