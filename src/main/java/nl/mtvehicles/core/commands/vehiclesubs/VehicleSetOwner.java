package nl.mtvehicles.core.commands.vehiclesubs;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
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
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        boolean playerSetOwner = ConfigModule.defaultConfig.getConfig().getBoolean("spelerSetOwner");

        if (!playerSetOwner && !checkPermission("mtvehicles.setowner")) {
            return true;
        }

        if (!item.hasItemMeta() || !(new NBTItem(item)).hasKey("mtvehicles.kenteken")) {
            sendMessage(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("noVehicleInHand")));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("useSetOwner"));
            return true;
        }

        String licensePlate = VehicleUtils.getLicensePlate(item);

        if (!VehicleUtils.existsByLicensePlate(licensePlate)) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("vehicleNotFound"));
            return true;
        }

        Player of = Bukkit.getPlayer(args[1]);

        if (of == null || !of.hasPlayedBefore()) {
            player.sendMessage(ConfigModule.messagesConfig.getMessage("playerNotFound"));
            return true;
        }

        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);
        assert vehicle != null;

        if ((playerSetOwner || !player.hasPermission("mtvehicles.setowner")) && !vehicle.isOwner(player)) {
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
