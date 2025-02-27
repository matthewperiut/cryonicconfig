package com.periut.cryonicconfig;

import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public final class UtilityCryonicConfig {
    public static final String MOD_ID = "cryonicconfig";

    // Map to store all mod configs
    public static final HashMap<String, ConfigStorage> SERVER_CONFIG = new HashMap<>();
    public static final HashMap<String, ConfigStorage> LOCAL_CONFIG = new HashMap<>();

    // Minecraft's config directory
    private static String dir;

    public static void init(String dir) {
        UtilityCryonicConfig.dir = dir + "/config"; // Ensure configs are stored inside "config/"
        File configDir = new File(UtilityCryonicConfig.dir);

        // Ensure the config directory exists
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        File mainConfigFile = new File(configDir, "cryonicconfig.json");

        if (mainConfigFile.exists()) {
            // Load existing mappings of mod IDs to config file names (not full paths!)
            try (Reader reader = Files.newBufferedReader(mainConfigFile.toPath())) {
                Gson gson = new Gson();
                Map<String, String> configMappings = gson.fromJson(reader, HashMap.class);

                if (configMappings != null) {
                    for (Map.Entry<String, String> entry : configMappings.entrySet()) {
                        String modId = entry.getKey();
                        String configFileName = entry.getValue();         // e.g. "my_mod.json"
                        String fullPath = UtilityCryonicConfig.dir + "/" + configFileName;

                        // Create a ConfigStorage that actually loads the data
                        ConfigStorage configStorage = new ConfigStorage(modId);
                        configStorage.loadFromFile(fullPath);

                        LOCAL_CONFIG.put(modId, configStorage);
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, String> configMappings = new HashMap<>();

        for (Map.Entry<String, ConfigStorage> entry : LOCAL_CONFIG.entrySet()) {
            String modId = entry.getKey();
            ConfigStorage configStorage = entry.getValue();

            // Ensure we only store the filename, not the full path
            String configFileName = modId + ".json";
            configMappings.put(modId, configFileName);

            // Save individual mod config file in "config/"
            String fullPath = UtilityCryonicConfig.dir + "/" + configFileName;
            configStorage.saveToFile(fullPath);
        }

        // Save the cryonicconfig.json which maps mod IDs to filenames
        try (Writer writer = new FileWriter(UtilityCryonicConfig.dir + "/cryonicconfig.json")) {
            gson.toJson(configMappings, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Called when a sync message is received
    public static void handleSyncMessage(String mod_id, String key, String value) {
        ConfigStorage serverConfig = SERVER_CONFIG.computeIfAbsent(mod_id, k -> new ConfigStorage(mod_id));

        // Determine the value type and store it
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            serverConfig.setBoolean(key, Boolean.parseBoolean(value));
        } else {
            try {
                if (value.contains(".")) {
                    serverConfig.setDouble(key, Double.parseDouble(value));
                } else {
                    serverConfig.setInt(key, Integer.parseInt(value));
                }
            } catch (NumberFormatException e) {
                serverConfig.setString(key, value);
            }
        }
    }
}
