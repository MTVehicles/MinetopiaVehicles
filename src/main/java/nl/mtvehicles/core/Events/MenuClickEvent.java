package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Commands.VehiclesSubs.MenuCmd;
import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.Vehicles;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("completedvehiclegive")));
                p.getInventory().addItem(vehicleMenu.get(p.getUniqueId()));
                p.closeInventory();
            }
        }
    }

}
