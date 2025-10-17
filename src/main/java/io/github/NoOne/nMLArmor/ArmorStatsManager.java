package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLItems.ItemStat;
import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLPlayerStats.statSystem.StatChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.*;

public class ArmorStatsManager {
    public void addArmorStatsToPlayerStats(Player player, ItemStack armor) {
        if (ItemSystem.isEquippable(armor)) {
            HashMap<ItemStat, Double> statMap = ItemSystem.getAllStats(armor);

            for (Map.Entry<ItemStat, Double> stat : statMap.entrySet()) {
                String statString = ItemStat.getStatString(stat.getKey()).toLowerCase().replaceAll("\\s", "");

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, statString, stat.getValue()));
            }
        }
    }

    public void removeArmorStatsFromPlayerStats(Player player, ItemStack armor) {
        if (ItemSystem.isEquippable(armor)) {
            HashMap<ItemStat, Double> statMap = ItemSystem.getAllStats(armor);

            for (Map.Entry<ItemStat, Double> stat : statMap.entrySet()) {
                String statString = ItemStat.getStatString(stat.getKey()).toLowerCase().replaceAll("\\s", "");

                Bukkit.getPluginManager().callEvent(new StatChangeEvent(player, statString, -stat.getValue()));
            }
        }
    }
}
