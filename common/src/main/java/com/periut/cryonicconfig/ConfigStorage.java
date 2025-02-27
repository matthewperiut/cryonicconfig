package com.periut.cryonicconfig;

import com.google.gson.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ConfigStorage {
    private final Map<String, Object> configData = new HashMap<>();
    private final String modId; // Store the mod_id

    // Constructor that requires mod_id
    public ConfigStorage(String modId) {
        this.modId = modId;
    }

    // Constructor that loads from a file
    public ConfigStorage(String modId, String filePath) {
        this.modId = modId;
        loadFromFile(filePath);
    }

    // Setters
    public void setInt(String key, int value) {
        configData.put(key, value);
        UtilityCryonicConfig.save();
    }

    public void setDouble(String key, double value) {
        configData.put(key, value);
        UtilityCryonicConfig.save();
    }

    public void setString(String key, String value) {
        configData.put(key, value);
        UtilityCryonicConfig.save();
    }

    public void setBoolean(String key, boolean value) {
        configData.put(key, value);
        UtilityCryonicConfig.save();
    }

    // Getters with default values, using SERVER_CONFIG if available
    public int getInt(String key, int defaultValue) {
        if (UtilityCryonicConfig.SERVER_CONFIG.containsKey(modId) && UtilityCryonicConfig.SERVER_CONFIG.get(modId).configData.containsKey(key)) {
            return UtilityCryonicConfig.SERVER_CONFIG.get(modId).getIntDirect(key, defaultValue);
        }
        return getIntDirect(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        if (UtilityCryonicConfig.SERVER_CONFIG.containsKey(modId) && UtilityCryonicConfig.SERVER_CONFIG.get(modId).configData.containsKey(key)) {
            return UtilityCryonicConfig.SERVER_CONFIG.get(modId).getDoubleDirect(key, defaultValue);
        }
        return getDoubleDirect(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        if (UtilityCryonicConfig.SERVER_CONFIG.containsKey(modId) && UtilityCryonicConfig.SERVER_CONFIG.get(modId).configData.containsKey(key)) {
            return UtilityCryonicConfig.SERVER_CONFIG.get(modId).getStringDirect(key, defaultValue);
        }
        return getStringDirect(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (UtilityCryonicConfig.SERVER_CONFIG.containsKey(modId) && UtilityCryonicConfig.SERVER_CONFIG.get(modId).configData.containsKey(key)) {
            return UtilityCryonicConfig.SERVER_CONFIG.get(modId).getBooleanDirect(key, defaultValue);
        }
        return getBooleanDirect(key, defaultValue);
    }

    // Direct retrieval methods (bypass checking SERVER_CONFIG)
    private int getIntDirect(String key, int defaultValue) {
        if (!configData.containsKey(key)) {
            setInt(key, defaultValue);
        }
        return configData.getOrDefault(key, defaultValue) instanceof Number
                ? ((Number) configData.get(key)).intValue() : defaultValue;
    }

    private double getDoubleDirect(String key, double defaultValue) {
        if (!configData.containsKey(key)) {
            setDouble(key, defaultValue);
        }
        return configData.getOrDefault(key, defaultValue) instanceof Number
                ? ((Number) configData.get(key)).doubleValue() : defaultValue;
    }

    private String getStringDirect(String key, String defaultValue) {
        if (!configData.containsKey(key)) {
            setString(key, defaultValue);
        }
        return configData.getOrDefault(key, defaultValue) instanceof String
                ? (String) configData.get(key) : defaultValue;
    }

    private boolean getBooleanDirect(String key, boolean defaultValue) {
        if (!configData.containsKey(key)) {
            setBoolean(key, defaultValue);
        }
        return configData.getOrDefault(key, defaultValue) instanceof Boolean
                ? (Boolean) configData.get(key) : defaultValue;
    }

    // Save data to a given JSON file
    public void saveToFile(String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(configData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load data from a given JSON file
    public void loadFromFile(String filePath) {
        File configFile = new File(filePath);
        if (!configFile.exists()) return; // If file doesn't exist, no need to load

        try (Reader reader = Files.newBufferedReader(configFile.toPath())) {
            Gson gson = new Gson();
            Map<String, Object> loadedData = gson.fromJson(reader, HashMap.class);
            if (loadedData != null) {
                configData.putAll(loadedData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sync method now includes mod_id in messages
    public void sync(String key, PlayerEntity player) {
        if (!configData.containsKey(key))
            return;

        if (!player.isMainPlayer()) {
            Object o = configData.get(key);
            if (o instanceof Number n) {
                player.sendMessage(Text.of("ccsync:" + modId + ":" + key + ":" + n), false);
            } else if (o instanceof Boolean b) {
                player.sendMessage(Text.of("ccsync:" + modId + ":" + key + ":" + b), false);
            } else if (o instanceof String s) {
                player.sendMessage(Text.of("ccsync:" + modId + ":" + key + ":" + s), false);
            }
        }
    }
}
