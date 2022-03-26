package nl.mtvehicles.core.infrastructure.models;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            sendMessage(ConfigModule.messagesConfig.getMessage(Message.NOT_FOR_CONSOLE));
            return true;
        }

        return this.execute(sender, cmd, s, args);
    }

    public abstract boolean execute(CommandSender sender, Command cmd, String s, String[] args);

    public void sendMessage(String message) {
        this.commandSender.sendMessage(TextUtils.colorize(message));
    }

    public boolean checkPermission(String permission) {
        if (commandSender.hasPermission(permission)) {
            return true;
        }

        ConfigModule.messagesConfig.sendMessage(commandSender, Message.NO_PERMISSION);

        return false;
    }

    public boolean isPlayerCommand() {
        return isPlayerCommand;
    }

    public void setPlayerCommand(boolean playerCommand) {
        isPlayerCommand = playerCommand;
    }

    protected boolean isHoldingVehicle(){
        ItemStack item = player.getInventory().getItemInMainHand();
        if (!item.hasItemMeta() || !(new NBTItem(item)).hasKey("mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.NO_VEHICLE_IN_HAND)));
            return false;
        }
        return true;
    }
}
