package nl.mtvehicles.core.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import nl.mtvehicles.core.commands.vehiclesubs.VehicleEdit;
import nl.mtvehicles.core.events.ChatEvent;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.utils.MenuUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static nl.mtvehicles.core.Main.schedulerRun;

/**
 * On player chat
 */
public class ChatListener extends MTVListener {

    public ChatListener(){
        super(new ChatEvent());
    }

    /**
     * Changing a license plate with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onLicensePlateChange(AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".kenteken") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".kenteken")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editLicensePlate(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".kenteken", false);
                
                if (event.isAsynchronous())
                    schedulerRun(() -> MenuUtils.menuEdit(player));
                return;
            }

            if (event.isAsynchronous())
                schedulerRun(() -> MenuUtils.menuEdit(player));

            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".kenteken", false);
        });
    }

    /**
     * Changing vehicle's name with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onVehicleNameChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".naam") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".naam")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editName(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".naam", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.menuEdit(player));
                return;
            }

            MenuUtils.menuEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".naam", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.menuEdit(player));
        });
    }

    /**
     * Changing vehicle's fuel amount with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onFuelChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".benzine") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".benzine")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isInt(message)) {
                MenuUtils.benzineEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editFuel(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.benzineEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.benzineEdit(player));
        });
    }

    /**
     * Changing vehicle's fuel consumption with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onFuelUsageChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".benzineverbruik") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".benzineverbruik")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isDouble(message)) {
                MenuUtils.benzineEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".benzineverbruik", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editFuelUsage(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".benzineverbruik", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.benzineEdit(player));
                return;
            }

            MenuUtils.benzineEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".benzine", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.benzineEdit(player));
        });
    }

    /**
     * Changing vehicle trunk's number of rows with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onTrunkRowsChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".kofferbakRows") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".kofferbakRows")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (message.toLowerCase().contains("!q")) {
                MenuUtils.trunkEdit(player);
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
                ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);

                if (event.isAsynchronous())
                    Bukkit.getScheduler().runTask(Main.instance, () -> MenuUtils.trunkEdit(player));
                return;
            }

            if (!isInt(message)) {
                MenuUtils.trunkEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);
                return;
            }

            String licensePlate = getLicensePlate(player);
            
            if (VehicleEdit.editTrunkRows(player, licensePlate, message)) {
                ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
            }
            
            ItemUtils.edit.put(player.getUniqueId() + ".kofferbakRows", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.trunkEdit(player));
        });
    }

    /**
     * Changing vehicle's acceleration speed with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onAccelerationSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".acceleratieSpeed") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".acceleratieSpeed")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editAccelerationSpeed(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".acceleratieSpeed", false);
            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
        });
    }

    /**
     * Changing vehicle's maximum speed with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".maxSpeed") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".maxSpeed")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editMaxSpeed(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".maxSpeed", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
        });
    }

    /**
     * Changing vehicle's braking speed with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onBrakingSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".brakingSpeed") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".brakingSpeed")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editBrakingSpeed(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".brakingSpeed", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
        });
    }

    /**
     * Changing vehicle's friction with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onFrictionSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".aftrekkenSpeed") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".aftrekkenSpeed")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editFrictionSpeed(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".aftrekkenSpeed", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
        });
    }

    /**
     * Changing vehicle's maximum backwards speed with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onMaxSpeedBackwardsChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".maxSpeedBackwards") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".maxSpeedBackwards")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isDouble(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editMaxSpeedBackwards(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".maxSpeedBackwards", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
        });
    }

    /**
     * Changing vehicle's rotation speed with /vehicle edit
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onRotateSpeedChange(final AsyncPlayerChatEvent event) {
        this.event = event;
        player = event.getPlayer();
        String realMessage = event.getMessage().trim();

        if (ItemUtils.edit.get(player.getUniqueId() + ".rotateSpeed") == null) return;
        if (!ItemUtils.edit.get(player.getUniqueId() + ".rotateSpeed")) return;
        event.setCancelled(true);

        schedulerRun(() -> {
            ChatEvent api = (ChatEvent) getAPI();
            api.setMessage(realMessage);
            callAPI();
            if (isCancelled()) return;

            String message = api.getMessage();

            if (!isInt(message)) {
                MenuUtils.speedEdit(player);
                ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", false);
                return;
            }

            if (!message.toLowerCase().contains("!q")) {
                String licensePlate = getLicensePlate(player);
                
                if (VehicleEdit.editRotationSpeed(player, licensePlate, message)) {
                    ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_SUCCESSFUL);
                }
                
                ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", false);

                if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
                return;
            }

            MenuUtils.speedEdit(player);
            ConfigModule.messagesConfig.sendMessage(player, Message.ACTION_CANCELLED);
            ItemUtils.edit.put(player.getUniqueId() + ".rotateSpeed", false);

            if (event.isAsynchronous()) schedulerRun(() -> MenuUtils.speedEdit(player));
        });
    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (Throwable e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_INTEGER);
            return false;
        }
        return true;
    }

    private boolean isDouble(String str) {
        try {
            Double.valueOf(str);
        } catch (Throwable e) {
            ConfigModule.messagesConfig.sendMessage(player, Message.MUST_BE_DOUBLE);
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
