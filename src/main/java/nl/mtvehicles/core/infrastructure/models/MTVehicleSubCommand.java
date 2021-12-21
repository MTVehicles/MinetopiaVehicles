package nl.mtvehicles.core.infrastructure.models;

import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class MTVehicleSubCommand {
    public CommandSender commandSender;
    public Player player;
    public boolean isPlayer;
    private boolean isPlayerCommand;

    public boolean onExecute(CommandSender sender, Command cmd, String s, String[] args) {
        this.commandSender = sender;
        this.isPlayer = sender instanceof Player;
        this.player = isPlayer ? (Player) sender : null;

        if (isPlayerCommand && !isPlayer) {
            sendMessage(ConfigModule.messagesConfig.getMessage("notForConsole"));
            return true;
        }

        return this.execute(sender, cmd, s, args);
    }

    public abstract boolean execute(CommandSender sender, Command cmd, String s, String[] args);

    public void sendMessage(String message) {
        this.commandSender.sendMessage("" + TextUtils.colorize(message));
    }

    public boolean checkPermission(String permission) {
        if (commandSender.hasPermission(permission)) {
            return true;
        }

        ConfigModule.messagesConfig.sendMessage(commandSender, "noPerms");

        return false;
    }

    public boolean isPlayerCommand() {
        return isPlayerCommand;
    }

    public void setPlayerCommand(boolean playerCommand) {
        isPlayerCommand = playerCommand;
    }
}
