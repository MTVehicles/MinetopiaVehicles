package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.MenuUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VehicleRestore extends MTVehicleSubCommand {
    public VehicleRestore() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.restore")) return true;

        sendMessage(Main.messagesConfig.getMessage("menuOpen"));
        Player p = (Player) sender;

        if (args.length != 2) {
            MenuUtils.restoreCMD(p, 1, null);
            MenuUtils.restoreUUID.put("uuid", null);
            return true;
        }
        OfflinePlayer of = Bukkit.getPlayer(args[1]);

        if (of == null || !of.hasPlayedBefore()) {
            sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        MenuUtils.restoreCMD(p, 1, of.getUniqueId());
        MenuUtils.restoreUUID.put("uuid", of.getUniqueId());
        MenuUtils.restoreId.put("pagina", 1);
        return true;
    }
}
