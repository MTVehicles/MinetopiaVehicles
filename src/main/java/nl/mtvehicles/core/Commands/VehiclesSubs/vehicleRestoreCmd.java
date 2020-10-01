package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.VehiclesUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class vehicleRestoreCmd extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            if (!checkPermission("mtvehicles.restore")) return true;
            VehiclesUtils.restoreId.put("pagina", 1);
            Player p = (Player) sender;
            sendMessage(Main.messagesConfig.getMessage("menuOpen"));
            VehiclesUtils.restoreCMD(p, 1);
        }
        return true;
    }
}
