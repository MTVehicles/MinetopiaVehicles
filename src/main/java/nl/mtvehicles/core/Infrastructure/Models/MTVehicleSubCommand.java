package nl.mtvehicles.core.Infrastructure.Models;

import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class MTVehicleSubCommand {
    public CommandSender commandSender;

    public boolean onExecute(CommandSender sender, Command cmd, String s, String[] args) {
        this.commandSender = sender;
        return this.execute(sender, cmd, s, args);
    }

    public abstract boolean execute(CommandSender sender, Command cmd, String s, String[] args);

    public void sendMessage(String message) {
        this.commandSender.sendMessage("" + Text.colorize(message));
    }

    public boolean checkPermission(String permission) {
        if (commandSender.hasPermission(permission)) {
            return true;
        }

        return false;
    }
}
