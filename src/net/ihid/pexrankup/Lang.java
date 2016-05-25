package net.ihid.pexrankup;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Created by Mikey on 5/24/2016.
 */
public enum Lang {
    NO_PERMISSION("MAIN" + ".no-permission"),
    IMPROPER_USAGE("MAIN" + ".improper-usage"),
    INVALID_SENDER("MAIN" + ".invalid-sender"),
    PLAYER_OFFLINE("MAIN" + ".player-offline");

    private final String path;
    private YamlConfiguration config = RankupPlugin.i.getConfig();

    Lang(String path) {
        this.path = path;
    }

    public String toString() {
        return config.getString(path);
    }
}
