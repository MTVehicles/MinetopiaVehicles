package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
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

        String pluginVersion = Main.pluginVersion;
        String serverVersion = Bukkit.getVersion();

        sendMessage(String.format("§2Running §aMTVehicles v%s§2.", pluginVersion));
        sendMessage(String.format("§2Your server is running §a%s§2.", serverVersion));
        if (Main.isPreRelease)
            sendMessage(ConfigModule.messagesConfig.getMessage("usingPreRelease"));

        return true;
    }
}
