package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VehicleClickEvent implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();
    public static HashMap<String, Double> speedhigh = new HashMap<>();
    public static HashMap<String, Integer> maxhight = new HashMap<>();
    private Map<String, Long> lastUsage = new HashMap<String, Long>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        Entity a = e.getRightClicked();
        Player p = e.getPlayer();
        long lastUsed = 0L;
        if (a.getCustomName() == null) {
            return;
        }
        if (!a.getCustomName().contains("MTVEHICLES")) {
            return;
        }
        if (lastUsage.containsKey(p.getName())) {
            lastUsed = ((Long) lastUsage.get(p.getName())).longValue();
        }
        if (System.currentTimeMillis() - lastUsed >= 500) {
            lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
        } else {
            return;
        }
        String license = TextUtils.licenseReplacer(a.getCustomName());
        if (p.isSneaking()) {
            pickupVehicle(license, p);
            e.setCancelled(true);
            return;
        }
        Main.configList.forEach(ConfigUtils::reload);
        if (a.getCustomName().contains("MTVEHICLES_SEAT")) {
            e.setCancelled(true);
            Vehicle vehicle = Vehicle.getByPlate(license);
            if (vehicle == null) {
                return;
            }
            if (vehicle.getOwner().equals(p.getUniqueId().toString()) || vehicle.canSit(p) || p.hasPermission("mtvehicles.ride")) {
                if (a.isEmpty()) {
                    a.setPassenger(p);
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterMember").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(license).getOwner())).getName())));
                }
            } else {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(license).getOwner())).getName())));
            }
            return;
        }
        createVehicle(license, p);
        e.setCancelled(true);
    }

    public static HashMap<String, Double> mainx = new HashMap<>();
    public static HashMap<String, Double> mainy = new HashMap<>();
    public static HashMap<String, Double> mainz = new HashMap<>();
    public static HashMap<String, Integer> seatsize = new HashMap<>();
    public static HashMap<String, Double> seatx = new HashMap<>();
    public static HashMap<String, Double> seaty = new HashMap<>();
    public static HashMap<String, Double> seatz = new HashMap<>();
    public static HashMap<String, Double> wiekenx = new HashMap<>();
    public static HashMap<String, Double> wiekeny = new HashMap<>();
    public static HashMap<String, Double> wiekenz = new HashMap<>();
    public static HashMap<String, String> type = new HashMap<>();
    public static HashMap<String, Double> benzine = new HashMap<>();
    public static HashMap<String, Double> benzineverbruik = new HashMap<>();

    public void createVehicle(String ken, Player p) {
        if (!(VehicleLeaveEvent.autostand2.get(ken) == null)) {
            if (!VehicleLeaveEvent.autostand2.get(ken).isEmpty()) {
                return;
            }
        }
        Vehicle vehicle = Vehicle.getByPlate(ken);
        if (vehicle == null) {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNotFound")));
            return;
        }
        if (!vehicle.getOwner().equals(p.getUniqueId().toString()) && !vehicle.canRide(p) && !p.hasPermission("mtvehicles.ride")) {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
            return;
        }
        for (Entity entity : p.getWorld().getEntities()) {
            if (Main.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER"))) {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleInWater")));
                return;
            }
            if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                ArmorStand vehicleAs = (ArmorStand) entity;
                if (!entity.isEmpty()) {
                    return;
                }
                benzine.put(ken, vehicle.getBenzine());
                benzineverbruik.put(ken, Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzineVerbruik"));
                type.put(ken, Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType"));
                Location location = new Location(entity.getWorld(), entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), entity.getLocation().getYaw(), entity.getLocation().getPitch());
                if (vehicleAs.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                    TextUtils.basicStandCreator(ken, "SKIN", location, vehicleAs.getHelmet(), false);
                    TextUtils.basicStandCreator(ken, "MAIN", location, null, true);
                    List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
                    for (int i = 1; i <= seats.size(); i++) {
                        Map<String, Double> seat = seats.get(i - 1);
                        if (i == 1) {
                            TextUtils.mainSeatStandCreator(ken, location, p, seat.get("x"), seat.get("y"), seat.get("z"));
                            BossbarUtils.addBossbar(p, ken);
                            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterRider").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                        }
                        if (i > 1) {
                            seatsize.put(ken, seats.size());
                            seatx.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, seat.get("x"));
                            seaty.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, seat.get("y"));
                            seatz.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, seat.get("z"));
                            Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("x")));
                            ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                            as.setCustomName("MTVEHICLES_SEAT" + (Integer) i + "_" + ken);
                            as.setGravity(false);
                            as.setVisible(false);
                            VehicleLeaveEvent.autostand.put("MTVEHICLES_SEAT" + (Integer) i + "_" + ken, as);
                        }
                    }
                    List<Map<String, Double>> wiekens = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
                    String vehicleType = Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType");
                    if (vehicleType.contains("HELICOPTER")) {
                        maxhight.put(ken, Main.defaultConfig.getConfig().getInt("helicopterMaxHight"));
                        if (vehicleType == null) return;
                        for (int i = 1; i <= wiekens.size(); i++) {
                            Map<?, ?> seat = wiekens.get(i - 1);
                            if (i == 1) {
                                Location location2 = new Location(location.getWorld(), location.getX() + (Double) seat.get("z"), (Double) location.getY() + (Double) seat.get("y"), location.getZ() + (Double) seat.get("x"));
                                wiekenx.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("x"));
                                wiekeny.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("y"));
                                wiekenz.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("z"));
                                ArmorStand as = location2.getWorld().spawn(location2, ArmorStand.class);
                                as.setCustomName("MTVEHICLES_WIEKENS_" + ken);
                                as.setGravity(false);
                                as.setVisible(false);
                                VehicleLeaveEvent.autostand.put("MTVEHICLES_WIEKENS_" + ken, as);
                                as.setHelmet((ItemStack) seat.get("item"));
                            }
                        }
                    }
                }
                vehicleAs.remove();
            }
        }
    }

    public void pickupVehicle(String ken, Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            if (Vehicle.getByPlate(ken) == null) {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNotFound")));
                return;
            }
            if (Vehicle.getByPlate(ken).getOwner().equals(p.getUniqueId().toString()) && Main.defaultConfig.getConfig().getBoolean("carPickup") == false || p.hasPermission("mtvehicles.oppakken")) {
                for (World world : Bukkit.getServer().getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        if (Main.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER"))) {
                            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleInWater")));
                            return;
                        }
                        if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                            ArmorStand test = (ArmorStand) entity;
                            if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                                if (checkInvFull(p) == false) {
                                    p.getInventory().addItem(test.getHelmet());
                                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehiclePickup").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                                } else {
                                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("inventoryFull")));
                                    return;
                                }
                            }
                            test.remove();
                        }
                    }
                }
            } else {
                if (Main.defaultConfig.getConfig().getBoolean("carPickup") == true) {
                    p.sendMessage(TextUtils.colorize("&cVoertuigen oppakken staat uitgeschakeld"));
                    return;
                }
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoOwnerPickup").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                return;
            }
        });
    }

    public boolean checkInvFull(Player p) {
        return !Arrays.asList(p.getInventory().getStorageContents()).contains(null);
    }
}