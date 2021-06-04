package nl.mtvehicles.core.Inventory;

import nl.mtvehicles.core.Events.JoinEvent;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class InventoryCloseEvent implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent e) {
        if (e.getView().getTitle().contains("Kofferbak Vehicle: ")) {
            String ken = e.getView().getTitle().replace("Kofferbak Vehicle: ", "");
            List<ItemStack> chest = (List<ItemStack>) Main.vehicleDataConfig.getConfig().getList("vehicle." + ken + ".kofferbakData");
            chest.removeAll(chest);
            for (ItemStack item : e.getInventory().getContents()) {
                chest.add(item);
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbakData", chest);
                Main.vehicleDataConfig.save();
            }
        }
        if (e.getView().getTitle().contains("Choose your language")) {
            Player p = (Player) e.getPlayer();
            if (JoinEvent.languageCheck.get(p.getUniqueId())) {
                p.sendMessage(TextUtils.colorize("&cThe language settings have not changed because the menu is closed. Do you want to change this anyway? Then do /vehicle language"));
            }
        }
    }
}