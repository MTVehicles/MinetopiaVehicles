package nl.mtvehicles.core.Infrastructure.Models;

import nl.mtvehicles.core.Infrastructure.Helpers.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class MTVehicleCommand implements CommandExecutor {
    public CommandSender commandSender;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        this.commandSender = commandSender;
        return this.execute(commandSender, command, s, strings);
    }

    public abstract boolean execute(CommandSender sender, Command cmd, String s, String[] args);

    public void sendMessage(String message) {
        this.commandSender.sendMessage("" + Text.colorize(message));
    }
}

