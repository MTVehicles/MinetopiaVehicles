package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract class for the plugin's commands
 */
public abstract class MTVCommand implements CommandExecutor {
    /**
     * The command sender
     */
    public CommandSender commandSender;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] strings) {
        this.commandSender = commandSender;
        return this.execute(commandSender, command, label, strings);
    }

    /**
     * Code executed on use of the command
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return True if successful
     */
    public abstract boolean execute(CommandSender sender, Command cmd, String label, String[] args);

    /**
     * Send a message to the command sender
     * @param message Message
     */
    public void sendMessage(String message) {
        this.commandSender.sendMessage(TextUtils.colorize(message));
    }
}
