package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.PluginVersion;
import nl.mtvehicles.core.infrastructure.enums.SoftDependency;
import nl.mtvehicles.core.infrastructure.helpers.PluginUpdater;
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
        String isLatest = (PluginUpdater.isLatestVersion() && !PluginVersion.getPluginVersion().isDev()) ? " (latest)" : "";
        String serverVersion = Bukkit.getVersion();

        sendMessage(String.format("§2Running §aMTVehicles v%s§2%s.", pluginVersion, isLatest));
        sendMessage(String.format("§2Your server is running §a%s§2.", serverVersion));
        if (!DependencyModule.loadedDependencies.isEmpty()) {
            String dependencies = "";
            int numberOfDependencies = 0;
            for (SoftDependency dependency: DependencyModule.loadedDependencies) {
                if (numberOfDependencies == 0) dependencies += dependency.getName();
                else dependencies += ", " + dependency.getName();
                numberOfDependencies++;
            }
            if (DependencyModule.isDependencyEnabled(SoftDependency.VAULT)) {
                if (!DependencyModule.vault.isEconomySetUp()) dependencies = dependencies.replace("Vault", "§a§mVault§a");
            }
            sendMessage(String.format("§2Loaded dependencies (%s§2): §a%s§2.", numberOfDependencies, dependencies));
        } else {
            sendMessage(String.format("§2There are no loaded dependencies."));
        }

        if (VersionModule.isPreRelease) {
            sendMessage("§e-----");
            sendMessage(String.format(ConfigModule.messagesConfig.getMessage(Message.USING_PRE_RELEASE)));
        }

        return true;
    }
}
