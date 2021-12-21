package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.helpers.NBTUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLicenseChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".kenteken") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".kenteken")) {
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                if (!(ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + e.getMessage() + ".skinItem") == null)) {
                    ConfigModule.messagesConfig.sendMessage(p, "actionFailedDupLicense");
                    MenuUtils.menuEdit(p);
                    ItemUtils.edit.put(p.getUniqueId() + ".kenteken", false);
                    return;
                }
                for (String s : ConfigModule.vehicleDataConfig.getConfig().getConfigurationSection("vehicle." + ken).getKeys(false)) {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + e.getMessage() + "." + s, ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + ken + "." + s));
                }
                ConfigModule.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(ItemUtils.carItem2(ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".name"), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), e.getMessage()));
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
                }
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".kenteken", false);
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken, null);
                ConfigModule.vehicleDataConfig.save();
                return;
            }
            e.setCancelled(true);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
            }
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".kenteken", false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onNaamChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".naam") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".naam")) {
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".name", e.getMessage());
                ConfigModule.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(ItemUtils.carItem2(ConfigModule.vehicleDataConfig.getConfig().getInt("vehicle." + ken + ".skinDamage"), e.getMessage(), ConfigModule.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".skinItem"), ken));
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".naam", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.menuEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".naam", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".benzine") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".benzine")) {
            if (!isI(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.benzineEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
                return;
            }
            if (Integer.parseInt(e.getMessage()) > 100) {
                e.setCancelled(true);
                MenuUtils.benzineEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
                p.sendMessage(TextUtils.colorize("&cLetop! Het cijfer moet onder de 100 zijn!"));
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzine", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.benzineEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineVerbruikChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".benzineverbruik") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".benzineverbruik")) {
            if (!isD(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.benzineEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".benzineverbruik", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzineVerbruik", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".benzineverbruik", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.benzineEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKofferbakrowsChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".kofferbakRows") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".kofferbakRows")) {
            if (!isI(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.kofferbakEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".kofferbakRows", Integer.parseInt(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.kofferbakEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.kofferbakEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAcceleratieSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".acceleratieSpeed") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".acceleratieSpeed")) {
            if (!isD(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".acceleratieSpeed", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeed") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeed")) {
            if (!isD(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".maxSpeed", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBrakingSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".brakingSpeed") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".brakingSpeed")) {
            if (!isD(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".brakingSpeed", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAftrekkenSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".aftrekkenSpeed") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".aftrekkenSpeed")) {
            if (!isD(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".aftrekkenSpeed", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
            if (e.isAsynchronous()) {


                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedBackwardsChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeedBackwards") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeedBackwards")) {
            if (!isD(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".maxSpeedBackwards", Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                }
                return;
            }
            e.setCancelled(true);
            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRotateSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".rotateSpeed") == null) {
            return;
        }
        if (ItemUtils.edit.get(p.getUniqueId() + ".rotateSpeed")) {
            if (!isI(e.getMessage(), p)) {
                e.setCancelled(true);
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
                return;
            }
            if (!e.getMessage().toLowerCase().contains("annule")) {
                e.setCancelled(true);
                String ken = NBTUtils.getString(p.getInventory().getItemInMainHand(), "mtvehicles.kenteken");
                ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + ken + ".rotateSpeed", Integer.parseInt(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, "actionSuccessful");
                ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
                if (e.isAsynchronous()) {
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                }

                return;
            }
            e.setCancelled(true);
            MenuUtils.speedEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, "actionCanceled");
            ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
            if (e.isAsynchronous()) {
                Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
            }
        }
    }

    public boolean isI(String str, Player p) {
        try {
            Integer.parseInt(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cPay attention! It must be a number."));

            return false;
        }

        return true;
    }

    public boolean isD(String str, Player p) {
        try {
            Double.valueOf(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cPay attention! It must be a double. for example 0.02"));

            return false;
        }

        return true;
    }
}
