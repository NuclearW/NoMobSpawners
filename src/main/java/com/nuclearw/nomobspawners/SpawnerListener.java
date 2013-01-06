package com.nuclearw.nomobspawners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnerListener implements Listener {
	private NoMobSpawners plugin;

	public SpawnerListener(NoMobSpawners plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER);
		if(!Config.worlds.contains(event.getLocation().getWorld().getName())) return;

		Location location = event.getLocation();
		World world = event.getLocation().getWorld();

		SCAN: for(int x = location.getBlockX() - 8; x <= location.getBlockX() + 8; x++) {
			for(int z = location.getBlockZ() - 8; z <= location.getBlockZ() + 8; z++) {
				for(int y = location.getBlockY() - 3; y <= location.getBlockY() + 3; y++) {
					if(world.getBlockTypeIdAt(x, y, z) != 52) continue;

					plugin.remove.add(new Location(world, x, y, z));
					break SCAN;
				}
			}
		}

		event.setCancelled(true);
	}
}
