package com.nuclearw.nomobspawners;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class NoMobSpawners extends JavaPlugin {
	public Queue<Location> remove = new LinkedList<Location>();
	
	public void onEnable() {
		Config.load(this);

		BukkitScheduler bs = getServer().getScheduler();
		bs.scheduleSyncRepeatingTask(this, new SpawnerRemoverTask(this), 0L, 5L);

		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(new SpawnerListener(this), this);

		getLogger().info("Finished loading " + getDescription().getFullName());
	}

	public void onDisable() {
		getLogger().info("Finished unloading " + getDescription().getFullName());
	}
}
