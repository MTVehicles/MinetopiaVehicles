package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.Vehicles;
import nl.mtvehicles.core.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLicenseChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();


        if (Vehicles.edit.get(p.getUniqueId() + ".kenteken") == null) {
            return;
        }

        if (Vehicles.edit.get(p.getUniqueId() + ".kenteken") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                if (!(Main.vehicleDataConfig.getConfig().get("vehicle." + e.getMessage() + ".skinItem") == null)) {
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionFailedDupLicense")));
                    Vehicles.menuEdit(p);
                    Vehicles.edit.put(p.getUniqueId() + ".kenteken", false);
                    return;
                }
                for (String s : Main.vehicleDataConfig.getConfig().getConfigurationSection("vehicle." + ken).getKeys(false)) {
                    Main.vehicleDataConfig.getConfig().set("vehicle." + e.getMessage() + "." + s, Main.vehicleDataConfig.getConfig().get("vehicle." + ken + "." + s));
                }
                Main.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(Vehicles.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name"), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), e.getMessage()));
                Vehicles.menuEdit(p);
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                Vehicles.edit.put(p.getUniqueId() + ".kenteken", false);
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
                Main.vehicleDataConfig.save();
                return;
            }
            e.setCancelled(true);
            Vehicles.menuEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            Vehicles.edit.put(p.getUniqueId() + ".kenteken", false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onNaamChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (Vehicles.edit.get(p.getUniqueId() + ".naam") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".name", e.getMessage());
                Main.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(Vehicles.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), e.getMessage(), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), ken));
                Vehicles.menuEdit(p);
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                Vehicles.edit.put(p.getUniqueId() + ".naam", false);
                return;
            }
            e.setCancelled(true);
            Vehicles.menuEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            Vehicles.edit.put(p.getUniqueId() + ".naam", false);
        }
    }
}
