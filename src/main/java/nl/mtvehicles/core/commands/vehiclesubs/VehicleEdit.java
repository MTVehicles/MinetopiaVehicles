package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

/**
 * <b>/vehicle edit</b> - edit held vehicle's specifications (in a GUI).
 */
public class VehicleEdit extends MTVSubCommand {

    public VehicleEdit() {
        this.setPlayerCommand(true);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.edit"))
            return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle())
            return true;

        ConfigModule.configList.forEach(MTVConfig::reload);
        if (arguments.length == 1) {
            sendMessage(Message.MENU_OPEN);
            editMenu(player, item);
        } else {

            Player targetPlayer = player;
            int argOffset = 0;

            Player specifiedPlayer = Bukkit.getPlayer(arguments[1]);
            if (specifiedPlayer != null) {

                if (!player.hasPermission("mtvehicles.admin") && !player.equals(specifiedPlayer)) {
                    sendMessage(Message.NO_PERMISSION);
                    return true;
                }
                targetPlayer = specifiedPlayer;
                argOffset = 1;
            }

            if (arguments.length == 3 + argOffset) {
                return handleParameterEdit(targetPlayer, arguments[1 + argOffset], arguments[2 + argOffset]);
            } else {
                ConfigModule.messagesConfig.sendMessage(player, Message.USE_EDIT);
                return true;
            }
        }

