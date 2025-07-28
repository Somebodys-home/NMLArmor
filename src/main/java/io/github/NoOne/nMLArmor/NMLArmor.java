package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLDefenses.DefenseManager;
import io.github.NoOne.nMLDefenses.NMLDefenses;
import io.github.NoOne.nMLPlayerStats.NMLPlayerStats;
import io.github.NoOne.nMLPlayerStats.profileSystem.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class NMLArmor extends JavaPlugin {
    private NMLArmor instance;
    private static NMLPlayerStats nmlPlayerStats;
    private ProfileManager profileManager;
    private ArmorSystem armorSystem;
    private NMLDefenses nmlDefenses;
    private DefenseManager defenseManager;

    @Override
    public void onEnable() {
        instance = this;
        nmlDefenses = JavaPlugin.getPlugin(NMLDefenses.class);
        defenseManager = nmlDefenses.getDefenseManager();

        Plugin plugin = Bukkit.getPluginManager().getPlugin("NMLPlayerStats");
        if (plugin instanceof NMLPlayerStats statsPlugin) {
            nmlPlayerStats = statsPlugin;
            profileManager = nmlPlayerStats.getProfileManager();
        } else {
            getLogger().severe("Failed to find NMLPlayerStats! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        armorSystem = new ArmorSystem(this);

        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
        getCommand("generateArmor").setExecutor(new GenerateArmorCommand(this));
    }

    public NMLArmor getInstance() {
        return instance;
    }

    public static NMLPlayerStats getNmlPlayerStats() {
        return nmlPlayerStats;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ArmorSystem getArmorSystem() {
        return armorSystem;
    }

    public NMLDefenses getNmlDefenses() {
        return nmlDefenses;
    }
}
