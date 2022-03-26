package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onLicenseChat(AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".kenteken") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".kenteken")) {
            e.setCancelled(true);

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);

                if (!(ConfigModule.vehicleDataConfig.get(e.getMessage(), VehicleDataConfig.Option.SKIN_ITEM) == null)) {
                    ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_FAILED_DUP_LICENSE);
                    MenuUtils.menuEdit(p);
                    ItemUtils.edit.put(p.getUniqueId() + ".kenteken", false);
                    return;
                }
                for (String s : ConfigModule.vehicleDataConfig.getConfig().getConfigurationSection("vehicle." + licensePlate).getKeys(false)) {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + e.getMessage() + "." + s, ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + licensePlate + "." + s));
                }

                ConfigModule.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(ItemUtils.carItem2(ConfigModule.vehicleDataConfig.getDamage(licensePlate), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString(), e.getMessage()));

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));

                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".kenteken", false);
                ConfigModule.vehicleDataConfig.delete(licensePlate);
                ConfigModule.vehicleDataConfig.save();
                return;
            }

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));

            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".kenteken", false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onNaamChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".naam") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".naam")) {
            e.setCancelled(true);

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.NAME, e.getMessage());
                ConfigModule.vehicleDataConfig.save();
                p.getInventory().setItemInMainHand(ItemUtils.carItem2(ConfigModule.vehicleDataConfig.getDamage(licensePlate), e.getMessage(), ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString(), licensePlate));
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".naam", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
                return;
            }

            MenuUtils.menuEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".naam", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".benzine") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".benzine")) {
            e.setCancelled(true);

            if (!isInt(e.getMessage(), p)) {
                MenuUtils.benzineEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
                return;
            }

            if (Integer.parseInt(e.getMessage()) > 100) {
                MenuUtils.benzineEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);
                p.sendMessage(TextUtils.colorize("&cLetop! Het cijfer moet onder de 100 zijn!"));
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.benzineEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBenzineVerbruikChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".benzineverbruik") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".benzineverbruik")) {
            e.setCancelled(true);

            if (!isDouble(e.getMessage(), p)) {
                MenuUtils.benzineEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".benzineverbruik", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_USAGE, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".benzineverbruik", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.benzineEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".benzine", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onKofferbakrowsChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".kofferbakRows") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".kofferbakRows")) {
            e.setCancelled(true);

            if (e.getMessage().toLowerCase().contains("annule")) {
                MenuUtils.kofferbakEdit(p);
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
                ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.kofferbakEdit(p));
            }

            if (!isInt(e.getMessage(), p)) {
                MenuUtils.kofferbakEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
                return;
            }

            int input = Integer.parseInt(e.getMessage());
            if (input < 1 || input > 6) {
                MenuUtils.kofferbakEdit(p);
                ConfigModule.messagesConfig.sendMessage(p, Message.INVALID_INPUT);
                ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);
                return;
            }

            String licensePlate = getLicensePlate(p);
            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ROWS, input);
            ConfigModule.vehicleDataConfig.save();
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
            ItemUtils.edit.put(p.getUniqueId() + ".kofferbakRows", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.kofferbakEdit(p));

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAcceleratieSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".acceleratieSpeed") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".acceleratieSpeed")) {
            e.setCancelled(true);

            if (!isDouble(e.getMessage(), p)) {
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.ACCELARATION_SPEED, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".acceleratieSpeed", false);
            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeed") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeed")) {
            e.setCancelled(true);

            if (!isDouble(e.getMessage(), p)) {
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.MAX_SPEED, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".maxSpeed", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBrakingSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".brakingSpeed") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".brakingSpeed")) {
            e.setCancelled(true);

            if (!isDouble(e.getMessage(), p)) {
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".brakingSpeed", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAftrekkenSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".aftrekkenSpeed") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".aftrekkenSpeed")) {
            e.setCancelled(true);

            if (!isDouble(e.getMessage(), p)) {
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".aftrekkenSpeed", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedBackwardsChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeedBackwards") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".maxSpeedBackwards")) {
            e.setCancelled(true);

            if (!isDouble(e.getMessage(), p)) {
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS, Double.valueOf(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                return;
            }

            MenuUtils.benzineEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".maxSpeedBackwards", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRotateSpeedChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        if (ItemUtils.edit.get(p.getUniqueId() + ".rotateSpeed") == null) return;

        if (ItemUtils.edit.get(p.getUniqueId() + ".rotateSpeed")) {
            e.setCancelled(true);

            if (!isInt(e.getMessage(), p)) {
                MenuUtils.speedEdit(p);
                ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);
                return;
            }

            if (!e.getMessage().toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(p);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.ROTATE_SPEED, Integer.parseInt(e.getMessage()));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);

                if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
                return;
            }

            MenuUtils.speedEdit(p);
            ConfigModule.messagesConfig.sendMessage(p, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(p.getUniqueId() + ".rotateSpeed", false);

            if (e.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(p));
        }
    }

    public boolean isInt(String str, Player p) {
        try {
            Integer.parseInt(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cPay attention! It must be an integer. (For example: 7)"));
            return false;
        }
        return true;
    }

    public boolean isDouble(String str, Player p) {
        try {
            Double.valueOf(str);
        } catch (Throwable e) {
            p.sendMessage(TextUtils.colorize("&cPay attention! It must be a double. (For example: 0.02)"));
            return false;
        }
        return true;
    }

    /**
     * Get license plate of player's held vehicle
     * @param player Player
     * @return License plate of player's held vehicle
     */
    private String getLicensePlate(Player player){
        NBTItem nbt = new NBTItem(player.getInventory().getItemInMainHand());
        return nbt.getString("mtvehicles.kenteken");
    }
}
