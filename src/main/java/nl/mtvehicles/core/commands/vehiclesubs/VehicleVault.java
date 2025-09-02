package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;

/**
 * <b>/vehicle vault</b> - get information about the Vault dependency.
 */
public class VehicleVault extends MTVSubCommand {
    public VehicleVault() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.admin")) return true;

        if (!DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
            sendMessage(Message.NO_VAULT_DEPENDENCY);
            return true;
        }

        // Command '/mtv vault setup'
        if (arguments.length > 1 && arguments[1].equalsIgnoreCase("setup")) {
            if (DependencyModule.vault.isEconomySetUp()) {
                sendMessage(Message.VAULT_SETUP_ALREADY_HOOKED);
                return true;
            }

            if (DependencyModule.vault.retryEconomySetup()) {
                String msg = TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VAULT_SETUP_SUCCESS));
                sendMessage(msg.replace("{plugin}", DependencyModule.vault.getEconomyName()));
            } else {
                sendMessage(Message.VAULT_SETUP_FAIL);
            }
            return true;
        }

        // Command '/mtv vault'
        if (DependencyModule.vault.isEconomySetUp()) {
            String msg = TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.VAULT_INFO));
            sendMessage(msg.replace("{plugin}", DependencyModule.vault.getEconomyName()));
        } else {
            sendMessage(Message.VAULT_NO_ECONOMY_FOUND);
        }

        return true;
    }
}
