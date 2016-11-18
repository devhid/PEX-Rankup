package net.devhid.pexrankup;

import lombok.Getter;
import net.devhid.pexrankup.api.RankupAPI;
import net.devhid.pexrankup.commands.RanksCommand;
import net.devhid.pexrankup.commands.RankupCommand;
import net.devhid.pexrankup.commands.ReloadCommand;
import net.devhid.pexrankup.commands.SetRankCommand;
import net.devhid.pexrankup.util.ChatUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class RankupPlugin extends JavaPlugin {
    @Getter
    private static Economy economy;

    @Getter
    private static RankupAPI rankupAPI;

    @Getter
    private RankupManager rankupManager;

    public void onEnable() {
        saveDefaultConfig();
        setupVault();

        rankupManager = new RankupManager(this);
        rankupAPI = new RankupAPI();

        loadCommands();
    }

    public String getPrefix() {
        return (getConfig().getBoolean("MAIN" + ".prefix-enabled"))
                ? ChatUtil.color(getConfig().getString("MAIN" + ".prefix")) : "";
    }

    private void loadCommands() {
        getCommand("pexrankup-rankup").setExecutor(new RankupCommand(this));
        getCommand("pexrankup-ranks").setExecutor(new RanksCommand(this));
        getCommand("pexrankup-setrank").setExecutor(new SetRankCommand(this));
        getCommand("pexrankup-reload").setExecutor(new ReloadCommand(this));
    }

    private void setupVault() {
        if(!setupEconomy()) {
            getLogger().severe("Disabled due to lack of Vault dependency.");
            getServer().getPluginManager().disablePlugin(this);
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

    public static RankupPlugin getPlugin() {
        return JavaPlugin.getPlugin(RankupPlugin.class);
    }
}