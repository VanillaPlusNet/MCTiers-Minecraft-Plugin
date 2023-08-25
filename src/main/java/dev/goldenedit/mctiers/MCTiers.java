package dev.goldenedit.mctiers;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCTiers extends JavaPlugin {

    public static Plugin plugin;
    @Override
    public void onEnable() {
        plugin = (Plugin)this;
        this.getCommand("tier").setExecutor(new TierCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
