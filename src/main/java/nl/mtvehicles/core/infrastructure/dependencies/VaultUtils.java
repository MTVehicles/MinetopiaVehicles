package nl.mtvehicles.core.infrastructure.dependencies;

import net.milkbowl.vault.economy.Economy;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Methods for Vault soft-dependency.<br>
 * @warning <b>Do not initialise this class directly. Use {@link DependencyModule#vault} instead.</b>
 */
public class VaultUtils {
    //This must only be called if DependencyModule made sure that Vault is installed.

    private static Economy economy = null;

    /**
     * Default constructor which sets up Vault-linked economy plugin - <b>do not use this.</b><br>
     * Use {@link DependencyModule#vault} instead.
     */
    public VaultUtils(){
        setupEconomy();
    }

    private static void setupEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) return;
        economy = rsp.getProvider();
    }

    /**
     * Check whether an economy plugin is linked.
     * @return True if an economy plugin is set up and linked.
     */
    public boolean isEconomySetUp(){
        return !(economy == null);
    }

    /**
     * Get name of the economy plugin.
     */
    public String getEconomyName(){
        return economy.getName();
    }

    /**
     * Deposit money into player's account
     * @param p Player
     * @param amount Amount of money
     *
     * @return True if the withdrawal was successful, otherwise false (usually due to an unexpected error).
     */
    public boolean depositMoneyPlayer(OfflinePlayer p, double amount){ //true - successful, false - error
        if (!isPriceOk(amount) || !isEconomySetUp()) return false;
        if (!economy.hasAccount(p)) return false;

        return economy.depositPlayer(p, amount).transactionSuccess();
    }

    /**
     * Remove/Withdraw money from player's account
     * @param p Player
     * @param amount Amount of money
     *
     * @return True if the withdrawal was successful, otherwise false (usually due to insufficient funds).
     */
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

    /**
     * Get an amount of money in a string format (e.g. 23.8901 -> '$23,89').
     * This uses format specified by your economy plugin.
     * @param amount Amount of money
     */
    public String getMoneyFormat(double amount){
        return economy.format(amount);
    }

    /**
     * Check whether the price is greater than 0.
     * @param price Price
     * @return True if price is OK
     */
    private boolean isPriceOk(double price){
        return (price > 0);
    }
}
