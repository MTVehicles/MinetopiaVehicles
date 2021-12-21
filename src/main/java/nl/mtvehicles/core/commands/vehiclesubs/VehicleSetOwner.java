package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVehicleSubCommand;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.Main;
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
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        boolean playerSetOwner = ConfigModule.defaultConfig.getConfig().getBoolean("spelerSetOwner");

        if (!playerSetOwner && !checkPermission("mtvehicles.setowner")) {
            return true;
        }

        if (!item.hasItemMeta() || !(NBTUtils.contains(item, "mtvehicles.kenteken"))) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("useSetOwner"));
            return true;
        }

        String licensePlate = NBTUtils.getString(item, "mtvehicles.kenteken");

        if (!Vehicle.existsByPlate(licensePlate)) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("vehicleNotFound"));
            return true;
        }

        Player of = Bukkit.getPlayer(args[1]);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        Vehicle vehicle = Vehicle.getByPlate(licensePlate);
        assert vehicle != null;

        if ((playerSetOwner || !player.hasPermission("mtvehicles.setowner")) && !vehicle.getOwner().equals(player.getUniqueId().toString())) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("notYourCar"));
            return true;
        }

        vehicle.setRiders(new ArrayList<>());
        vehicle.setMembers(new ArrayList<>());
        vehicle.setOwner(of.getUniqueId().toString());
        vehicle.save();

        player.sendMessage(ConfigModule.messagesConfig.getMessage("memberChange"));

        return true;
    }
}
