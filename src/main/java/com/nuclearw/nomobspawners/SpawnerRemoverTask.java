package com.nuclearw.nomobspawners;

import org.bukkit.Location;

public class SpawnerRemoverTask implements Runnable {
	private NoMobSpawners plugin;

	public SpawnerRemoverTask(NoMobSpawners plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		Location remove = plugin.remove.poll();

		if(remove == null) return;

		remove.getBlock().setTypeId(0);
	}
}
