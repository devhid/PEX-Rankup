package net.devhid.pexrankup;

import org.bukkit.configuration.file.FileConfiguration;

public enum Lang {
    NO_PERMISSION("MAIN" + ".no-permission"),
    IMPROPER_USAGE("MAIN" + ".improper-usage"),
    INVALID_SENDER("MAIN" + ".invalid-sender"),
    PLAYER_OFFLINE("MAIN" + ".player-offline");

    private final String path;
    private final FileConfiguration config;

    Lang(String path) {
        this.path = path;
        this.config = RankupPlugin.getPlugin().getConfig();
    }

    public String toString() {
        return config.getString(path);
    }
}