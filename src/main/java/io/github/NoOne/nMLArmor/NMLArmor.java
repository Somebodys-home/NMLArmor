package io.github.NoOne.nMLArmor;

import io.github.NoOne.nMLItems.ItemSystem;
import io.github.NoOne.nMLItems.NMLItems;
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

    @Override
    public void onEnable() {
        instance = this;

        armorSystem = new ArmorSystem(this);

        nmlPlayerStats = JavaPlugin.getPlugin(NMLPlayerStats.class);
        profileManager = nmlPlayerStats.getProfileManager();

        getServer().getPluginManager().registerEvents(new ArmorListener(this), this);
        getCommand("generateArmor").setExecutor(new GenerateArmorCommand(this));
    }

    public NMLArmor getInstance() {
        return instance;
    }

    public ProfileManager getProfileManager() {
        return profileManager;
    }

    public ArmorSystem getArmorSystem() {
        return armorSystem;
    }
}
