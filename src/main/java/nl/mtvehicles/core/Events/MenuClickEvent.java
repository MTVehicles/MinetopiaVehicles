package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Commands.VehiclesSubs.MenuCmd;
import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Main;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;

public class MenuClickEvent implements Listener {

    public HashMap<UUID, ItemStack> vehicleMenu = new HashMap<>();
    public HashMap<UUID, Inventory> skinMenu = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().contains("Vehicle Menu")) {
            e.setCancelled(true);
            List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
            List<Map<?, ?>> skins = (List<Map<?, ?>>) vehicles.get(e.getRawSlot()).get("cars");

            Inventory inv = Bukkit.createInventory(null, 54, "Choose your vehicle");

            for (int i = 36; i <= 44; i++) {
                inv.setItem(i, mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
            }

            inv.setItem(47, mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
            inv.setItem(51, mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));

            for (Map<?, ?> skin : skins) {
                inv.addItem(carItem((int) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem")));
            }
            skinMenu.put(p.getUniqueId(), inv);
            p.openInventory(inv);
            return;
        }

        if (e.getView().getTitle().contains("Choose your vehicle")) {
            if (e.getCurrentItem().equals(mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                p.openInventory(MenuCmd.beginmenu.get(p.getUniqueId()));
                e.setCancelled(true);
                return;
            }

            e.setCancelled(true);
            vehicleMenu.put(p.getUniqueId(), e.getCurrentItem());
            Inventory inv = Bukkit.createInventory(null, 27, "Confirm getting vehicle");
            inv.setItem(11, woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&4Annuleren", "&7Druk hier om het te annuleren."));
            inv.setItem(15, woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&aCreate Vehicle", "&7Druk hier als je het voertuigen wilt aanmaken en op je naam wilt zetten"));
            p.openInventory(inv);
        }

        if (e.getView().getTitle().contains("Confirm getting vehicle")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Annuleren")) {
                p.openInventory(skinMenu.get(p.getUniqueId()));
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Create Vehicle")) {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("completedvehiclegive")));
                p.getInventory().addItem(vehicleMenu.get(p.getUniqueId()));
                p.closeInventory();
            }
        }
    }

    public ItemStack carItem(int id, String name, String material) {
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short) id).setName(TextUtils.colorize("&6" + name).replace(" ", " - ")).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a" + generateLicencePlate()));
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
