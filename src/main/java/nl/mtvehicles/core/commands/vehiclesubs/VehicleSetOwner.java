package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class VehicleSetOwner extends MTVehicleSubCommand {
    public VehicleSetOwner() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        ItemStack item = player.getInventory().getItemInMainHand();

        boolean playerSetOwner = (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.PUT_ONESELF_AS_OWNER);

        if (!playerSetOwner && !checkPermission("mtvehicles.setowner")) {
            return true;
        }

        if (!isHoldingVehicle()) return true;

        if (args.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.USE_SET_OWNER));
            return true;
        }

        String licensePlate = VehicleUtils.getLicensePlate(item);

        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.VEHICLE_NOT_FOUND));
            return true;
        }

        Player of = Bukkit.getPlayer(args[1]);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.PLAYER_NOT_FOUND));
            return true;
        }

        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);
        assert vehicle != null;

        if ((playerSetOwner || !player.hasPermission("mtvehicles.setowner")) && !vehicle.isOwner(player)) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.NOT_YOUR_CAR));
            return true;
        }

        vehicle.setRiders(new ArrayList<>());
        vehicle.setMembers(new ArrayList<>());
        vehicle.setOwner(of.getUniqueId().toString());
        vehicle.save();

        player.sendMessage(ConfigModule.messagesConfig.getMessage(Message.MEMBER_CHANGE));

        return true;
    }
}
