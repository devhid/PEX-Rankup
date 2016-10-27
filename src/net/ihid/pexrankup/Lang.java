package net.ihid.pexrankup;

import org.bukkit.configuration.file.YamlConfiguration;

public enum Lang {
    NO_PERMISSION("MAIN" + ".no-permission"),
    IMPROPER_USAGE("MAIN" + ".improper-usage"),
    INVALID_SENDER("MAIN" + ".invalid-sender"),
    PLAYER_OFFLINE("MAIN" + ".player-offline");

    private final String path;
    private YamlConfiguration config = RankupPlugin.getPlugin().getConfig();

    Lang(String path) {
        this.path = path;
    }

    public String toString() {
        return config.getString(path);
    }
}
