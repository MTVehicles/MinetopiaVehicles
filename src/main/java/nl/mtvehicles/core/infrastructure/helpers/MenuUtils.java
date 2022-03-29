package nl.mtvehicles.core.infrastructure.helpers;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.models.Config;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.models.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.listeners.inventory.InventoryClickListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@ToDo(comment = "To be translated.")
public class MenuUtils {
    public static HashMap<String, Integer> restoreId = new HashMap<>();
    public static HashMap<String, UUID> restoreUUID = new HashMap<>();

    public static ItemStack closeItem = ItemUtils.mItem("BARRIER", 1, (short) 0, ConfigModule.messagesConfig.getMessage(Message.CLOSE), ConfigModule.messagesConfig.getMessage(Message.CLOSE_DESCRIPTION));
    public static ItemStack backItem = ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, ConfigModule.messagesConfig.getMessage(Message.BACK), ConfigModule.messagesConfig.getMessage(Message.BACK_DESCRIPTION));

    public static void menuEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Settings");
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        boolean isGlowing = (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING);
        String name = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString();
        String skinItem = ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString();
        short skinDamage = (short) ConfigModule.vehicleDataConfig.getDamage(licensePlate);

        inv.setItem(10, ItemUtils.mItem2(skinItem, 1, skinDamage, "&6Naam Aanpassen", String.format("&7Huidige: &e%s", name)));
        inv.setItem(13, ItemUtils.mItem("PAPER", 1, (short) 0, "&6Kenteken Aanpassen", String.format("&7Huidige: &e%s", licensePlate)));
        if (isGlowing)
            inv.setItem(16, ItemUtils.glowItem("BOOK", "&6Glow Aanpassen", String.format("&7Huidige: &e%s", isGlowing)));
        else
            inv.setItem(16, ItemUtils.mItem("BOOK", 1, (short) 0, "&6Glow Aanpassen", String.format("&7Huidige: &e%s", isGlowing)));
        DrawOptions(p, inv);
    }

    private static void DrawOptions(Player p, Inventory inv) {
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, closeItem);
        inv.setItem(42, backItem);
        p.openInventory(inv);
    }

    private static void DrawOptions(Player p, Inventory inv, ItemStack option1, ItemStack option2, ItemStack option3) {
        inv.setItem(10, new ItemFactory(option1).setNBT("mtvehicles.item", "1").toItemStack());
        inv.setItem(13, new ItemFactory(option2).setNBT("mtvehicles.item", "2").toItemStack());
        inv.setItem(16, new ItemFactory(option3).setNBT("mtvehicles.item", "3").toItemStack());
        DrawOptions(p, inv);
    }

    public static void benzineEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Benzine");
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);
        if (vehicle == null) return;

        ItemStack option1 = ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FUEL_ENABLED).toString());
        ItemStack option2 = ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Huidige Benzine", String.format("&7Huidige: &e%s", vehicle.getFuel()));
        ItemStack option3 = ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine Verbruik", String.format("&7Huidige: &e%s", vehicle.getFuelUsage()));
        DrawOptions(p, inv, option1, option2, option3);
    }

    public static void trunkEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Kofferbak");
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        ItemStack option1 = ItemUtils.mItem("CHEST", 1, (short) 0, "&6Kofferbak", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ENABLED).toString());
        ItemStack option2 = ItemUtils.mItem("CHEST", 1, (short) 0, "&6Huidige Rows", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.TRUNK_ROWS).toString());
        ItemStack option3 = ItemUtils.mItem("CHEST", 1, (short) 0, "&6Open Kofferbak", "&7Huidige: &eClick to open");
        DrawOptions(p, inv, option1, option2, option3);
    }

    public static void membersEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Members");
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        Vehicle vehicle = VehicleUtils.getByLicensePlate(licensePlate);
        if (vehicle == null) return;

        ItemStack option1 = ItemUtils.mItem("PAPER", 1, (short) 0, "&6Owners", "&7Naam: &e" + Bukkit.getOfflinePlayer(vehicle.getOwnerUUID()).getName());
        ItemStack option2 = ItemUtils.mItemRiders("PAPER", 1, (short) 0, "&6Riders", licensePlate);
        ItemStack option3 = ItemUtils.mItemMembers("PAPER", 1, (short) 0, "&6Members", licensePlate);
        DrawOptions(p, inv, option1, option2, option3);
    }

    @ToDo(comment = "To be translated to all languages.")
    public static void speedEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Speed");
        NBTItem nbt = new NBTItem(p.getInventory().getItemInMainHand());
        String licensePlate = nbt.getString("mtvehicles.kenteken");

        ItemStack option1 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Acceleration Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ACCELARATION_SPEED));
        ItemStack option2 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Max Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED));
        ItemStack option3 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Braking Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED));
        ItemStack option4 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Friction Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED));
        ItemStack option5 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Rotation Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.ROTATE_SPEED));
        ItemStack option6 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Max Speed Backwards", "&7Current: &e" + ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS));

        inv.setItem(10, new ItemFactory(option1).setNBT("mtvehicles.item", "1").toItemStack());
        inv.setItem(11, new ItemFactory(option2).setNBT("mtvehicles.item", "2").toItemStack());
        inv.setItem(12, new ItemFactory(option3).setNBT("mtvehicles.item", "3").toItemStack());
        inv.setItem(13, new ItemFactory(option4).setNBT("mtvehicles.item", "4").toItemStack());
        inv.setItem(14, new ItemFactory(option5).setNBT("mtvehicles.item", "5").toItemStack());
        inv.setItem(15, new ItemFactory(option6).setNBT("mtvehicles.item", "6").toItemStack());
        DrawOptions(p, inv);
    }

    @ToDo(comment = "To be translated.")
    public static void getvehicleCMD(Player p, int id, int slot) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getVehicles();
        List<Map<?, ?>> skins = (List<Map<?, ?>>) vehicles.get(slot).get("cars");

        Inventory inv = Bukkit.createInventory(null, 54, "Choose your vehicle");
        InventoryClickListener.intSave.put(p.getUniqueId(), slot);
        InventoryClickListener.skinMenu.put(p.getUniqueId(), inv);

        List<Map> dataVehicle = new ArrayList<>();
        for (Map<?, ?> skin : skins) {
            dataVehicle.add(skin);
        }

        for (int i = 1 + id * 36 - 36; i <= id * 36; i++) {
            if (i - 1 < dataVehicle.size()) {
                Map<?, ?> vehicle = dataVehicle.get(i-1);
                if (vehicle.get("nbtValue") == null) {
                    inv.addItem(ItemUtils.carItem2((int) vehicle.get("itemDamage"), vehicle.get("name").toString(), vehicle.get("SkinItem").toString()));
                    continue;
                }
                inv.addItem(ItemUtils.carItem3((int) vehicle.get("itemDamage"), vehicle.get("name").toString(), vehicle.get("SkinItem").toString(), vehicle.get("nbtKey").toString(), vehicle.get("nbtValue").toString()));
            }
        }

        inv.setItem(53, ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"));
        inv.setItem(45, ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"));
        for (int i = 36; i <= 44; i++) {
            inv.setItem(i, ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(47, closeItem);
        inv.setItem(51, backItem);
        p.openInventory(inv);
    }

    public static void restoreCMD(Player p, int id, UUID ownerUUID) {
        Inventory inv = Bukkit.createInventory(null, 54, "Vehicle Restore " + id);
        ConfigModule.configList.forEach(Config::reload);
        if (!ConfigModule.vehicleDataConfig.isEmpty()) {
            List<String> dataVehicle = new ArrayList<>();
            for (String entry : ConfigModule.vehicleDataConfig.getVehicles().getKeys(false)) {
                dataVehicle.add(entry);
            }
            for (int i = 1 + id * 36 - 36; i <= id * 36; i++) {
                if (i - 1 < dataVehicle.size()) {
                    String license = dataVehicle.get(i - 1);
                    VehicleDataConfig data = ConfigModule.vehicleDataConfig;
                    if (ownerUUID == null || data.get(license, VehicleDataConfig.Option.OWNER).toString().contains(ownerUUID.toString())) {
                        if ((boolean) data.get(license, VehicleDataConfig.Option.IS_GLOWING)) {
                            inv.addItem(ItemUtils.carItem2glow(data.getDamage(license), data.get(license, VehicleDataConfig.Option.NAME).toString(), data.get(license, VehicleDataConfig.Option.SKIN_ITEM).toString(), license));
                        } else {
                            if (data.get(license, VehicleDataConfig.Option.NBT_VALUE) == null) {
                                inv.addItem(ItemUtils.carItem5(data.getDamage(license), data.get(license, VehicleDataConfig.Option.NAME).toString(), data.get(license, VehicleDataConfig.Option.SKIN_ITEM).toString(), license));
                                continue;
                            }
                            inv.addItem(ItemUtils.carItem4(data.getDamage(license), data.get(license, VehicleDataConfig.Option.NAME).toString(), data.get(license, VehicleDataConfig.Option.SKIN_ITEM).toString(), "mtcustom", data.get(license, VehicleDataConfig.Option.NBT_VALUE).toString(), license));
                        }
                    }
                }
            }
            for (int i = 36; i <= 44; i++) {
                inv.setItem(i, ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
            }
            for (int i = 36; i <= 44; i++) {
                inv.setItem(i, ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
            }
            inv.setItem(52, ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"));
            inv.setItem(46, ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"));
            p.openInventory(inv);
        }
    }
}
