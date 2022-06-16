package nl.mtvehicles.core.infrastructure.utils;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.dataconfig.MessagesConfig;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.MTVConfig;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.listeners.inventory.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Methods used in plugin's GUIs
 */
public class MenuUtils {
    /**
     * Page opened in the restore menu
     */
    public static HashMap<Player, Integer> restorePage = new HashMap<>();
    /**
     * UUID of the player whose vehicles are in the restore menu
     */
    public static HashMap<Player, UUID> restoreUUID = new HashMap<>();

    /**
     * Get the item which is clicked for getting to a previous menu
     */
    public static ItemStack getBackItem(){
        return ItemUtils.getMenuItem(
                "OAK_DOOR",
                "WOOD_DOOR",
                (short) 0,
                1,
                ConfigModule.messagesConfig.getMessage(Message.BACK),
                ConfigModule.messagesConfig.getMessage(Message.BACK_DESCRIPTION)
        );
    }

    /**
     * Get the item which is clicked for closing a menu
     */
    public static ItemStack getCloseItem(){
        return ItemUtils.getMenuItem(
                Material.BARRIER,
                1,
                ConfigModule.messagesConfig.getMessage(Message.CLOSE),
                ConfigModule.messagesConfig.getMessage(Message.CLOSE_DESCRIPTION)
        );
    }

