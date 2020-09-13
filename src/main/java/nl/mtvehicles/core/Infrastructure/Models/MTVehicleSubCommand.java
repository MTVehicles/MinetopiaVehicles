package nl.mtvehicles.core.Infrastructure.Models;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class MTVehicleSubCommand {
    public abstract boolean execute(CommandSender sender, Command cmd, String s, String[] args);
}
