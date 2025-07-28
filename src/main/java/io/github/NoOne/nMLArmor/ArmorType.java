package io.github.NoOne.nMLArmor;

import org.bukkit.Material;

public enum ArmorType {
    LIGHT,
    MEDIUM,
    HEAVY;

    public static Material getArmorTypeMaterial(ArmorType type, String armorPiece) {
        if (type == LIGHT) {
            if (armorPiece.equalsIgnoreCase("helmet")) return Material.LEATHER_HELMET;
            if (armorPiece.equalsIgnoreCase("chestplate")) return Material.LEATHER_CHESTPLATE;
            if (armorPiece.equalsIgnoreCase("leggings")) return Material.LEATHER_LEGGINGS;
            if (armorPiece.equalsIgnoreCase("boots")) return Material.LEATHER_BOOTS;
        } else if (type == MEDIUM) {
            if (armorPiece.equalsIgnoreCase("helmet")) return Material.CHAINMAIL_HELMET;
            if (armorPiece.equalsIgnoreCase("chestplate")) return Material.CHAINMAIL_CHESTPLATE;
            if (armorPiece.equalsIgnoreCase("leggings")) return Material.CHAINMAIL_LEGGINGS;
            if (armorPiece.equalsIgnoreCase("boots")) return Material.CHAINMAIL_BOOTS;
        } else if (type == HEAVY) {
            if (armorPiece.equalsIgnoreCase("helmet")) return Material.IRON_HELMET;
            if (armorPiece.equalsIgnoreCase("chestplate")) return Material.IRON_CHESTPLATE;
            if (armorPiece.equalsIgnoreCase("leggings")) return Material.IRON_LEGGINGS;
            if (armorPiece.equalsIgnoreCase("boots")) return Material.IRON_BOOTS;
        }

        return null;
    }


    public static String getArmorTypeString(ArmorType type) {
        switch (type) {
            case LIGHT -> { return "light"; }
            case MEDIUM -> { return "medium"; }
            case HEAVY -> { return "heavy"; }
            default -> { return ""; }
        }
    }

    public static ArmorType getArmorTypeFromString(String string) {
        switch (string) {
            case "light" -> { return LIGHT; }
            case "medium" -> { return MEDIUM; }
            case "heavy" -> { return HEAVY; }
            default -> { return null; }
        }
    }
}
