package nl.mtvehicles.core.Movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.Events.VehicleClickEvent;
import nl.mtvehicles.core.Events.VehicleLeaveEvent;
import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class VehicleMovement1_12 extends PacketAdapter {

    float yaw;
    int w;

    public VehicleMovement1_12() {
        super(Main.instance, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE);
        this.yaw = 0.0f;
        this.w = 0;
    }

    public void onPacketReceiving(final PacketEvent event) {
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) event.getPacket().getHandle();
        final Player p = event.getPlayer();
        if (p.getVehicle() == null || p.getVehicle().getCustomName() == null) {
            return;
        }
        if (p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "") == null) {
            return;
        }
        String ken = p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
        if (VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + ken) == null) {
            return;
        }
        if (VehicleClickEvent.speed.get(ken) == null) {
            VehicleClickEvent.speed.put(ken, 0.0);
            return;
        }
        if (Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzine") < 1) {
            BossbarUtils.setbossbarvalue(0 / 100.0D, ken);
        } else {
            BossbarUtils.setbossbarvalue(Vehicle.getByPlate(ken).getBenzine() / 100.0D, ken);
        }
        ArmorStand as = VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + ken);
        ArmorStand as2 = VehicleLeaveEvent.autostand.get("MTVEHICLES_SKIN_" + ken);
        ArmorStand as3 = VehicleLeaveEvent.autostand.get("MTVEHICLES_MAINSEAT_" + ken);
        ArmorStand as4 = VehicleLeaveEvent.autostand.get("MTVEHICLES_WIEKENS_" + ken);
        EntityArmorStand stand = ((CraftArmorStand) as2).getHandle();
        stand.setLocation(as.getLocation().getX(), as.getLocation().getY(), as.getLocation().getZ(), as.getLocation().getYaw(), as.getLocation().getPitch());
        mainSeat(as, as3, ken);
        if (!(VehicleClickEvent.seatsize.get(ken) == null)) {
            seat(as, ken);
        }
        Location loc = as.getLocation();
        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() - 0.2, loc.getZ(), loc.getYaw(), loc.getPitch());
        if (!Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType").contains("HELICOPTER")) {
            if (location.getBlock().getType().equals(Material.AIR) || location.getBlock().getType().toString().contains("WATER")) {
                KeyW(as, VehicleClickEvent.speed.get(ken), -0.8);
            } else {
                KeyW(as, VehicleClickEvent.speed.get(ken), 0.0);
            }
        } else {
            wiekens(as, as4, ken);
            if (!location.getBlock().getType().equals(Material.AIR) || location.getBlock().getType().equals(Material.WATER)) {
                VehicleClickEvent.speed.put(ken, 0.0);
            }
            KeyW(as, VehicleClickEvent.speed.get(ken), VehicleClickEvent.speedhigh.get(ken));

        }
        float forward = ppisv.b();
        float side = ppisv.a();
        boolean space = ppisv.c();
        boolean w;
        boolean s;
        if (Main.vehicleDataConfig.getConfig().getString("vehicle." + ken + ".vehicleType").contains("HELICOPTER")) {
            if (space) {
                VehicleClickEvent.speedhigh.put(ken, 0.2);
            } else {
                if (location.getBlock().getType().equals(Material.AIR) || location.getBlock().getType().toString().contains("WATER")) {
                    VehicleClickEvent.speedhigh.put(ken, -0.2);
                } else {
                    VehicleClickEvent.speedhigh.put(ken, 0.0);
                }
            }
        }
        if (forward > 0.0f) {

            if (!(Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzine") < 1)) {

                if (Main.defaultConfig.getConfig().getBoolean("benzine") == true && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + ken + ".benzineEnabled") == true) {
                    double dnum = Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzine") - Main.vehicleDataConfig.getConfig().getDouble("vehicle." + ken + ".benzineVerbruik");
                    Main.vehicleDataConfig.getConfig().set("vehicle." + ken + ".benzine", dnum);
                    Main.vehicleDataConfig.save();
                }
                if (VehicleClickEvent.speed.get(ken) > Vehicle.getByPlate(ken).getMaxSpeed()) {
                } else {
                    VehicleClickEvent.speed.put(ken, VehicleClickEvent.speed.get(ken) + Vehicle.getByPlate(ken).getAcceleratieSpeed());
                }
            } else {
                if (VehicleClickEvent.speed.get(ken) <= 0) {
                    VehicleClickEvent.speed.put(ken, 0.0);
                } else {
                    VehicleClickEvent.speed.put(ken, VehicleClickEvent.speed.get(ken) - Vehicle.getByPlate(ken).getAftrekkenSpeed());
                }

            }
            w = true;
            s = false;
        } else if (forward < 0.0f) {
            if (VehicleClickEvent.speed.get(ken) <= 0) {
                VehicleClickEvent.speed.put(ken, 0.0);
            } else {
                VehicleClickEvent.speed.put(ken, VehicleClickEvent.speed.get(ken) - Vehicle.getByPlate(ken).getBrakingSpeed());
            }
            w = false;
            s = true;
        } else {
            if (VehicleClickEvent.speed.get(ken) <= 0) {
                VehicleClickEvent.speed.put(ken, 0.0);
            } else {
                VehicleClickEvent.speed.put(ken, VehicleClickEvent.speed.get(ken) - Vehicle.getByPlate(ken).getAftrekkenSpeed());
            }
            w = false;
            s = false;
        }
        boolean a;
        boolean d;
        if (side > 0.0f) {
            KeyA(as, ken);
            a = true;
            d = false;
        } else if (side < 0.0f) {
            KeyD(as, ken);
            a = false;
            d = true;
        } else {
            a = false;
            d = false;
        }
    }

    public static void KeyW(ArmorStand as, double a, double b) {
        double xOffset = 0.7;
        double yOffset = 0.4;
        double zOffset = 0;
        Location locvp = as.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        Location loc = new Location(as.getWorld(), (double) xvp, as.getLocation().getY() + yOffset, (double) zvp, fbvp.getYaw(), fbvp.getPitch());
        if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
            as.setVelocity(new Vector(as.getLocation().getDirection().multiply((double) a).getX(), 0.5, as.getLocation().getDirection().multiply((double) a).getZ()));
        } else {
            Location loc2 = as.getLocation();
            Location location = new Location(loc2.getWorld(), loc2.getX(), loc2.getY(), loc2.getZ(), loc2.getYaw(), loc2.getPitch());
            if (location.getBlock().getType().toString().contains("STEP") || !loc.getBlock().getType().toString().contains("SLAB")) {
                as.setVelocity(new Vector(as.getLocation().getDirection().multiply((double) a).getX(), 0.5, as.getLocation().getDirection().multiply((double) a).getZ()));
                as.setVelocity(new Vector(as.getLocation().getDirection().multiply((double) a).getX(), b, as.getLocation().getDirection().multiply((double) a).getZ()));
            }
        }
    }

    public static void KeyD(ArmorStand a, String ken) {
        Location loc = a.getLocation();
        EntityArmorStand stand = ((CraftArmorStand) a).getHandle();
        int draai = Vehicle.getByPlate(ken).getRotateSpeed();
        stand.setLocation(a.getLocation().getX(), a.getLocation().getY(), a.getLocation().getZ(), loc.getYaw() + draai, loc.getPitch());
    }

    public static void KeyA(ArmorStand a, String ken) {
        Location loc = a.getLocation();
        EntityArmorStand stand = ((CraftArmorStand) a).getHandle();
        int draai = Vehicle.getByPlate(ken).getRotateSpeed();
        stand.setLocation(a.getLocation().getX(), a.getLocation().getY(), a.getLocation().getZ(), loc.getYaw() - draai, loc.getPitch());
    }

    public static void mainSeat(ArmorStand main, ArmorStand seatas, String ken) {
        double xOffset = VehicleClickEvent.mainx.get("MTVEHICLES_MAINSEAT_" + ken);
        double yOffset = VehicleClickEvent.mainy.get("MTVEHICLES_MAINSEAT_" + ken);
        double zOffset = VehicleClickEvent.mainz.get("MTVEHICLES_MAINSEAT_" + ken);
        Location locvp = main.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        Location loc = new Location(main.getWorld(), xvp, main.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
        EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
    }

    public static void wiekens(ArmorStand main, ArmorStand seatas, String ken) {
        double xOffset = VehicleClickEvent.wiekenx.get("MTVEHICLES_WIEKENS_" + ken);
        double yOffset = VehicleClickEvent.wiekeny.get("MTVEHICLES_WIEKENS_" + ken);
        double zOffset = VehicleClickEvent.wiekenz.get("MTVEHICLES_WIEKENS_" + ken);
        final Location locvp = main.getLocation().clone();
        final Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        final float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(seatas.getLocation().getYaw())));
        final float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(seatas.getLocation().getYaw())));
        final Location loc = new Location(main.getWorld(), (double) xvp, main.getLocation().getY() + yOffset, (double) zvp, seatas.getLocation().getYaw(), fbvp.getPitch());
        EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), seatas.getLocation().getYaw() + 15, seatas.getLocation().getPitch());
    }

    public static void seat(ArmorStand main, String ken) {
        for (int i = 2; i <= VehicleClickEvent.seatsize.get(ken); i++) {
            ArmorStand seatas = VehicleLeaveEvent.autostand.get("MTVEHICLES_SEAT" + i + "_" + ken);
            double xOffset = VehicleClickEvent.seatx.get("MTVEHICLES_SEAT" + i + "_" + ken);
            double yOffset = VehicleClickEvent.seaty.get("MTVEHICLES_SEAT" + i + "_" + ken);
            double zOffset = VehicleClickEvent.seatz.get("MTVEHICLES_SEAT" + i + "_" + ken);
            Location locvp = main.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
            float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
            float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
            Location loc = new Location(main.getWorld(), xvp, main.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
            EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
        }
    }
}