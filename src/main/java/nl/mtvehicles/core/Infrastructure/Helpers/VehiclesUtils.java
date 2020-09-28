package nl.mtvehicles.core.Infrastructure.Helpers;

import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class VehiclesUtils {

    public static HashMap<String, Boolean> edit = new HashMap<>();

    public static ItemStack carItem(int durability, String name, String material) {
        Material carMaterial = Material.getMaterial(material);
        assert carMaterial != null;
        ItemStack carItem = new ItemStack(carMaterial);
        ItemMeta itemMeta = new ItemStack(carMaterial).getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(TextUtils.colorize("&6" + name));
        List<String> itemLore = new ArrayList<>();
        itemLore.add(TextUtils.colorize("&a"));
        itemMeta.setLore(itemLore);
        itemMeta.setUnbreakable(true);
        carItem.setItemMeta(itemMeta);
        carItem.setDurability((short) durability);
        return carItem;
    }

    public static ItemStack carItem2(int id, String name, String material) {
        String ken = generateLicencePlate();
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a" + ken));
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);
        im.setUnbreakable(true);
        car.setItemMeta(im);

        return car;
    }


    public static String generateLicencePlate() {
        String plate = String.format("%s-%s-%s", RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false), RandomStringUtils.random(2, true, false));
        return plate.toUpperCase();
    }


    public static ItemStack woolItem(String mat1, String mat2, int amount, short durability, String text, String lores) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(mat1.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);

            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(mat2), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);

                return is;
            } catch (Exception e2) {
                e2.printStackTrace();

                return null;
            }
        }
    }

    public static ItemStack mItem(String material, int amount, short durability, String text, String lores) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);

            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);

                return is;
            } catch (Exception e2) {
                e2.printStackTrace();

                return null;
            }
        }
    }

    public static ItemStack mItemRiders(String material, int amount, short durability, String text, String ken) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            if (Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders").size() == 0) {
                itemlore.add(TextUtils.colorize((("&7Riders: &eGeen"))));
            } else {
                for (String subj : Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders")) {
                    itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                }
            }
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);

            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                if (Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders").size() == 0) {
                    itemlore.add(TextUtils.colorize((("&7Riders: &eGeen"))));
                } else {
                    for (String subj : Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".riders")) {
                        itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                    }
                }
                im.setLore(itemlore);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);

                return is;
            } catch (Exception e2) {
                e2.printStackTrace();

                return null;
            }
        }
    }

    public static ItemStack mItemMembers(String material, int amount, short durability, String text, String ken) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            if (Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members").size() == 0) {
                itemlore.add(TextUtils.colorize((("&7Members: &eGeen"))));
            } else {
                for (String subj : Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members")) {
                    itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                }
            }
            im.setLore(itemlore);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);

            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                if (Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members").size() == 0) {
                    itemlore.add(TextUtils.colorize((("&7Members: &eGeen"))));
                } else {
                    for (String subj : Main.vehicleDataConfig.getConfig().getStringList("vehicle." + ken + ".members")) {
                        itemlore.add(TextUtils.colorize(("&7- &e" + Bukkit.getOfflinePlayer(UUID.fromString(subj)).getName())));
                    }
                }
                im.setLore(itemlore);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);

                return is;
            } catch (Exception e2) {
                e2.printStackTrace();

                return null;
            }
        }
    }

    public static ItemStack mItem2(String material, int amount, short durability, String text, String lores) {
        try {
            ItemStack is = new ItemStack(Material.getMaterial(material.toUpperCase()), amount, durability);
            ItemMeta im = is.getItemMeta();
            List<String> itemlore = new ArrayList<>();
            itemlore.add(TextUtils.colorize(lores));
            im.setLore(itemlore);
            im.setUnbreakable(true);
            im.setDisplayName(TextUtils.colorize(text));
            is.setItemMeta(im);

            return is;
        } catch (Exception e) {
            try {
                ItemStack is = new ItemStack(Material.matchMaterial(material, true), amount, durability);
                ItemMeta im = is.getItemMeta();
                List<String> itemlore = new ArrayList<>();
                itemlore.add(TextUtils.colorize(lores));
                im.setLore(itemlore);
                im.setUnbreakable(true);
                im.setDisplayName(TextUtils.colorize(text));
                is.setItemMeta(im);

                return is;
            } catch (Exception e2) {
                e2.printStackTrace();

                return null;
            }
        }
    }


    public static ItemStack carItem2(int id, String name, String material, String ken) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a" + ken));
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);

        im.setUnbreakable(true);
        car.setItemMeta(im);

        return car;
    }

    public static ItemStack carItem2glow(int id, String name, String material, String ken) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name)).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a" + ken));
        itemlore.add(TextUtils.colorize("&a"));
        im.setLore(itemlore);
        im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        im.setUnbreakable(true);
        car.setItemMeta(im);

        return car;
    }

    public static ItemStack glowItem(String material, String name, String ken) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setName(name).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize(ken));
        im.setLore(itemlore);
        im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        im.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        car.setItemMeta(im);

        return car;
    }

    public static void menuEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Settings");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        inv.setItem(10, VehiclesUtils.mItem2(Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), 1, (short) Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), "&6Naam Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name")));
        inv.setItem(13, VehiclesUtils.mItem("PAPER", 1, (short) 0, "&6Kenteken Aanpassen", "&7Huidige: &e" + ken));
        if ((boolean) Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".isGlow") == true) {
            inv.setItem(16, VehiclesUtils.glowItem("BOOK", "&6Glow Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")));
        } else {
            inv.setItem(16, VehiclesUtils.mItem("BOOK", 1, (short) 0, "&6Glow Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")));
        }
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, VehiclesUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, VehiclesUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, VehiclesUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }

    public static void benzineEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Benzine");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = VehiclesUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine", "&7Huidige: &e" + (Boolean) Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".benzineEnabled"));
        ItemStack test2 = VehiclesUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Huidige Benzine", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzine"));
        ItemStack test3 = VehiclesUtils.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine Verbruik", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzineVerbruik"));

        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());

        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, VehiclesUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, VehiclesUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, VehiclesUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }

    public static void kofferbakEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Kofferbak");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = VehiclesUtils.mItem("CHEST", 1, (short) 0, "&6Kofferbak", "&7Huidige: &e" + (Boolean) Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".kofferbak"));
        ItemStack test2 = VehiclesUtils.mItem("CHEST", 1, (short) 0, "&6Huidige Rows", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".kofferbakRows"));
        ItemStack test3 = VehiclesUtils.mItem("CHEST", 1, (short) 0, "&6Open Kofferbak", "&7Huidige: &eClick to open");

        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());

        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, VehiclesUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, VehiclesUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, VehiclesUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }
    public static void membersEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Members");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = VehiclesUtils.mItem("PAPER", 1, (short) 58, "&6Owners", "&7Naam: &e"+ Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName());
        ItemStack test2 = VehiclesUtils.mItemRiders("PAPER", 1, (short) 58, "&6Riders", ken);
        ItemStack test3 = VehiclesUtils.mItemMembers("PAPER", 1, (short) 58, "&6Members", ken);

        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());

        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, VehiclesUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, VehiclesUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, VehiclesUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }

    public static void speedEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Speed");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Acceleratie Speed", "&7Huidige: &e"+(double)Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".acceleratieSpeed"));
        ItemStack test2 = VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Max Speed", "&7Huidige: &e"+(double)Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".maxSpeed"));
        ItemStack test3 = VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Braking Speed", "&7Huidige: &e"+(double)Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".brakingSpeed"));
        ItemStack test4 = VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Aftrekken Speed", "&7Huidige: &e"+(double)Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".aftrekkenSpeed"));
        ItemStack test5 = VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Rotate Speed", "&7Huidige: &e"+(int)Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".rotateSpeed"));
        ItemStack test6 = VehiclesUtils.woolItem("STAINED_GLASS_PANE", "LIME_STAINED_GLASS", 1, (short) 5, "&6Max Speed Backwards", "&7Huidige: &e"+(double)Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".maxSpeedBackwards"));

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
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, VehiclesUtils.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, VehiclesUtils.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, VehiclesUtils.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }

    public static void saveInventory(final Inventory inv) {
//        for (int i = 0; i < inv.length; i++) { // start iterating into the inv
//            ItemStack item = inv[i]; // getting the itemstack
//            if (item == null) Main.vehicleDataConfig.getConfig().set("Players.xVoidZx.world.Inventory." + i, "empty"); // if it's a null itemstack, we save it as a string
//            else Main.vehicleDataConfig.getConfig().set("Players.xVoidZx.world.Inventory." + i, item); // else, we save the itemstack
//        }
    }

}
