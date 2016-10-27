package net.devhid.pexrankup;

import net.devhid.pexrankup.api.RankupAPI;
import net.devhid.pexrankup.commands.RanksCommand;
import net.devhid.pexrankup.commands.RankupCommand;
import net.devhid.pexrankup.commands.SetRankCommand;
import net.devhid.pexrankup.util.ChatUtil;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class RankupPlugin extends JavaPlugin {
    private static Economy economy;
    private static RankupAPI rankupAPI;

    public void onEnable() {
        saveDefaultConfig();
        loadCommands();

        setupVault();
        rankupAPI = new RankupAPI();
    }

    public String getPrefix() {
        return (getConfig().getBoolean("MAIN" + ".prefix-enabled")) ? ChatUtil.color(getConfig().getString("MAIN" + ".prefix")) : "";
    }

    private void loadCommands() {
        getCommand("rankup").setExecutor(new RankupCommand(this));
        getCommand("ranks").setExecutor(new RanksCommand(this));
        getCommand("setrank").setExecutor(new SetRankCommand(this));
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

    public static Economy getEconomy() {
        return economy;
    }

    public static RankupAPI getRankupAPI() {
        return rankupAPI;
    }
}