package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleGiveFuel extends MTVSubCommand {
    public VehicleGiveFuel() {
        this.setPlayerCommand(false);
    }
    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.givefuel")) return true;

        if (arguments.length != 3) {
            sendMessage(Message.USE_GIVE_FUEL);
            return true;
        }

        Player argPlayer = Bukkit.getPlayer(arguments[1]);

        int fuel = 25;


        try{
            fuel = Integer.parseInt(arguments[2]);
        } catch ( Exception e) {
            sendMessage(Message.USE_GIVE_FUEL);
            return true;
        }


        if (argPlayer == null) {
            sendMessage(Message.PLAYER_NOT_FOUND);
            return true;
        }

        ItemStack fuelItem = VehicleFuel.jerrycanItem(fuel, fuel);
        argPlayer.getInventory().addItem(fuelItem);
        sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.GIVE_FUEL_SUCCESS).replace("%p%", argPlayer.getName()).replace("%l%", String.valueOf(fuel)));


        return false;
    }
}