        return true;
    }

    /**
     * Handle parameter editing via command
     * 
     * @param targetPlayer Player whose vehicle is being edited
     * @param paramName    The parameter name to edit
     * @param paramValue   The new value for the parameter
     * @return true if successful
     */
    private boolean handleParameterEdit(Player targetPlayer, String paramName, String paramValue) {
        final ItemStack item = targetPlayer.getInventory().getItemInMainHand();

        String licensePlate = VehicleUtils.getLicensePlate(item);
        boolean success = false;

        // Call the appropriate edit method based on parameter name
        switch (paramName.toLowerCase()) {
            case "licenseplate":
                success = editLicensePlate(targetPlayer, licensePlate, paramValue);
                break;
            case "name":
                success = editName(targetPlayer, licensePlate, paramValue);
                break;
            case "fuel":
                success = editFuel(targetPlayer, licensePlate, paramValue);
                break;
            case "fuelusage":
                success = editFuelUsage(targetPlayer, licensePlate, paramValue);
                break;
            case "trunkrows":
                success = editTrunkRows(targetPlayer, licensePlate, paramValue);
                break;
            case "accelerationspeed":
                success = editAccelerationSpeed(targetPlayer, licensePlate, paramValue);
                break;
            case "maxspeed":
                success = editMaxSpeed(targetPlayer, licensePlate, paramValue);
                break;
            case "brakingspeed":
                success = editBrakingSpeed(targetPlayer, licensePlate, paramValue);
                break;
            case "frictionspeed":
                success = editFrictionSpeed(targetPlayer, licensePlate, paramValue);
                break;
            case "maxspeedbackwards":
                success = editMaxSpeedBackwards(targetPlayer, licensePlate, paramValue);
                break;
            case "rotationspeed":
                success = editRotationSpeed(targetPlayer, licensePlate, paramValue);
                break;
            default:
                ConfigModule.messagesConfig.sendMessage(player, Message.INVALID_INPUT);
                return true;
        }

        if (success) {
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
        }

        return true;
    }

    /**
     * Edit vehicle's license plate
     * 
     * @param player          Player who holds the vehicle
     * @param licensePlate    Current license plate
     * @param newLicensePlate New license plate
     * @return true if successful
     */
    public static boolean editLicensePlate(Player player, String licensePlate, String newLicensePlate) {
        if (ConfigModule.vehicleDataConfig.get(newLicensePlate, VehicleDataConfig.Option.SKIN_ITEM) != null) {
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_FAILED_DUP_LICENSE);
            return false;
        }

        for (VehicleDataConfig.Option option : VehicleDataConfig.Option.values()) {
            Object value = ConfigModule.vehicleDataConfig.get(licensePlate, option);
            if (value != null) {
                ConfigModule.vehicleDataConfig.set(newLicensePlate, option, value);
            }
        }

        player.getInventory().setItemInMainHand(ItemUtils.getVehicleItem(
                ItemUtils.getMaterial(ConfigModule.vehicleDataConfig
                        .get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                ConfigModule.vehicleDataConfig.getDamage(licensePlate),
                (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING),
                ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(),
                newLicensePlate));

        ConfigModule.vehicleDataConfig.delete(licensePlate);
        return true;
    }

    /**
     * Edit vehicle's name
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param newName      New vehicle name
     * @return true if successful
     */
    public static boolean editName(Player player, String licensePlate, String newName) {
        ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.NAME, newName);
        ConfigModule.vehicleDataConfig.save();

        player.getInventory().setItemInMainHand(ItemUtils.getVehicleItem(
                ItemUtils.getMaterial(ConfigModule.vehicleDataConfig
                        .get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                ConfigModule.vehicleDataConfig.getDamage(licensePlate),
                (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING),
                newName,
                licensePlate));

        return true;
    }

    /**
     * Edit vehicle's fuel amount
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param fuelStr      New fuel amount as string
     * @return true if successful
     */
    public static boolean editFuel(Player player, String licensePlate, String fuelStr) {
        try {
            Integer fuel = Integer.parseInt(fuelStr);
            if (fuel > 100) {
                ConfigModule.messagesConfig.sendMessage(player, Message.FUEL_TOO_HIGH);
                return false;
            }

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, Double.parseDouble(fuelStr));
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_INTEGER);
            return false;
        }
    }

    /**
     * Edit vehicle's fuel usage
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param fuelUsageStr New fuel usage as string
     * @return true if successful
     */
    public static boolean editFuelUsage(Player player, String licensePlate, String fuelUsageStr) {
        try {
            double fuelUsage = Double.parseDouble(fuelUsageStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_USAGE, fuelUsage);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }

    /**
     * Edit vehicle trunk's number of rows
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param rowsStr      New number of rows as string
     * @return true if successful
     */
    public static boolean editTrunkRows(Player player, String licensePlate, String rowsStr) {
        try {
            int rows = Integer.parseInt(rowsStr);

            if (rows < 1 || rows > 6) {
                ConfigModule.messagesConfig.sendMessage(player, Message.INVALID_INPUT);
                return false;
            }

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ROWS, rows);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_INTEGER);
            return false;
        }
    }

    /**
     * Edit vehicle's acceleration speed
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param speedStr     New acceleration speed as string
     * @return true if successful
     */
    public static boolean editAccelerationSpeed(Player player, String licensePlate, String speedStr) {
        try {
            double speed = Double.parseDouble(speedStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.ACCELERATION_SPEED, speed);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }

    /**
     * Edit vehicle's maximum speed
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param speedStr     New maximum speed as string
     * @return true if successful
     */
    public static boolean editMaxSpeed(Player player, String licensePlate, String speedStr) {
        try {
            double speed = Double.parseDouble(speedStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.MAX_SPEED, speed);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }

    /**
     * Edit vehicle's braking speed
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param speedStr     New braking speed as string
     * @return true if successful
     */
    public static boolean editBrakingSpeed(Player player, String licensePlate, String speedStr) {
        try {
            double speed = Double.parseDouble(speedStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED, speed);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }

    /**
     * Edit vehicle's friction speed
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param speedStr     New friction speed as string
     * @return true if successful
     */
    public static boolean editFrictionSpeed(Player player, String licensePlate, String speedStr) {
        try {
            double speed = Double.parseDouble(speedStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED, speed);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }

    /**
     * Edit vehicle's maximum backwards speed
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param speedStr     New maximum backwards speed as string
     * @return true if successful
     */
    public static boolean editMaxSpeedBackwards(Player player, String licensePlate, String speedStr) {
        try {
            double speed = Double.parseDouble(speedStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS, speed);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }

    /**
     * Edit vehicle's rotation speed
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param speedStr     New rotation speed as string
     * @return true if successful
     */
    public static boolean editRotationSpeed(Player player, String licensePlate, String speedStr) {
        try {
            int speed = Integer.parseInt(speedStr);

            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED, speed);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_INTEGER);
            return false;
        }
    }

    /**
     * Open /vehicle edit GUI menu to a player
     * @param p Player
     * @param item Vehicle item
     */
    public static void editMenu(Player p, ItemStack item) {
        String licensePlate = VehicleUtils.getLicensePlate(item);
        MessagesConfig msg = ConfigModule.messagesConfig;
        Inventory inv = Bukkit.createInventory(null, 27, InventoryTitle.VEHICLE_EDIT_MENU.getStringTitle());
        inv.setItem(10, ItemUtils.getMenuCustomItem(
                ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                "mtcustom",
                ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NBT_VALUE),
                msg.getMessage(Message.VEHICLE_SETTINGS),
                ConfigModule.vehicleDataConfig.getDamage(licensePlate),
                ""
        ));
        inv.setItem(11, ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE, msg.getMessage(Message.FUEL_SETTINGS), 58, ""));
        inv.setItem(12, ItemUtils.getMenuItem(Material.CHEST, 1, msg.getMessage(Message.TRUNK_SETTINGS), ""));
        inv.setItem(13, ItemUtils.getMenuItem(Material.PAPER, 1, msg.getMessage(Message.MEMBER_SETTINGS), ""));
        inv.setItem(14, ItemUtils.getMenuItem("LIME_STAINED_GLASS", "STAINED_GLASS", (short) 5, 1, msg.getMessage(Message.SPEED_SETTINGS), ""));
        inv.setItem(16, ItemUtils.getMenuItem(Material.BARRIER, 1, msg.getMessage(Message.DELETE_VEHICLE), msg.getMessage(Message.DELETE_WARNING_LORE)));
        p.openInventory(inv);
    }
}
