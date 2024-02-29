package net.kokoricraft.giftbox.objects;

import net.kokoricraft.giftbox.GiftBox;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

public class NekoConfig {
	private final File configFile;
	private FileConfiguration config;
	private boolean needUpdate = false;
	private boolean hasDefault = false;

	public NekoConfig(String path, GiftBox plugin) {
		this(plugin.getDataFolder() + File.separator+ path, path, plugin);
	}

	public NekoConfig(GiftBox plugin, String path) {
		this(path, path, plugin);
	}

	public NekoConfig(String path, String savePath, GiftBox plugin){
		configFile = new File(path);
		config = YamlConfiguration.loadConfiguration(configFile);

		if(configFile.exists()) return;

		try{
			plugin.saveResource(savePath, false);
			config = YamlConfiguration.loadConfiguration(configFile);
			hasDefault = true;
		}catch(Exception ignored){}
	}

	public Object get(String path, Object def) {
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.get(path, def);
	}
	
	public String getString(String path, String def) {
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getString(path, def);
	}

	public int getInt(String path, int def) {
		if(!config.contains(path)) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getInt(path, def);
	}
	
	public double getDouble(String path, double def) {
		if(!config.contains(path)) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getDouble(path, def);
	}
	
	public List<String> getStringList(String path, List<String> def){
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getStringList(path);
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		return config.getConfigurationSection(path);
	}
	
	public Boolean getBoolean(String path, Boolean def) {
		if(!config.contains(path) && def != null) {
			config.set(path, def);
			this.needUpdate = true;
		}
		return config.getBoolean(path, def);
	}
	
	public Object getObject(String path, Class<?> clazz) {
		return config.getObject(path, clazz);
	}
	
	public String getString(String path) {
		return config.getString(path);
	}
		
	public int getInt(String path) {
		return config.getInt(path);
	}
	
	public double getDouble(String path) {
		return config.getDouble(path);
	}
	
	public Boolean getBoolean(String path) {
		return config.getBoolean(path);
	}
	
	public List<String> getStringList(String path){
		return config.getStringList(path);
	}
	
	public void set(String path, Object object) {
		config.set(path, object);
	}
	
	public Boolean contains(String paht) {
		return config.contains(paht);
	}
	
	public void update() {
		if(needUpdate) {
			saveConfig();
			this.needUpdate = false;
		}
	}
	
	public void forceUpdate() {
		saveConfig();
		this.needUpdate = false;
	}
	
	public Boolean hasDefault() {
		return this.hasDefault;
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public boolean exists() {
		return this.configFile.exists();
	}
	
	public void saveConfig() {
        try {
            this.config.save(this.configFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public boolean delete() {
		return configFile.delete();
	}

	public String getPath() {
		return configFile.getPath();
	}

	public static void saveFile(String from, String to, GiftBox plugin){
		InputStream inputStream = plugin.getResource(from);
		if (inputStream == null)
			return;

		File outFile = new File(to);

		if(outFile.exists())
			return;

		streamToFile(inputStream, outFile);
	}

	public static void streamToFile(InputStream inputStream, File outFile){
		try{
			OutputStream outputStream = new FileOutputStream(outFile);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.close();
			inputStream.close();
		}catch (Exception ignored){}
	}

	public String getName(){
		return configFile.getName().split(Pattern.quote("."))[0];
	}
}
