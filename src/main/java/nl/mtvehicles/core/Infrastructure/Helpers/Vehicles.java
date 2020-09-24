package nl.mtvehicles.core.Infrastructure.Helpers;

import nl.mtvehicles.core.Main;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS;

public class Vehicles {

    public static HashMap<String, Boolean> edit = new HashMap<>();

    public static ItemStack carItem(int id, String name, String material) {
        String ken = generateLicencePlate();
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name).replace(" ", " - ")).setNBT("mtvehicles.kenteken", ken).setNBT("mtvehicles.naam", name).toItemStack();
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
        StringBuilder plate = new StringBuilder();
        plate.append(RandomStringUtils.random(2, true, false));
        plate.append("-");
        plate.append(RandomStringUtils.random(2, true, false));
        plate.append("-");
        plate.append(RandomStringUtils.random(2, true, false));
        return plate.toString().toUpperCase();
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
        inv.setItem(10, Vehicles.mItem2(Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), 1, (short) Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), "&6Naam Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name")));
        inv.setItem(13, Vehicles.mItem("PAPER", 1, (short) 0, "&6Kenteken Aanpassen", "&7Huidige: &e" + ken));
        if ((boolean) Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".isGlow") == true) {
            inv.setItem(16, Vehicles.glowItem("BOOK", "&6Glow Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")));
        } else {
            inv.setItem(16, Vehicles.mItem("BOOK", 1, (short) 0, "&6Glow Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")));
        }
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, Vehicles.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, Vehicles.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, Vehicles.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }

    public static void benzineEdit(Player p) {
        Inventory inv = Bukkit.createInventory(null, 45, "Vehicle Benzine");
        String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
        ItemStack test = Vehicles.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine", "&7Huidige: &e" + (Boolean) Main.vehicleDataConfig.getConfig().get("vehicle." + ken + ".benzineEnabled"));
        ItemStack test2 = Vehicles.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Huidige Benzine", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzine"));
        ItemStack test3 = Vehicles.mItem2("DIAMOND_HOE", 1, (short) 58, "&6Benzine Verbruik", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzineVerbruik"));

        ItemStack car = (new ItemFactory(test).setNBT("mtvehicles.item", "1").toItemStack());
        ItemStack car2 = (new ItemFactory(test2).setNBT("mtvehicles.item", "2").toItemStack());
        ItemStack car3 = (new ItemFactory(test3).setNBT("mtvehicles.item", "3").toItemStack());

        inv.setItem(10, car);
        inv.setItem(13, car2);
        inv.setItem(16, car3);
        for (int i = 27; i <= 35; i++) {
            inv.setItem(i, Vehicles.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
        }
        inv.setItem(38, Vehicles.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
        inv.setItem(42, Vehicles.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));
        p.openInventory(inv);
    }
}
