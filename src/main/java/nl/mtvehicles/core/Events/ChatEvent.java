package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.NBTUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.Vehicles;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLicenseChat(AsyncPlayerChatEvent e) {
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

                if (e.isAsynchronous()) {


                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        Vehicles.menuEdit(p);
                    });
                }

                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                Vehicles.edit.put(p.getUniqueId() + ".kenteken", false);
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
                Main.vehicleDataConfig.save();
                return;
            }
            e.setCancelled(true);

            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    Vehicles.menuEdit(p);
                });
            }

            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            Vehicles.edit.put(p.getUniqueId() + ".kenteken", false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onNaamChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (Vehicles.edit.get(p.getUniqueId() + ".naam") == null) {
            return;
        }
        if (Vehicles.edit.get(p.getUniqueId() + ".naam") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".name", e.getMessage());
                Main.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(Vehicles.carItem2(Main.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), e.getMessage(), Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), ken));
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                Vehicles.edit.put(p.getUniqueId() + ".naam", false);
                if (e.isAsynchronous()) {

                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        Vehicles.menuEdit(p);
                    });

                }
                return;
            }
            e.setCancelled(true);
            Vehicles.menuEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            Vehicles.edit.put(p.getUniqueId() + ".naam", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    Vehicles.menuEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (Vehicles.edit.get(p.getUniqueId() + ".benzine") == null) {
            return;
        }
        if(isI(e.getMessage(), p) == false) {
            e.setCancelled(true);
            Vehicles.benzineEdit(p);
            Vehicles.edit.put(p.getUniqueId() + ".benzine", false);
            return;
        }
        if (Integer.parseInt(e.getMessage()) > 100){
            e.setCancelled(true);
            Vehicles.benzineEdit(p);
            Vehicles.edit.put(p.getUniqueId() + ".benzine", false);
            p.sendMessage(TextUtils.colorize("&cLetop! Het cijfer moet onder de 100 zijn!"));
            return;
        }
        if (Vehicles.edit.get(p.getUniqueId() + ".benzine") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzine", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                Vehicles.edit.put(p.getUniqueId() + ".benzine", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        Vehicles.benzineEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            Vehicles.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            Vehicles.edit.put(p.getUniqueId() + ".benzine", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    Vehicles.menuEdit(p);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineVerbruikChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (Vehicles.edit.get(p.getUniqueId() + ".benzineverbruik") == null) {
            return;
        }
        if(isD(e.getMessage(), p) == false) {
            e.setCancelled(true);
            Vehicles.benzineEdit(p);
            Vehicles.edit.put(p.getUniqueId() + ".benzineverbruik", false);
            return;
        }
        if (Vehicles.edit.get(p.getUniqueId() + ".benzineverbruik") == true) {
            if (!e.getMessage().contains("annule") || !e.getMessage().contains("Annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzineVerbruik", Double.valueOf(e.getMessage()));
                Main.vehicleDataConfig.save();
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionSuccessful")));
                Vehicles.edit.put(p.getUniqueId() + ".benzineverbruik", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> {
                        Vehicles.benzineEdit(p);
                    });
                }
                return;
            }
            e.setCancelled(true);
            Vehicles.benzineEdit(p);
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("actionCanceled")));
            Vehicles.edit.put(p.getUniqueId() + ".benzine", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    Vehicles.menuEdit(p);
                });
            }
        }
    }

    public boolean isI(String str, Player p) {
        try {
            Integer.parseInt(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cLetop! Het moet een cijfer zijn."));
            return false;
        }
        return true;
    }

    public boolean isD(String str, Player p) {
        try {
            Double.valueOf(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cLetop! Het moet een double zijn. bv 0.02"));
            return false;
        }
        return true;
    }
}
