package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleGiveCar extends MTVehicleSubCommand {
    public VehicleGiveCar() {
        this.setPlayerCommand(false);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!checkPermission("mtvehicles.givecar")) return true;

        if (args.length != 3) {
            sendMessage(Main.messagesConfig.getMessage("useGiveCar"));
            return true;
        }

        Player of = Bukkit.getPlayer(args[1]);

        String carUuid = args[2];

        if (of == null || !of.hasPlayedBefore()) {
            sendMessage(Main.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        ItemStack car = Vehicle.getByDamage(of, carUuid);

        if (car == null){
            sender.sendMessage(Main.messagesConfig.getMessage("giveCarNotFound"));
            return true;
        }
        of.getInventory().addItem(car);
        sender.sendMessage(Main.messagesConfig.getMessage("giveCarSucces").replace("%p%", of.getName()));

        return true;
    }
}
