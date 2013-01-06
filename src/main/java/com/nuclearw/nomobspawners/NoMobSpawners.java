package com.nuclearw.nomobspawners;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NoMobSpawners extends JavaPlugin {
	
	public void onEnable() {
		Config.load(this);

		PluginManager manager = getServer().getPluginManager();

		manager.registerEvents(new SpawnerListener(this), this);

		getLogger().info("Finished loading " + getDescription().getFullName());
	}

	public void onDisable() {
		getLogger().info("Finished unloading " + getDescription().getFullName());
	}
}
