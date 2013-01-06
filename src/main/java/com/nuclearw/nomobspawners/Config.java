package com.nuclearw.nomobspawners;

import java.io.File;
import java.util.HashSet;

public class Config {
	public static HashSet<String> worlds = new HashSet<String>();

	public static void load(NoMobSpawners plugin) {
		if(!new File(plugin.getDataFolder(), "config.yml").exists()) {
			plugin.saveDefaultConfig();
		}

		String worldsStrip = plugin.getConfig().getString("active-worlds");

		if(worldsStrip == null || worldsStrip.isEmpty()) return;

		String[] worldsSplit = worldsStrip.split(",");

		if(worldsSplit.length == 0) return;

		for(String world : worldsSplit) {
			worlds.add(world);
			plugin.getLogger().info("Added world: " + world);
		}
	}
}
