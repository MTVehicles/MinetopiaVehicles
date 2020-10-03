package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class VehicleReload extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.reload")) return true;

        Bukkit.getLogger().info("Reload config files..");
        Main.configList.forEach(ConfigUtils::reload);
        Bukkit.getLogger().info("Files loaded!");
        sendMessage(Main.messagesConfig.getMessage("reloadSuccesvol"));

        return true;
    }
}
