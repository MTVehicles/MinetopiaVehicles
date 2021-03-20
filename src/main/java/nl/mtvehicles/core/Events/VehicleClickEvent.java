package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.ItemFactory;
import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.ConfigUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class VehicleClickEvent implements Listener {
    public static HashMap<String, Double> speed = new HashMap<>();
    public static HashMap<String, Double> speedhigh = new HashMap<>();

    private Map<String, Long> lastUsage = new HashMap<String, Long>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        Entity a = event.getRightClicked();
        Player p = event.getPlayer();

        if (a.getCustomName() == null) {
            return;
        }

        if (!a.getCustomName().contains("MTVEHICLES")) {
            return;
        }

        long lastUsed = 0L;

        if (this.lastUsage.containsKey(p.getName())) {
            lastUsed = ((Long) this.lastUsage.get(p.getName())).longValue();
        }
        int cdmillis = 1 * 500;

        if (System.currentTimeMillis() - lastUsed >= cdmillis) {
            this.lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
        } else {
            return;
        }

        if (p.isSneaking()) {
            if (a.getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
                pickupVehicle(a.getCustomName().replace("MTVEHICLES_MAINSEAT_", ""), p);
                event.setCancelled(true);
                return;
            }
            if (a.getCustomName().contains("MTVEHICLES_MAIN_")) {
                pickupVehicle(a.getCustomName().replace("MTVEHICLES_MAIN_", ""), p);
                event.setCancelled(true);
                return;
            }
            if (a.getCustomName().contains("MTVEHICLES_SKIN_")) {
                pickupVehicle(a.getCustomName().replace("MTVEHICLES_SKIN_", ""), p);
                event.setCancelled(true);
                return;
            }
            if (a.getCustomName().contains("MTVEHICLES_WIEKENS_")) {
                event.setCancelled(true);
                return;
            }
            return;
        }

        Main.configList.forEach(ConfigUtils::reload);

        if (a.getCustomName().contains("MTVEHICLES_MAINSEAT_")) {
            createVehicle(a.getCustomName().replace("MTVEHICLES_MAINSEAT_", ""), p);
            event.setCancelled(true);
        }
        if (a.getCustomName().contains("MTVEHICLES_MAIN_")) {
            createVehicle(a.getCustomName().replace("MTVEHICLES_MAIN_", ""), p);
            event.setCancelled(true);
        }
        if (a.getCustomName().contains("MTVEHICLES_SKIN_")) {
            createVehicle(a.getCustomName().replace("MTVEHICLES_SKIN_", ""), p);
            event.setCancelled(true);
        }
        if (a.getCustomName().contains("MTVEHICLES_SEAT")) {
            event.setCancelled(true);
            String ken = a.getCustomName().substring(17);
            Vehicle vehicle = Vehicle.getByPlate(ken);
            if (vehicle == null) {
                return;
            }
            if (vehicle.getOwner().equals(p.getUniqueId().toString()) || vehicle.canSit(p) || p.hasPermission("mtvehicles.ride")) {
                if (a.isEmpty()) {
                    a.setPassenger(p);
                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterMember").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner())).getName())));
                }
            } else {
                p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner())).getName())));
            }
        }
        if (a.getCustomName().contains("MTVEHICLES_WIEKENS_")) {
            event.setCancelled(true);
        }
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

    public void createVehicle(final String ken, final Player p) {
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
        if (vehicle.getOwner().equals(p.getUniqueId().toString()) || vehicle.canRide(p) || p.hasPermission("mtvehicles.ride")) {
            for (final World world : Bukkit.getServer().getWorlds()) {
                for (final Entity entity : world.getEntities()) {
                    if (Main.defaultConfig.getConfig().getBoolean("anwb") && !p.hasPermission("mtvehicles.anwb") && (entity.getLocation().clone().add(0.0, 0.9, 0.0).getBlock().getType().toString().contains("WATER"))) {
                        p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleInWater")));
                        return;
                    }
                    if (entity.getCustomName() != null && entity.getCustomName().contains(ken)) {
                        final ArmorStand test = (ArmorStand) entity;
                        if (!entity.isEmpty()) {
                            return;
                        }
                        if (test.getCustomName().contains("MTVEHICLES_SKIN_" + ken)) {
                            benzine.put(ken, vehicle.getBenzine());
                            benzineverbruik.put(ken, Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzineVerbruik"));
                            type.put(ken, Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType"));
                            Location loc = test.getLocation();
                            Location location = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
                            ArmorStand as = location.getWorld().spawn(location, ArmorStand.class);
                            as.setCustomName("MTVEHICLES_SKIN_" + ken);
                            as.setHelmet(test.getHelmet());
                            ArmorStand as2 = location.getWorld().spawn(location, ArmorStand.class);
                            as2.setCustomName("MTVEHICLES_MAIN_" + ken);
                            List<Map<String, Double>> seats = (List<Map<String, Double>>) vehicle.getVehicleData().get("seats");
                            VehicleLeaveEvent.autostand.put("MTVEHICLES_SKIN_" + ken, as);
                            VehicleLeaveEvent.autostand.put("MTVEHICLES_MAIN_" + ken, as2);
                            as.setGravity(false);
                            as2.setGravity(true);
                            as.setVisible(false);
                            as2.setVisible(false);
                            for (int i = 1; i <= seats.size(); i++) {
                                Map<String, Double> seat = seats.get(i - 1);
                                if (i == 1) {
                                    Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("z")));
                                    ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                    as3.setCustomName("MTVEHICLES_MAINSEAT_" + ken);
                                    VehicleLeaveEvent.autostand.put("MTVEHICLES_MAINSEAT_" + ken, as3);
                                    as3.setGravity(false);
                                    speed.put(ken, 0.0);
                                    speedhigh.put(ken, 0.0);
                                    mainx.put("MTVEHICLES_MAINSEAT_" + ken, seat.get("x"));
                                    mainy.put("MTVEHICLES_MAINSEAT_" + ken, seat.get("y"));
                                    mainz.put("MTVEHICLES_MAINSEAT_" + ken, seat.get("z"));
                                    as3.setPassenger(p);
                                    as3.setVisible(false);
                                    VehicleLeaveEvent.autostand2.put(ken, as3);
                                    BossbarUtils.addBossbar(p, ken);
                                    p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleEnterRider").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
                                }
                                if (i > 1) {
                                    seatsize.put(ken, seats.size());
                                    seatx.put("MTVEHICLES_SEAT" + (int) i + "_" + ken, seat.get("x"));
                                    seaty.put("MTVEHICLES_SEAT" + (int) i + "_" + ken, seat.get("y"));
                                    seatz.put("MTVEHICLES_SEAT" + (int) i + "_" + ken, seat.get("z"));
                                    Location location2 = new Location(location.getWorld(), location.getX() + Double.valueOf(seat.get("z")), location.getY() + Double.valueOf(seat.get("y")), location.getZ() + Double.valueOf(seat.get("x")));
                                    ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                    as3.setCustomName("MTVEHICLES_SEAT" + (int) i + "_" + ken);
                                    as3.setGravity(false);
                                    as3.setVisible(false);
                                    VehicleLeaveEvent.autostand.put("MTVEHICLES_SEAT" + (int) i + "_" + ken, as3);
                                }
                            }
                            List<Map<String, Double>> wiekens = (List<Map<String, Double>>) vehicle.getVehicleData().get("wiekens");
                            String vehicleType = Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType");
                            if (vehicleType == null) return;
                            if (vehicleType.contains("HELICOPTER")) {
                                for (int i = 1; i <= wiekens.size(); i++) {
                                    Map<?, ?> seat = wiekens.get(i - 1);
                                    if (i == 1) {
                                        Location location2 = new Location(location.getWorld(), location.getX() + (double) seat.get("z"), (double) location.getY() + (double) seat.get("y"), location.getZ() + (double) seat.get("x"));
                                        wiekenx.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("x"));
                                        wiekeny.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("y"));
                                        wiekenz.put("MTVEHICLES_WIEKENS_" + ken, (Double) seat.get("z"));
                                        ArmorStand as3 = location2.getWorld().spawn(location2, ArmorStand.class);
                                        as3.setCustomName("MTVEHICLES_WIEKENS_" + ken);
                                        as3.setGravity(false);
                                        as3.setVisible(false);
                                        VehicleLeaveEvent.autostand.put("MTVEHICLES_WIEKENS_" + ken, as3);
                                        ItemStack car = (new ItemFactory(Material.getMaterial("DIAMOND_HOE"))).setDurability((short) 1058).setName(TextUtils.colorize("&6Wieken")).setNBT("mtvehicles.kenteken", ken).toItemStack();
                                        ItemMeta im = car.getItemMeta();
                                        List<String> itemlore = new ArrayList<>();
                                        itemlore.add(TextUtils.colorize("&a"));
                                        itemlore.add(TextUtils.colorize("&a" + ken));
                                        itemlore.add(TextUtils.colorize("&a"));
                                        im.setLore(itemlore);
                                        im.setUnbreakable(true);
                                        car.setItemMeta(im);
                                        if (!Main.instance.version.equals("v1_12_R1")) {
                                            as3.setHelmet((ItemStack) seat.get("item"));
                                        } else {
                                            as3.setHelmet(car);
                                        }
                                    }
                                }
                            }
                        }
                        test.remove();
                    }
                }
            }
        } else {
            p.sendMessage(TextUtils.colorize(Main.messagesConfig.getMessage("vehicleNoRiderEnter").replace("%p%", Bukkit.getOfflinePlayer(UUID.fromString(Vehicle.getByPlate(ken).getOwner().toString())).getName())));
        }
    }

    public void pickupVehicle(String ken, Player p) {
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
    }


    public boolean checkInvFull(Player p) {
        return !Arrays.asList(p.getInventory().getStorageContents()).contains(null);
    }
}
