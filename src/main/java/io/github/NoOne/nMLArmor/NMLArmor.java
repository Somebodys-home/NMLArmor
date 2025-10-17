package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMLArmor extends JavaPlugin {
    private static NMLPlayerStats nmlPlayerStats;
    private ArmorStatsManager armorStatsManager;

    @Override
    public void onEnable() {
        nmlPlayerStats = JavaPlugin.getPlugin(NMLPlayerStats.class);

        armorStatsManager = new ArmorStatsManager();

        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);

    }

    public ArmorStatsManager getArmorStatsManager() {
        return armorStatsManager;
    }
}
