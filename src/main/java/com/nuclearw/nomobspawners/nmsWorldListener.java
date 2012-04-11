package com.nuclearw.nomobspawners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

public class nmsWorldListener implements Listener {
	public static NoMobSpawners plugin;
	
	public nmsWorldListener(NoMobSpawners instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		plugin.stripChunk(event.getChunk());
	}
	
	@EventHandler
	public void onChunkPopulated(ChunkPopulateEvent event) {
		plugin.stripChunk(event.getChunk());
	}
}
