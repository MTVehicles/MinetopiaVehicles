package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.DependencyModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleVersion extends MTVehicleSubCommand {
    public VehicleVersion() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.admin")) return true;

        String pluginVersion = VersionModule.pluginVersion;
        String serverVersion = Bukkit.getVersion();

        sendMessage(String.format("§2Running §aMTVehicles v" + pluginVersion + "§2."));
        sendMessage(String.format("§2Your server is running §a" + serverVersion + "§2."));
        if (!DependencyModule.loadedDependencies.isEmpty()) {
            String dependencies = String.join(", ", DependencyModule.loadedDependencies);
            if (DependencyModule.isDependencyEnabled("Vault")) {
                if (!DependencyModule.vault.isEconomySetUp()) dependencies = dependencies.replace("Vault", "§a§mVault§a");
            }
            sendMessage(String.format("§2Loaded dependencies: §a" + dependencies + "§2."));
        } else {
            sendMessage(String.format("§2There are no loaded dependencies."));
        }
        if (VersionModule.isPreRelease)
            sendMessage(String.format(ConfigModule.messagesConfig.getMessage("usingPreRelease")));

        return true;
    }
}
