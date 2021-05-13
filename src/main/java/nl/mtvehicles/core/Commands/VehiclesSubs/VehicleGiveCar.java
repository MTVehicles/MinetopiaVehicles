package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleGiveCar extends MTVehicleSubCommand {
    public VehicleGiveCar() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.givecar")) return true;
        if (args.length != 3) {
            sender.sendMessage(Main.messagesConfig.getMessage("useGiveCar"));
            return true;
        }
        try {
            Integer.parseInt(args[1]);
        } catch (Throwable e) {
            sender.sendMessage(Main.messagesConfig.getMessage("useGiveCar"));
            return false;
        }
        Player of = Bukkit.getPlayer(args[2]);
        if (of == null || !of.hasPlayedBefore()) {
            sender.sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }
        Vehicle.getByDamage(Integer.parseInt(args[1]), of);
        return true;
    }
}
