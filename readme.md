# ![Cryonic Config](logo/1280x720logo.png)
- Cross-version easy to port config tool with minimal dependencies  
- Specifically targeting ease of use and portability
- Cryonic because it's cool as hell

# API
Simple Usage:
```java
// You can store or use on the fly, this can be called anywhere
ConfigStorage config = CryonicConfig.getConfig("mod_id");

// You can store ints, doubles, booleans, and strings
// By getting them, you are setting their default value too
// getter format (variable name, default value)
// You cannot reuse variable names! They will be overridden.
config.getInt("varName", 3);
config.getDouble("name", 3.3);
config.getBoolean("var", true);
config.getString("str", "Geronimo!")

// The variable will exist locally on client and server
// If you want a connected server player to use a server config
// You must call, making client use the server's value 
config.sync("varName", playerEntity);

// Feel free to use this format:
CryonicConfig.getConfig("mod_id").getInt("varName", 3);

// You can set variables manually, instead of letting get generate them
// This is also useful for overriding old values
config.setInt("varName", 3);
config.setDouble("name", 3.3);
config.setBoolean("var", true);
config.setString("str", "Geronimo!")
```

# Explanation of Functionality
- This will store jsons in {minecraft_dir}/config as "mod_id.json"
- All config files will be read by dictionary in "cryonic_config.json" on early init
- Configs are only configurable by editing the jsons directly, no plans of GUI config
- For ease of portability, synced variables are sent directly to a player in chat, and intercepted
- Hashmaps are used to look up Str to ConfigStorage and Str to Obj (variable)