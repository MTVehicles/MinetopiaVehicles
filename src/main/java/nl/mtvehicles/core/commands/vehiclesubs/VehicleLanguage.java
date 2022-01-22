package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.LanguageUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleLanguage extends MTVehicleSubCommand {
    public VehicleLanguage() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender.hasPermission("mtvehicles.language") || sender.hasPermission("mtvehicles.admin")) LanguageUtils.openLanguageGUI((Player) sender);
        else ConfigModule.messagesConfig.sendMessage(sender, "noPerms");

        return true;
    }
}