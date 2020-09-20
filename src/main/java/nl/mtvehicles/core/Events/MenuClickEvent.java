package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Commands.VehiclesSubs.EditCmd;
import nl.mtvehicles.core.Commands.VehiclesSubs.MenuCmd;
import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.Vehicles;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MenuClickEvent implements Listener {

    public HashMap<UUID, ItemStack> vehicleMenu = new HashMap<>();
    public HashMap<UUID, Inventory> skinMenu = new HashMap<>();
    public HashMap<UUID, Integer> intSave = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().contains("Vehicle Menu")) {
            e.setCancelled(true);
            List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
            List<Map<?, ?>> skins = (List<Map<?, ?>>) vehicles.get(e.getRawSlot()).get("cars");


            intSave.put(p.getUniqueId(), e.getRawSlot());

            Inventory inv = Bukkit.createInventory(null, 54, "Choose your vehicle");

            for (int i = 36; i <= 44; i++) {
                inv.setItem(i, Vehicles.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"));
            }

            inv.setItem(47, Vehicles.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"));
            inv.setItem(51, Vehicles.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"));

            for (Map<?, ?> skin : skins) {
                inv.addItem(Vehicles.carItem((int) skin.get("itemDamage"), ((String) skin.get("name")), (String) skin.get("SkinItem")));
            }
            skinMenu.put(p.getUniqueId(), inv);
            p.openInventory(inv);
            return;
        }

        if (e.getView().getTitle().contains("Choose your vehicle")) {
            if (e.getCurrentItem().equals(Vehicles.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(Vehicles.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                p.openInventory(MenuCmd.beginmenu.get(p.getUniqueId()));
                e.setCancelled(true);
                return;
            }
            if (e.getCurrentItem().equals(Vehicles.mItem("STAINED_GLASS_PANE", 1, (short) 0, "&c", "&c"))) {
                e.setCancelled(true);
                return;

            }

            e.setCancelled(true);
            vehicleMenu.put(p.getUniqueId(), e.getCurrentItem());
            Inventory inv = Bukkit.createInventory(null, 27, "Confirm getting vehicle");
            inv.setItem(11, Vehicles.woolItem("WOOL", "RED_WOOL", 1, (short) 14, "&4Annuleren", "&7Druk hier om het te annuleren."));
            inv.setItem(15, Vehicles.woolItem("WOOL", "LIME_WOOL", 1, (short) 5, "&aCreate Vehicle", "&7Druk hier als je het voertuigen wilt aanmaken en op je naam wilt zetten"));
            p.openInventory(inv);
        }

        if (e.getView().getTitle().contains("Confirm getting vehicle")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Annuleren")) {
                p.openInventory(skinMenu.get(p.getUniqueId()));
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Create Vehicle")) {

                List<Map<?, ?>> vehicles = Main.vehiclesConfig.getConfig().getMapList("voertuigen");
                System.out.println(vehicles.get(intSave.get(p.getUniqueId())).get("RotateSpeed"));

                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("completedvehiclegive")));
                p.getInventory().addItem(vehicleMenu.get(p.getUniqueId()));
                String kenteken = NBTUtils.getString(vehicleMenu.get(p.getUniqueId()), "mtvehicles.kenteken");
                String naam = NBTUtils.getString(vehicleMenu.get(p.getUniqueId()), "mtvehicles.naam");
                Vehicle vehicle = new Vehicle();

                vehicle.setLicensePlate("vehicle."+kenteken);
                vehicle.setName(naam);
                System.out.println(naam);
                vehicle.setSkinDamage(vehicleMenu.get(p.getUniqueId()).getDurability());
                vehicle.setSkinItem(vehicleMenu.get(p.getUniqueId()).getType().toString());
                vehicle.setGlow(false);
                vehicle.setBenzineEnabled((boolean) vehicles.get(intSave.get(p.getUniqueId())).get("benzineEnabled"));
                vehicle.setBenzine(100);
                vehicle.setKofferbak(true);
                vehicle.setKofferbakRows(1);
                vehicle.setKofferbakData(null);
                vehicle.setAcceleratieSpeed((double)vehicles.get(intSave.get(p.getUniqueId())).get("acceleratieSpeed"));
                vehicle.setMaxSpeed((double)vehicles.get(intSave.get(p.getUniqueId())).get("maxSpeed"));
                vehicle.setBrakingSpeed((double)vehicles.get(intSave.get(p.getUniqueId())).get("brakingSpeed"));
                vehicle.setAftrekkenSpeed((double)vehicles.get(intSave.get(p.getUniqueId())).get("aftrekkenSpeed"));
                vehicle.setRotateSpeed((int) vehicles.get(intSave.get(p.getUniqueId())).get("rotateSpeed"));
                vehicle.setMaxSpeedBackwards((double)vehicles.get(intSave.get(p.getUniqueId())).get("maxSpeedBackwards"));
                vehicle.setOwner(p.getUniqueId().toString());


                vehicle.save();
                p.closeInventory();
            }
        }

        if (e.getView().getTitle().contains("Vehicle Edit")) {
            e.setCancelled(true);
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Vehicle Settings")) {

                Vehicles.menuEdit(p);


            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Benzine Settings")) {
                p.sendMessage("benzine");
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Kofferbak Settings")) {
                p.sendMessage("koffer");
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Member Settings")) {
                p.sendMessage("member");
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Speed Settings")) {
                p.sendMessage("speed");
            }
            if (e.getCurrentItem().getItemMeta().getDisplayName().contains("Delete Vehicle")) {
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle."+ken, null);
                Main.vehicleDataConfig.save();
                p.getInventory().getItemInMainHand().setAmount(0);
                p.closeInventory();
            }


        }

        if (e.getView().getTitle().contains("Vehicle Settings")) {
            e.setCancelled(true);
            String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
            if (e.getCurrentItem().equals(Vehicles.mItem("BARRIER", 1, (short) 0, "&4Sluiten", "&cDruk hier om het menu te sluiten!"))) {
                e.setCancelled(true);
                p.closeInventory();
                return;
            }
            if (e.getCurrentItem().equals(Vehicles.mItem("WOOD_DOOR", 1, (short) 0, "&6Terug", "&eDruk hier om terug te gaan!"))) {
                EditCmd.editMenu(p, p.getInventory().getItemInMainHand());
                e.setCancelled(true);
                return;
            }

            if (e.getCurrentItem().equals(Vehicles.glowItem("BOOK", "&6Glow Aanpassen", "&7Huidige: &e" + Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")))) {
                Main.vehicleDataConfig.getConfig().set("vehicle."+ken+".isGlow", false);
                ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
                im.removeEnchant(Enchantment.ARROW_INFINITE);
                im.removeItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                p.getInventory().getItemInMainHand().setItemMeta(im);
                Main.vehicleDataConfig.save();
                Vehicles.menuEdit(p);


            }

            if (e.getCurrentItem().equals(Vehicles.mItem("BOOK", 1 , (short)0, "&6Glow Aanpassen", "&7Huidige: &e"+Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".isGlow")))) {
                Main.vehicleDataConfig.getConfig().set("vehicle."+ken+".isGlow", true);
                ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
                im.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                im.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
                p.getInventory().getItemInMainHand().setItemMeta(im);
                Main.vehicleDataConfig.save();
                Vehicles.menuEdit(p);
            }

            if (e.getCurrentItem().equals(Vehicles.mItem("PAPER", 1 , (short)0, "&6Kenteken Aanpassen", "&7Huidige: &e"+ken))) {
                p.closeInventory();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("typeLicenseInChat")));
                Vehicles.edit.put(p.getUniqueId()+".kenteken", true);
            }

            if (e.getCurrentItem().equals(Vehicles.mItem2(Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), 1 , (short)Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), "&6Naam Aanpassen", "&7Huidige: &e"+Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name")))) {
                p.closeInventory();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("typeNameInChat")));
                Vehicles.edit.put(p.getUniqueId()+".naam", true);
            }
        }
    }

}
