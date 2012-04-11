package com.nuclearw.nomobspawners;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class NoMobSpawners extends JavaPlugin {
	static String mainDirectory = "plugins" + File.separator + "NoMobSpawners";
	static File configFile = new File(mainDirectory + File.separator + "config");
	static File versionFile = new File(mainDirectory + File.separator + "VERSION");
	static Properties prop = new Properties();
	
	Logger log = Logger.getLogger("Minecraft");
	
	HashMap<World, Boolean> worlds = new HashMap<World, Boolean>();
	ArrayList<Integer> customBlocksList = new ArrayList<Integer>();
	
	public void onEnable() {
		new File(mainDirectory).mkdir();
		
		if(!versionFile.exists()) {
			updateVersion();
		} else {
			String vnum = readVersion();
			if(vnum.equals("0.1")) {
				FileInputStream configIn;
				try {
					configIn = new FileInputStream(configFile);
					prop.load(configIn);
					configIn.close();
					
					configFile.createNewFile();
					FileOutputStream out = new FileOutputStream(configFile);
					prop.put("blocks-strip", "");
					prop.store(out, "Worlds to strip, separate with commas. Leave blocks-strip empty if you only want to remove mobspawners, otherwise separate the blocks you want removed with commas.");
					out.flush();
					out.close();
					prop.clear();
				} catch (FileNotFoundException ex) {
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				updateVersion();
			}
			if(vnum.equals("0.2")) updateVersion();
			if(vnum.equals("0.2.1")) updateVersion();
		}
		
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				FileOutputStream out = new FileOutputStream(configFile);
				prop.put("active-worlds", "");
				prop.put("blocks-strip", "");
				prop.store(out, "Worlds to strip, separate with commas. Leave blocks-strip empty if you only want to remove mobspawners, otherwise separate the blocks you want removed with commas.");
				out.flush();
				out.close();
				prop.clear();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		FileInputStream configIn;
		try {
			configIn = new FileInputStream(configFile);
			prop.load(configIn);
			configIn.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		String activeWorldsStr = prop.getProperty("active-worlds");
		if(activeWorldsStr.contains(",")) {
			String[] activeWorlds = activeWorldsStr.split(",");
			for(String w : activeWorlds) {
				worlds.put(getServer().getWorld(w), true);
			}
		} else {
			worlds.put(getServer().getWorld(activeWorldsStr), true);
		}
		
		if(prop.getProperty("blocks-strip") != null) {
			String customBlocksStr = prop.getProperty("blocks-strip");
			if(customBlocksStr.contains(",")) {
				String[] customBlocks = customBlocksStr.split(",");
				for(String b : customBlocks) {
					try {
						int blockid = Integer.parseInt(b);
						customBlocksList.add(blockid);
					} catch (NumberFormatException ex) {
						log.severe("[NoMobSpawners] Custom block ID: " + b + " could not be parsed");
					}
				}
			} else {
				try {
					int blockid = Integer.parseInt(customBlocksStr);
					customBlocksList.add(blockid);
				} catch (NumberFormatException ex) {
					log.severe("[NoMobSpawners] Custom block ID: " + customBlocksStr + " could not be parsed");
				}
			}
		}
		
		final nmsWorldListener worldListener = new nmsWorldListener(this);

		getServer().getPluginManager().registerEvents(worldListener, this);
		
		log.info("[NoMobSpawners] version "+this.getDescription().getVersion()+" loaded.");
	}
	public void onDisable() {
		log.info("[NoMobSpawners] version "+this.getDescription().getVersion()+" unloaded.");
	}
	public void stripChunk(Chunk chunk) {
		World world = chunk.getWorld();
		if(worlds.isEmpty() || !worlds.containsKey(world) || worlds.get(world) != true) return;
		if(customBlocksList.isEmpty()) {
			int bx = chunk.getX()<<4;
			int bz = chunk.getZ()<<4;
			for(int xx = bx; xx < bx+16; xx++) {
			    for(int zz = bz; zz < bz+16; zz++) {
			        for(int yy = 0; yy < 256; yy++) {
			            int typeId = world.getBlockTypeIdAt(xx, yy, zz);
			            if(typeId == 52) world.getBlockAt(xx,yy,zz).setTypeId(0);
			        }
			    }
			}
		} else {
			int bx = chunk.getX()<<4;
			int bz = chunk.getZ()<<4;
			for(int xx = bx; xx < bx+16; xx++) {
			    for(int zz = bz; zz < bz+16; zz++) {
			        for(int yy = 0; yy < 256; yy++) {
			            int typeId = world.getBlockTypeIdAt(xx, yy, zz);
			            if(customBlocksList.contains(typeId)) world.getBlockAt(xx,yy,zz).setTypeId(0);
			        }
			    }
			}
		}
		
		/*
		if(removeEntities) {
			Entity[] entities = chunk.getEntities();
			for(Entity e : entities) {
				if(e instanceof Painting) ((Painting) e).remove();
			}
		}
		*/
	}
	
	public void updateVersion() {
		try {
			versionFile.createNewFile();
			BufferedWriter vout = new BufferedWriter(new FileWriter(versionFile));
			vout.write(this.getDescription().getVersion());
			vout.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
	}

	public String readVersion() {
		byte[] buffer = new byte[(int) versionFile.length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(versionFile));
			f.read(buffer);
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (f != null) try { f.close(); } catch (IOException ignored) { }
		}
		
		return new String(buffer);
	}
}