    /**
     * Open /vehicle edit menu to a player
     */
    public static void menuEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, InventoryTitle.VEHICLE_SETTINGS_MENU.getStringTitle());
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        boolean isGlowing = (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING);
        String name = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
        String skinItem = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString();
        short skinDamage = (short) ConfigModule.vehicleDataConfig.getDamage(licensePlate);

        MessagesConfig msg = ConfigModule.messagesConfig;

        inv.setItem(10, ItemUtils.getMenuCustomItem(ItemUtils.getMaterial(skinItem), "&6" + msg.getMessage(Message.EDIT_NAME), skinDamage, String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), name)));
        inv.setItem(13, ItemUtils.getMenuItem(Material.PAPER, 1, "&6" + msg.getMessage(Message.EDIT_LICENSE_PLATE), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), licensePlate)));
        if (isGlowing)
            //inv.setItem(16, ItemUtils.glowItem("BOOK", "&6" + msg.getMessage(Message.TOGGLE_GLOW), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_ON))));
            inv.setItem(16, ItemUtils.getMenuGlowingItem(Material.BOOK, 1, "&6" + msg.getMessage(Message.TOGGLE_GLOW), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_ON))));
        else
            inv.setItem(16, ItemUtils.getMenuItem(Material.BOOK, 1, "&6" + msg.getMessage(Message.TOGGLE_GLOW), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_OFF))));
        DrawOptions(p, inv);
    }

    private static void DrawOptions(Player p, Inventory inv) {
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"));
        }
        inv.setItem(38, getCloseItem());
        inv.setItem(42, getBackItem());
        p.openInventory(inv);
    }

    private static void DrawOptions(Player p, Inventory inv, ItemStack option1, ItemStack option2, ItemStack option3) {
        inv.setItem(10, new ItemFactory(option1).setNBT("mtvehicles.item", "1").toItemStack());
        inv.setItem(13, new ItemFactory(option2).setNBT("mtvehicles.item", "2").toItemStack());
        inv.setItem(16, new ItemFactory(option3).setNBT("mtvehicles.item", "3").toItemStack());
        DrawOptions(p, inv);
    }

    /**
     * Open fuel edit menu to a player
     */
    public static void benzineEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, InventoryTitle.VEHICLE_FUEL_MENU.getStringTitle());
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        if (vehicle == null) return;

        MessagesConfig msg = ConfigModule.messagesConfig;

        ItemStack option1;
        if ((boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED))
            option1 = ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE,  "&6" + msg.getMessage(Message.TOGGLE_FUEL), 58, String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_ON)));
        else
            option1 = ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE, "&6" + msg.getMessage(Message.TOGGLE_FUEL), 58, String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_OFF)));
        ItemStack option2 = ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE, "&6" + msg.getMessage(Message.CURRENT_FUEL), 58, String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), vehicle.getFuel()));
        ItemStack option3 = ItemUtils.getMenuCustomItem(Material.DIAMOND_HOE, "&6" + msg.getMessage(Message.FUEL_USAGE), 58, String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), vehicle.getFuelUsage()));
        DrawOptions(p, inv, option1, option2, option3);
    }

    /**
     * Open trunk edit menu to a player
     */
    public static void trunkEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, InventoryTitle.VEHICLE_TRUNK_MENU.getStringTitle());
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");
        MessagesConfig msg = ConfigModule.messagesConfig;

        ItemStack option1;
        if ((boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED))
            option1 = ItemUtils.getMenuItem(Material.CHEST, 1, "&6" + msg.getMessage(Message.TOGGLE_TRUNK), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_ON)));
        else
            option1 = ItemUtils.getMenuItem(Material.CHEST, 1, "&6" + msg.getMessage(Message.TOGGLE_TRUNK), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), msg.getMessage(Message.TURNED_OFF)));
        ItemStack option2 = ItemUtils.getMenuItem(Material.CHEST, 1, "&6" + msg.getMessage(Message.EDIT_TRUNK_ROWS), String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ROWS)));
        ItemStack option3 = ItemUtils.getMenuItem(Material.CHEST, 1, "&6" + msg.getMessage(Message.OPEN_TRUNK),  "&7" + msg.getMessage(Message.CLICK_TO_OPEN));
        DrawOptions(p, inv, option1, option2, option3);
    }

    /**
     * Open members edit menu to a player
     */
    public static void membersEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, InventoryTitle.VEHICLE_MEMBERS_MENU.getStringTitle());
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        Vehicle vehicle = VehicleUtils.getVehicle(licensePlate);
        if (vehicle == null) return;

        ItemStack option1 = ItemUtils.getMenuItem(Material.PAPER, 1, "&6" + ConfigModule.messagesConfig.getMessage(Message.OWNER), String.format("&7%s: &e%s", ConfigModule.messagesConfig.getMessage(Message.NAME), Bukkit.getOfflinePlayer(vehicle.getOwnerUUID()).getName()));
        ItemStack option2 = ItemUtils.getMenuRidersItem(licensePlate);
        ItemStack option3 = ItemUtils.getMenuMembersItem(licensePlate);
        DrawOptions(p, inv, option1, option2, option3);
    }

    /**
     * Open speed edit menu to a player
     */
    public static void speedEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, InventoryTitle.VEHICLE_SPEED_MENU.getStringTitle());
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        VehicleDataConfig data = ConfigModule.vehicleDataConfig;
        MessagesConfig msg = ConfigModule.messagesConfig;

        ItemStack option1 = ItemUtils.getMenuItem(
                "LIME_STAINED_GLASS",
                "STAINED_GLASS",
                (short) 5,
                1,
                "&6" + msg.getMessage(Message.ACCELERATION_SPEED),
                String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), data.get(licensePlate, VehicleDataConfig.Option.ACCELARATION_SPEED))
        );
        ItemStack option2 = ItemUtils.getMenuItem(
                "LIME_STAINED_GLASS",
                "STAINED_GLASS",
                (short) 5,
                1,
                "&6" + msg.getMessage(Message.MAX_SPEED),
                String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), data.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED))
        );
        ItemStack option3 = ItemUtils.getMenuItem(
                "LIME_STAINED_GLASS",
                "STAINED_GLASS",
                (short) 5,
                1,
                "&6" + msg.getMessage(Message.BRAKING_SPEED),
                String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), data.get(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED))
        );
        ItemStack option4 = ItemUtils.getMenuItem(
                "LIME_STAINED_GLASS",
                "STAINED_GLASS",
                (short) 5,
                1,
                "&6" + msg.getMessage(Message.FRICTION_SPEED),
                String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), data.get(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED))
        );
        ItemStack option5 = ItemUtils.getMenuItem(
                "LIME_STAINED_GLASS",
                "STAINED_GLASS",
                (short) 5,
                1,
                "&6" + msg.getMessage(Message.ROTATION_SPEED),
                String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), data.get(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED))
        );
        ItemStack option6 = ItemUtils.getMenuItem(
                "LIME_STAINED_GLASS",
                "STAINED_GLASS",
                (short) 5,
                1,
                "&6" + msg.getMessage(Message.MAX_SPEED_BACKWARDS),
                String.format("&7%s: &e%s", msg.getMessage(Message.CURRENTLY), data.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS))
        );

        inv.setItem(10, new ItemFactory(option1).setNBT("mtvehicles.item", "1").toItemStack());
        inv.setItem(11, new ItemFactory(option2).setNBT("mtvehicles.item", "2").toItemStack());
        inv.setItem(12, new ItemFactory(option3).setNBT("mtvehicles.item", "3").toItemStack());
        inv.setItem(13, new ItemFactory(option4).setNBT("mtvehicles.item", "4").toItemStack());
        inv.setItem(14, new ItemFactory(option5).setNBT("mtvehicles.item", "5").toItemStack());
        inv.setItem(15, new ItemFactory(option6).setNBT("mtvehicles.item", "6").toItemStack());
        DrawOptions(p, inv);
    }

    /**
     * Open vehicle choice menu to a player
     */
    public static void getvehicleCMD(Player p, int page, int slot) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        List<Map<?, ?>> skins = (List<Map<?, ?>>) vehicles.get(slot).get("cars");

        Inventory inv = Bukkit.createInventory(null, 54, InventoryTitle.CHOOSE_VEHICLE_MENU.getStringTitle());
        InventoryClickListener.intSave.put(p.getUniqueId(), slot);
        InventoryClickListener.skinMenu.put(p.getUniqueId(), inv);

        List<Map> dataVehicle = new ArrayList<>();
        for (Map<?, ?> skin : skins) {
            dataVehicle.add(skin);
        }

        for (int i = 1 + page * 36 - 36; i <= page * 36; i++) {
            if (i - 1 < dataVehicle.size()) {
                Map<?, ?> vehicle = dataVehicle.get(i-1);
                if (vehicle.get("nbtValue") == null) {
                    inv.addItem(ItemUtils.getVehicleItem(ItemUtils.getMaterial(vehicle.get("SkinItem").toString()), (int) vehicle.get("itemDamage"), vehicle.get("name").toString()));
                    continue;
                }
                inv.addItem(ItemUtils.getVehicleItem(ItemUtils.getMaterial(vehicle.get("SkinItem").toString()), (int) vehicle.get("itemDamage"), vehicle.get("name").toString(), vehicle.get("nbtKey").toString(), vehicle.get("nbtValue")));
            }
        }

        inv.setItem(53, ItemUtils.getMenuItem(Material.SPECTRAL_ARROW, 1, "&c" + ConfigModule.messagesConfig.getMessage(Message.NEXT_PAGE), "&c"));
        inv.setItem(45, ItemUtils.getMenuItem(Material.SPECTRAL_ARROW, 1, "&c"  + ConfigModule.messagesConfig.getMessage(Message.PREVIOUS_PAGE), "&c"));
        for (int i = 36; i <= 44; i++) {
            inv.setItem(i, ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"));
        }
        inv.setItem(47, getCloseItem());
        inv.setItem(51, getBackItem());
        p.openInventory(inv);
    }

    /**
     * Open /vehicle restore menu to a player
     * @param p Player
     * @param page Page
     * @param ownerUUID UUID of the owner whose vehicles are displayed
     */
    public static void restoreCMD(Player p, int page, UUID ownerUUID) {
        Inventory inv = Bukkit.createInventory(null, 54, InventoryTitle.VEHICLE_RESTORE_MENU.getStringTitle());
        ConfigModule.configList.forEach(MTVConfig::reload);
        restorePage.put(p, page);
        if (!ConfigModule.vehicleDataConfig.isEmpty()) {
            List<String> dataVehicle = new ArrayList<>();
            for (String entry : ConfigModule.vehicleDataConfig.getVehicles().getKeys(false)) {
                dataVehicle.add(entry);
            }
            for (int i = 1 + page * 36 - 36; i <= page * 36; i++) {
                if (i - 1 < dataVehicle.size()) {
                    String license = dataVehicle.get(i - 1);
                    VehicleDataConfig data = ConfigModule.vehicleDataConfig;
                    Boolean isGlowing = (Boolean) data.get(license, VehicleDataConfig.Option.IS_GLOWING);
                    if (isGlowing == null) isGlowing = false;
                    if (ownerUUID == null || data.get(license, VehicleDataConfig.Option.OWNER).toString().contains(ownerUUID.toString())) {
                        if (data.get(license, VehicleDataConfig.Option.NBT_VALUE) == null) {
                            inv.addItem(ItemUtils.getVehicleItem(ItemUtils.getMaterial(
                                    data.get(license, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                                    data.getDamage(license),
                                    isGlowing,
                                    data.get(license, VehicleDataConfig.Option.NAME).toString(),
                                    license));
                            continue;
                        }
                        inv.addItem(ItemUtils.getVehicleItem(ItemUtils.getMaterial(
                                data.get(license, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                                data.getDamage(license),
                                isGlowing,
                                data.get(license, VehicleDataConfig.Option.NAME).toString(),
                                license,
                                "mtcustom",
                                data.get(license, VehicleDataConfig.Option.NBT_VALUE)));
                    }
                }
            }
            for (int i = 36; i <= 44; i++) {
                inv.setItem(i, ItemUtils.getMenuItem(ItemUtils.getStainedGlassPane(), 1, "&c", "&c"));
            }
            inv.setItem(53, ItemUtils.getMenuItem(Material.SPECTRAL_ARROW, 1, "&c" + ConfigModule.messagesConfig.getMessage(Message.NEXT_PAGE), "&c"));
            inv.setItem(45, ItemUtils.getMenuItem(Material.SPECTRAL_ARROW, 1, "&c"  + ConfigModule.messagesConfig.getMessage(Message.PREVIOUS_PAGE), "&c"));
            p.openInventory(inv);
        }
    }
}
