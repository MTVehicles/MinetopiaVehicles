package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleGiveVoucher extends MTVehicleSubCommand {
    public VehicleGiveVoucher() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.givevoucher")) return true;

        if (args.length != 3) {
            sendMessage(Main.messagesConfig.getMessage("useGiveVoucher"));
            return true;
        }

        try {
            Integer.parseInt(args[1]);
        } catch (Throwable e) {
            sendMessage(Main.messagesConfig.getMessage("useGiveVoucher"));
            return false;
        }

        Player of = Bukkit.getPlayer(args[2]);

        if (of == null || !of.hasPlayedBefore()) {
            sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        Vehicle.getVoucher(Integer.parseInt(args[1]), of);

        return true;
    }
}
