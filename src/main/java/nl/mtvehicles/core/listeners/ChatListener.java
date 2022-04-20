package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.events.ChatEvent;
import nl.mtvehicles.core.infrastructure.dataconfig.VehicleDataConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.MenuUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends MTVListener {

    public ChatListener(){
        super(new ChatEvent());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLicensePlateChange(AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".kenteken") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".kenteken")) {
            event.setCancelled(true);

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);

                if (!(ConfigModule.vehicleDataConfig.get(message, VehicleDataConfig.Option.SKIN_ITEM) == null)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_FAILED_DUP_LICENSE);
                    MenuUtils.menuEdit(player);
                    ItemUtils.edit.put(player.getUniqueId() + ".kenteken", false);
                    return;
                }
                for (String s : ConfigModule.vehicleDataConfig.getConfig().getConfigurationSection("vehicle." + licensePlate).getKeys(false)) {
                    ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + message + "." + s, ConfigModule.vehicleDataConfig.getConfig().get("vehicle." + licensePlate + "." + s));
                }

                ConfigModule.vehicleDataConfig.save();
                player.getInventory().setItemInMainHand(ItemUtils.getVehicleItem(
                        ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                        ConfigModule.vehicleDataConfig.getDamage(licensePlate),
                        (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING),
                        ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.NAME).toString(),
                        message)
                );

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(player));

                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".kenteken", false);
                ConfigModule.vehicleDataConfig.delete(licensePlate);
                ConfigModule.vehicleDataConfig.save();
                return;
            }

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(player));

            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".kenteken", false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onVehicleNameChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".naam") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".naam")) {
            event.setCancelled(true);

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.NAME, message);
                ConfigModule.vehicleDataConfig.save();
                player.getInventory().setItemInMainHand(ItemUtils.getVehicleItem(
                        ItemUtils.getMaterial(ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.SKIN_ITEM).toString()),
                        ConfigModule.vehicleDataConfig.getDamage(licensePlate),
                        (boolean) ConfigModule.vehicleDataConfig.get(licensePlate, VehicleDataConfig.Option.IS_GLOWING),
                        message,
                        licensePlate)
                );
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".naam", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(player));
                return;
            }

            MenuUtils.menuEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".naam", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFuelChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".benzine") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".benzine")) {
            event.setCancelled(true);

            if (!isInt(message)) {
                MenuUtils.benzineEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);
                return;
            }

            if (Integer.parseInt(message) > 100) {
                MenuUtils.benzineEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);
                player.sendMessage(TextUtils.colorize("&cLetop! Het cijfer moet onder de 100 zijn!"));
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.benzineEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFuelUsageChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".benzineverbruik") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".benzineverbruik")) {
            event.setCancelled(true);

            if (!isDouble(message)) {
                MenuUtils.benzineEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".benzineverbruik", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FUEL_USAGE, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".benzineverbruik", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.benzineEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.menuEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTrunkRowsChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".kofferbakRows") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".kofferbakRows")) {
            event.setCancelled(true);

            if (message.toLowerCase().contains("annule")) {
                MenuUtils.trunkEdit(player);
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
                ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.trunkEdit(player));
            }

            if (!isInt(message)) {
                MenuUtils.trunkEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);
                return;
            }

            int input = Integer.parseInt(message);
            if (input < 1 || input > 6) {
                MenuUtils.trunkEdit(player);
                ConfigModule.messagesConfig.sendMessage(player, Message.INVALID_INPUT);
                ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);
                return;
            }

            String licensePlate = getLicensePlate(player);
            ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.TRUNK_ROWS, input);
            ConfigModule.vehicleDataConfig.save();
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
            ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.trunkEdit(player));

        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAccelerationSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".acceleratieSpeed") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".acceleratieSpeed")) {
            event.setCancelled(true);

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.ACCELARATION_SPEED, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", false);
            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".maxSpeed") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".maxSpeed")) {
            event.setCancelled(true);

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.MAX_SPEED, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBrakingSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".brakingSpeed") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".brakingSpeed")) {
            event.setCancelled(true);

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.BRAKING_SPEED, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFrictionSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".aftrekkenSpeed") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".aftrekkenSpeed")) {
            event.setCancelled(true);

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.FRICTION_SPEED, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedBackwardsChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".maxSpeedBackwards") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".maxSpeedBackwards")) {
            event.setCancelled(true);

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.MAX_SPEED_BACKWARDS, Double.valueOf(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onRotateSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String message = event.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".rotateSpeed") == null) return;

        ChatEvent api = (ChatEvent) getAPI();
        api.setMessage(message);
        callAPI();
        if (isCancelled()) return;

        message = api.getMessage();

        if (ItemUtils.edit.get(player.getUniqueId() + ".rotateSpeed")) {
            event.setCancelled(true);

            if (!isInt(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("annule")) {
                String licensePlate = getLicensePlate(player);
                ConfigModule.vehicleDataConfig.set(licensePlate, VehicleDataConfig.Option.ROTATION_SPEED, Integer.parseInt(message));
                ConfigModule.vehicleDataConfig.save();
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", false);

                if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", false);

            if (event.isAsynchronous()) Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.speedEdit(player));
        }
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (Throwable e) {
            player.sendMessage(TextUtils.colorize("&cPay attention! It must be an integer. (For example: 7)"));
            return false;
        }
        return true;
    }

    private boolean isDouble(String str) {
        try {
            Double.valueOf(str);
        } catch (Throwable e) {
            player.sendMessage(TextUtils.colorize("&cPay attention! It must be a double. (For example: 0.02)"));
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
