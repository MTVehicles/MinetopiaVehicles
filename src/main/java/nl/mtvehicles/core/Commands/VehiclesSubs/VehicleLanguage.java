package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Events.JoinEvent;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleLanguage extends MTVehicleSubCommand {
    public VehicleLanguage() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.reload")) return true;

        JoinEvent.checkLanguage((Player) sender);

        return true;
    }
}
