package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Main;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MenuClickEvent implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if(e.getView().getTitle().contains("Vehicle Menu")){
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();

            if(!e.getCurrentItem().hasItemMeta()){
                return;
            }

            List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
            List<Map<?, ?>> skins = (List<Map<?, ?>>) vehicles.get(e.getRawSlot()).get("cars");


            Inventory inv = Bukkit.createInventory(null, 54, "Choose your vehicle");
            System.out.println(skins);
            for (int i=36; i<=44; i++) {
                inv.setItem(i, mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
            }
            inv.setItem(47, mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
            inv.setItem(51, mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));

            for (Map<?, ?> skin : skins) {
                inv.addItem(carItem((int) skin.get("itemDamage"), ((String)skin.get("name")), (String) skin.get("SkinItem")));
            }

            p.openInventory(inv);


        }
    }

    public ItemStack carItem(int id, String name, String material){
        ItemStack car = (new ItemFactory(Material.getMaterial(material))).setDurability((short)id).setName(TextUtils.colorize("&6"+name).replace(" ", " - ")).toItemStack();
        ItemMeta im = car.getItemMeta();
        List<String> itemlore = new ArrayList<>();
        itemlore.add(TextUtils.colorize("&a"));
        itemlore.add(TextUtils.colorize("&a"+generateLicencePlate()));
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
                ItemStack is =  new ItemStack(Material.matchMaterial(material, true), amount, durability);
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
