package nl.mtvehicles.core.infrastructure.dependencies;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;
import nl.mtvehicles.core.Main;

public class VaultUtils {
    //This is only called if DependencyModule made sure that Vault is installed.

    private static Economy economy = null;

    public VaultUtils(){
        setupEconomy();
    }

    private static void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Main.instance.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return;
        economy = rsp.getProvider();
    }

    public boolean isEconomySetUp(){
        return !(economy == null);
    }

    public String getEconomyName(){
        return economy.getName();
    }

    public boolean depositMoneyPlayer(OfflinePlayer p, double price){ //true - successful, false - error
        if (!isPriceOk(price) || !isEconomySetUp()) return false;
        if (!economy.hasAccount(p)) return false;

        return economy.depositPlayer(p, price).transactionSuccess();
    }

    public boolean withdrawMoneyPlayer(OfflinePlayer p, double price){ //true - payed, false - didn't
        if (!isPriceOk(price) || !isEconomySetUp()) return false;
        if (!economy.hasAccount(p)) return false;
        if (!economy.has(p, price)) return false;

        return economy.withdrawPlayer(p, price).transactionSuccess();
    }

    private boolean isPriceOk(double price){ //returns true if ok
        return (price > 0);
    }
}