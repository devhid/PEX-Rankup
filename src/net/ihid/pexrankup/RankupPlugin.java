package net.ihid.pexrankup;

import lombok.Getter;
import net.ihid.pexrankup.commands.RanksCommand;
import net.ihid.pexrankup.commands.RankupCommand;
import net.ihid.pexrankup.commands.SetRankCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RankupPlugin extends JavaPlugin {
    public static Economy economy;

    @Getter
    private static RankupAPI rankupAPI;

    @Getter
    private final PluginConfig rawConfig = new PluginConfig(this, "config.yml");

    public void onEnable() {
        saveDefault();
        loadCommands();

        setupVault();
        rankupAPI = new RankupAPI();
    }

    private void loadCommands() {
        getCommand("rankup").setExecutor(new RankupCommand(this));
        getCommand("ranks").setExecutor(new RanksCommand(this));
        getCommand("setrank").setExecutor(new SetRankCommand(this));
    }

    @Override
    public YamlConfiguration getConfig() {
        return rawConfig.getConfig();
    }

    public static RankupPlugin getPlugin() {
        return JavaPlugin.getPlugin(RankupPlugin.class);
    }

    private void saveDefault() {
        rawConfig.saveDefaultConfig();
    }

    private void setupVault() {
        if(!setupEconomy()) {
            getLogger().severe("Disabled due to lack of Vault dependency.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
    }

    private boolean setupEconomy() {
        if(Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

}
