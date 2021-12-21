package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.infrastructure.models.Vehicle;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.inventory.InventoryClickEvent;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MenuUtils {
    public static HashMap<String, Integer> restoreId = new HashMap<>();
    public static HashMap<String, UUID> restoreUUID = new HashMap<>();

    public static void menuEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Settings");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");

        Boolean isGlowing = ConfigModule.vehicleDataConfig.getConfig().getBoolean(String.format("vehicle.%s.isGlow", ken));
        String name = ConfigModule.vehicleDataConfig.getConfig().getString(String.format("vehicle.%s.name", ken));
        String skinItem = ConfigModule.vehicleDataConfig.getConfig().getString(String.format("vehicle.%s.skinItem", ken));
        short skinDamage = (short) ConfigModule.vehicleDataConfig.getConfig().getInt(String.format("vehicle.%s.skinDamage", ken));

        inv.setItem(10, ItemUtils.mItem2(skinItem, 1, skinDamage, "&6Naam Aanpassen", String.format("&7Huidige: &e%s", name)));
        inv.setItem(13, ItemUtils.mItem("PAPER", 1, (short) 0, "&6Kenteken Aanpassen", String.format("&7Huidige: &e%s", ken)));
        if (isGlowing) {
            inv.setItem(16, ItemUtils.glowItem("BOOK", "&6Glow Aanpassen", String.format("&7Huidige: &e%s", isGlowing)));
        } else {
            inv.setItem(16, ItemUtils.mItem("BOOK", 1, (short) 0, "&6Glow Aanpassen", String.format("&7Huidige: &e%s", isGlowing)));
        }
        DrawOptions(p, inv);
    }

    private static void DrawOptions(Player p, Inventory inv) {
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }

    public static void benzineEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Benzine");
        String licensePlate = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");

        Vehicle vehicle = Vehicle.getByPlate(licensePlate);

        if (vehicle == null) {
            return;
        }

        ItemStack test = ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + licensePlate + ".benzineEnabled"));
        ItemStack test2 = ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Huidige Benzine", String.format("&7Huidige: &e%s", vehicle.getFuel()));
        ItemStack test3 = ItemUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine Verbruik", String.format("&7Huidige: &e%s", vehicle.getFuelUsage()));
        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());
        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        DrawOptions(p, inv);
    }

    public static void kofferbakEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Kofferbak");
        String licensePlate = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = ItemUtils.mItem("CHEST", 1, (short) 0, "&6Kofferbak", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + licensePlate + ".kofferbak"));
        ItemStack test2 = ItemUtils.mItem("CHEST", 1, (short) 0, "&6Huidige Rows", "&7Huidige: &e" + ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + licensePlate + ".kofferbakRows"));
        ItemStack test3 = ItemUtils.mItem("CHEST", 1, (short) 0, "&6Open Kofferbak", "&7Huidige: &eClick to open");
        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());
        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        DrawOptions(p, inv);
    }

    public static void membersEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Members");
        String licensePlate = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");

        Vehicle vehicle = Vehicle.getByPlate(licensePlate);

        if (vehicle == null) {
            return;
        }

        ItemStack test = ItemUtils.mItem("PAPER", 1, (short) 0, "&6Owners", "&7Naam: &e" + Bukkit.getOfflinePlayer(UUID.fromString(vehicle.getOwner())).getName());
        ItemStack test2 = ItemUtils.mItemRiders("PAPER", 1, (short) 0, "&6Riders", licensePlate);
        ItemStack test3 = ItemUtils.mItemMembers("PAPER", 1, (short) 0, "&6Members", licensePlate);
        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());
        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        DrawOptions(p, inv);
    }

    public static void speedEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Speed");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Acceleration Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + ".acceleratieSpeed"));
        ItemStack test2 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Max Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + ".maxSpeed"));
        ItemStack test3 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Braking Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + ".brakingSpeed"));
        ItemStack test4 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Friction Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + ".aftrekkenSpeed"));
        ItemStack test5 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Rotation Speed", "&7Current: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + ".rotateSpeed"));
        ItemStack test6 = ItemUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Max Speed Backwards", "&7Current: &e" + ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + ".maxSpeedBackwards"));
        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());
        ItemStack car4 = (new ItemFactory(test4).setNBT("mtvehicles.item", "4").toItemStack());
        ItemStack car5 = (new ItemFactory(test5).setNBT("mtvehicles.item", "5").toItemStack());
        ItemStack car6 = (new ItemFactory(test6).setNBT("mtvehicles.item", "6").toItemStack());
        inv.setItem(10, car);
        inv.setItem(11, car2);
        inv.setItem(12, car3);
        inv.setItem(13, car4);
        inv.setItem(14, car5);
        inv.setItem(15, car6);
        DrawOptions(p, inv);
    }

    public static void getvehicleCMD(Player p, int id, int slot) {
        List<Map<?, ?>> vehicles = ConfigModule.vehiclesConfig.getConfig().getMapList("voertuigen");
        List<Map<?, ?>> skins = (List<Map<?, ?>>) vehicles.get(slot).get("cars");
        InventoryClickEvent.intSave.put(p.getUniqueId(), slot);
        Inventory inv = Bukkit.createInventory(null, 54, "Choose your vehicle");
        for (int i = 36; i <= 44; i++) {
            inv.setItem(i, ItemUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(47, ItemUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(51, ItemUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        List<Map> dataVehicle = new ArrayList<>();
        for (Map<?, ?> skin : skins) {
            dataVehicle.add(skin);
            //inv.addItem(ItemUtils.carItem2((int) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem")));
        }
        for (int i = 1 + id * 36 - 36; i <= id * 36; i++) {
            if (i - 1 < dataVehicle.size()) {

                if (dataVehicle.get(i-1).get("nbtValue") == null) {
                    inv.addItem(ItemUtils.carItem2((int) dataVehicle.get(i-1).get("itemDamage"), ((String) dataVehicle.get(i-1).get("name")), (String) dataVehicle.get(i-1).get("SkinItem")));
                    continue;
                }

                inv.addItem(ItemUtils.carItem3((int) dataVehicle.get(i-1).get("itemDamage"), ((String) dataVehicle.get(i-1).get("name")), (String) dataVehicle.get(i-1).get("SkinItem"), (String) dataVehicle.get(i-1).get("nbtKey"), (String) dataVehicle.get(i-1).get("nbtValue")));

            }

        }
        inv.setItem(53, ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVolgende Pagina", "&c"));
        inv.setItem(45, ItemUtils.mItem("SPECTRAL_ARROW", 1, (short) 0, "&cVorige Pagina", "&c"));

        InventoryClickEvent.skinMenu.put(p.getUniqueId(), inv);
        p.openInventory(inv);
    }

    public static void restoreCMD(Player p, int id, UUID of) {
        Inventory inv = Bukkit.createInventory(null, 54, "Vehicle Restore " + id);
        ConfigModule.configList.forEach(ConfigUtils::reload);
        if (ConfigModule.vehicleDataConfig.getConfig().getConfigurationSection("vehicle") != null) {
            List<String> dataVehicle = new ArrayList<>();
            for (String entry : ConfigModule.vehicleDataConfig.getConfig().getConfigurationSection("vehicle").getKeys(false)) {
                dataVehicle.add(entry);
            }
            for (int i = 1 + id * 36 - 36; i <= id * 36; i++) {
                if (i - 1 < dataVehicle.size()) {
                    if (of == null || ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".owner").contains(of.toString())) {
                        if (ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + dataVehicle.get(i - 1) + ".isGlow") == true) {
                            inv.addItem(ItemUtils.carItem2glow(ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + dataVehicle.get(i - 1) + ".skinDamage"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".name"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".skinItem"), dataVehicle.get(i - 1)));
                        } else {
                            if (ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".nbtValue") == null) {
                                inv.addItem(ItemUtils.carItem5(ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + dataVehicle.get(i - 1) + ".skinDamage"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".name"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".skinItem"), dataVehicle.get(i - 1)));
                                continue;
                            }
                            inv.addItem(ItemUtils.carItem4(ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + dataVehicle.get(i - 1) + ".skinDamage"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".name"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".skinItem"), "mtcustom", ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + dataVehicle.get(i - 1) + ".nbtValue"), dataVehicle.get(i - 1)));
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
