package nl.mtvehicles.core.infrastructure.dependencies;

import net.milkbowl.vault.economy.Economy;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultUtils {
    //This is only called if DependencyModule made sure that Vault is installed.

    private static Economy economy = null;

    public VaultUtils(){
        setupEconomy();
    }

    private static void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return;
        economy = rsp.getProvider();
    }

    public boolean isEconomySetUp(){
        return !(economy == null);
    }

    public String getEconomyName(){
        return economy.getName();
    }

    public boolean depositMoneyPlayer(OfflinePlayer p, double amount){ //true - successful, false - error
        if (!isPriceOk(amount) || !isEconomySetUp()) return false;
        if (!economy.hasAccount(p)) return false;

        return economy.depositPlayer(p, amount).transactionSuccess();
    }

    public boolean withdrawMoneyPlayer(OfflinePlayer p, double amount){ //true - payed, false - didn't
        if (!isPriceOk(amount) || !isEconomySetUp()) return false;
        if (!economy.hasAccount(p)) return false;
        if (!economy.has(p, amount)){
            if (p.isOnline()) ConfigModule.messagesConfig.sendMessage(p.getPlayer(), Message.INSUFFICIENT_FUNDS);
            return false;
        }

        if (economy.withdrawPlayer(p, amount).transactionSuccess()){
            if (p.isOnline()) p.getPlayer().sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.TRANSACTION_SUCCESSFUL), DependencyModule.vault.getMoneyFormat(amount)));
            return true;
        }
        return false;
    }

    public String getMoneyFormat(double amount){
        return economy.format(amount);
    }

    private boolean isPriceOk(double price){ //returns true if ok
        return (price > 0);
    }
}