package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Infrastructure.Helpers.MenuUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleRestore extends MTVehicleSubCommand {
    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) {
            return false;
        }

        if (!checkPermission("mtvehicles.restore")) return true;

        if (args.length == 2) {
            return true;
        }

        MenuUtils.restoreId.put("pagina", 1);
        Player p = (Player) sender;
        sendMessage(Main.messagesConfig.getMessage("menuOpen"));
        MenuUtils.restoreCMD(p, 1);

        return true;
    }
}
