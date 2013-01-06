package com.nuclearw.nomobspawners;

import java.io.File;
import java.util.HashSet;

public class Config {
	public static HashSet<String> worlds = new HashSet<String>();

	public static void load(NoMobSpawners plugin) {
		if(!new File(plugin.getDataFolder(), "config.yml").exists()) {
			plugin.saveDefaultConfig();
		}

		String worldsStrip = plugin.getConfig().getString("rain-warning-msg");

		for(String world : worldsStrip.split(",")) {
			worlds.add(world);
		}
	}
}
