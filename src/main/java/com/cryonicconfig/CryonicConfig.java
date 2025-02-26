package com.cryonicconfig;

import static com.cryonicconfig.UtilityCryonicConfig.LOCAL_CONFIG;
import static com.cryonicconfig.UtilityCryonicConfig.SERVER_CONFIG;

public class CryonicConfig {
    public static ConfigStorage getConfig(String mod_id) {
        return SERVER_CONFIG.getOrDefault(mod_id, LOCAL_CONFIG.computeIfAbsent(mod_id, k -> new ConfigStorage(mod_id)));
    }
}
