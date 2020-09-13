package nl.mtvehicles.core.Infrastructure.Models;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class MTVehicleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }

    public abstract boolean execute(CommandSender sender, Command cmd, String s, String[] args);
}
