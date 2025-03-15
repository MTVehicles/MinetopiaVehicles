package nl.mtvehicles.core.commands.vehiclesubs;

import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.TriFunction;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.models.MTVSubCommand;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>/vehicle edit</b> - edit held vehicle's specifications (in a GUI).
 */
public class VehicleEdit extends MTVSubCommand {

    private static final Map<String, TriFunction<Player, String, String, Boolean>> EDIT_FUNCTIONS = new HashMap<>();

    public static List<String> getEditCommands() {
        return new ArrayList<>(EDIT_FUNCTIONS.keySet());
    }

    public VehicleEdit() {
        this.setPlayerCommand(false);
        EDIT_FUNCTIONS.put("licenseplate", VehicleEdit::editLicensePlate);
        EDIT_FUNCTIONS.put("name", VehicleEdit::editName);
        EDIT_FUNCTIONS.put("fuel", VehicleEdit::editFuel);
        EDIT_FUNCTIONS.put("fuelusage", VehicleEdit::editFuelUsage);
        EDIT_FUNCTIONS.put("trunkrows", VehicleEdit::editTrunkRows);
        EDIT_FUNCTIONS.put("accelerationspeed", VehicleEdit::editAccelerationSpeed);
        EDIT_FUNCTIONS.put("maxspeed", VehicleEdit::editMaxSpeed);
        EDIT_FUNCTIONS.put("brakingspeed", VehicleEdit::editBrakingSpeed);
        EDIT_FUNCTIONS.put("frictionspeed", VehicleEdit::editFrictionSpeed);
        EDIT_FUNCTIONS.put("maxspeedbackwards", VehicleEdit::editMaxSpeedBackwards);
        EDIT_FUNCTIONS.put("rotationspeed", VehicleEdit::editRotationSpeed);
        EDIT_FUNCTIONS.put("glowing", VehicleEdit::editGlowing);
        EDIT_FUNCTIONS.put("fuelenabled", VehicleEdit::editFuelEnabled);
        EDIT_FUNCTIONS.put("trunkenabled", VehicleEdit::editTrunkEnabled);
    }

    @Override
    public boolean execute() {
        if (!checkPermission("mtvehicles.edit"))
            return true;

        final ItemStack item = player.getInventory().getItemInMainHand();

        if (!isHoldingVehicle())
            return true;

        ConfigModule.configList.forEach(MTVConfig::reload);
        if (arguments.length == 1 && isPlayer) {
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

        TriFunction<Player, String, String, Boolean> editFunction = EDIT_FUNCTIONS.get(paramName.toLowerCase());
    if (editFunction == null) {
        ConfigModule.messagesConfig.sendMessage(player, Message.INVALID_INPUT);
        return true;
    }
    
    boolean success = editFunction.apply(targetPlayer, licensePlate, paramValue);
    
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
        return editDoubleValue(player, licensePlate, fuelUsageStr, VehicleDataConfig.Option.FUEL_USAGE);
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
        return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.ACCELERATION_SPEED);
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
        return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.MAX_SPEED);
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
        return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.BRAKING_SPEED);
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
        return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.FRICTION_SPEED);
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
        return editDoubleValue(player, licensePlate, speedStr, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS);
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
        return editIntValue(player, licensePlate, speedStr, VehicleDataConfig.Option.ROTATION_SPEED);
    }

    /**
     * Edit whether the vehicle is glowing
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param valueStr     "true" or "false" for glowing effect
     * @return true if successful
     */
    public static boolean editGlowing(Player player, String licensePlate, String valueStr) {
        ItemStack item = player.getInventory().getItemInMainHand();
        ItemMeta meta = item.getItemMeta();
        if(valueStr.equalsIgnoreCase("true")) {
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.removeEnchant(Enchantment.ARROW_INFINITE);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        item.setItemMeta(meta);
        return editBooleanValue(player, licensePlate, valueStr, VehicleDataConfig.Option.IS_GLOWING);
    }

    /**
     * Edit whether fuel is enabled for the vehicle
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param valueStr     "true" or "false" for fuel enabled
     * @return true if successful
     */
    public static boolean editFuelEnabled(Player player, String licensePlate, String valueStr) {
        return editBooleanValue(player, licensePlate, valueStr, VehicleDataConfig.Option.FUEL_ENABLED);
    }

    /**
     * Edit whether trunk is enabled for the vehicle
     * 
     * @param player       Player who holds the vehicle
     * @param licensePlate Vehicle's license plate
     * @param valueStr     "true" or "false" for trunk enabled
     * @return true if successful
     */
    public static boolean editTrunkEnabled(Player player, String licensePlate, String valueStr) {
        return editBooleanValue(player, licensePlate, valueStr, VehicleDataConfig.Option.TRUNK_ENABLED);
    }

    private static boolean editBooleanValue(Player player, String licensePlate, String valueStr, VehicleDataConfig.Option option) {
        boolean value;

        switch (valueStr.toLowerCase()) {
            case "true":
                value = true;
                break;
            case "false":
                value = false;
                break;
            default:
                ConfigModule.messagesConfig.sendMessage(player, Message.INVALID_INPUT);
                return false;
        }
        ConfigModule.vehicleDataConfig.set(licensePlate, option, value);
        ConfigModule.vehicleDataConfig.save();
        
        return true;
    }

    private static boolean editDoubleValue(Player player, String licensePlate, String valueStr, VehicleDataConfig.Option option) {
        try {
            double value = Double.parseDouble(valueStr);
            ConfigModule.vehicleDataConfig.set(licensePlate, option, value);
            ConfigModule.vehicleDataConfig.save();
            return true;
        } catch (NumberFormatException e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
            return false;
        }
    }
    
    private static boolean editIntValue(Player player, String licensePlate, String valueStr, VehicleDataConfig.Option option) {
        try {
            int value = Integer.parseInt(valueStr);
            ConfigModule.vehicleDataConfig.set(licensePlate, option, value);
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
