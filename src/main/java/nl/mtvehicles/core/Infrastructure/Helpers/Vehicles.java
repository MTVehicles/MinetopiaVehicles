package nl.mtvehicles.core.Infrastructure.Helpers;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Vehicles {

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
            String material = "WOOL";
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
}
