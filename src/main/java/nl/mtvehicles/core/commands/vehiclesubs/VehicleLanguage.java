package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.events.JoinEvent;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleLanguage extends MTVehicleSubCommand {
    public VehicleLanguage() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.language")) return true;

        JoinEvent.checkLanguage((Player) sender);

        return true;
    }
}