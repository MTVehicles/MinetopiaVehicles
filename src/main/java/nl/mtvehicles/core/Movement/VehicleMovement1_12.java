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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;

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
        String license = p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
        if (VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + license) == null) {
            return;
        }
        if (VehicleClickEvent.speed.get(license) == null) {
            VehicleClickEvent.speed.put(license, 0.0);
            return;
        }
        if (VehicleClickEvent.benzine.get(license) < 1) {
            BossbarUtils.setbossbarvalue(0 / 100.0D, license);
            return;
        }
        BossbarUtils.setbossbarvalue(VehicleClickEvent.benzine.get(license) / 100.0D, license);
        ArmorStand standMain = VehicleLeaveEvent.autostand.get("MTVEHICLES_MAIN_" + license);
        ArmorStand standSkin = VehicleLeaveEvent.autostand.get("MTVEHICLES_SKIN_" + license);
        ArmorStand standMainSeat = VehicleLeaveEvent.autostand.get("MTVEHICLES_MAINSEAT_" + license);
        ArmorStand standRotors = VehicleLeaveEvent.autostand.get("MTVEHICLES_WIEKENS_" + license);
        ((CraftArmorStand) standSkin).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw(), standMain.getLocation().getPitch());
        mainSeat(standMain, (CraftArmorStand) standMainSeat, license);
        updateStand(standMain, license, ppisv.c());
        slabCheck(standMain, license);
        if (VehicleClickEvent.type.get(license).contains("HELICOPTER")) {
            rotors(standMain, standRotors, license);
        }
        if (ppisv.a() > 0.0) {
            ((CraftArmorStand) standMain).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() - Vehicle.getByPlate(license).getRotateSpeed(), standMain.getLocation().getPitch());
        } else if (ppisv.a() < 0.0) {
            ((CraftArmorStand) standMain).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() + Vehicle.getByPlate(license).getRotateSpeed(), standMain.getLocation().getPitch());
        }
        if (ppisv.b() > 0.0) {
            if (VehicleClickEvent.speed.get(license) < 0) {
                VehicleClickEvent.speed.put(license, VehicleClickEvent.speed.get(license) + Vehicle.getByPlate(license).getBrakingSpeed());
                return;
            }
            if (Main.defaultConfig.getConfig().getBoolean("benzine") == true && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled") == true) {
                double dnum = VehicleClickEvent.benzine.get(license) - VehicleClickEvent.benzineverbruik.get(license);
                VehicleClickEvent.benzine.put(license, dnum);
            }
            if (VehicleClickEvent.speed.get(license) > Vehicle.getByPlate(license).getMaxSpeed()) {
                return;
            }
            VehicleClickEvent.speed.put(license, VehicleClickEvent.speed.get(license) + Vehicle.getByPlate(license).getAcceleratieSpeed());
        }
        if (ppisv.b() < 0.0) {
            if (VehicleClickEvent.speed.get(license) > 0) {
                VehicleClickEvent.speed.put(license, VehicleClickEvent.speed.get(license) - Vehicle.getByPlate(license).getBrakingSpeed());
                return;
            }
            if (Main.defaultConfig.getConfig().getBoolean("benzine") == true && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled") == true) {
                double dnum = VehicleClickEvent.benzine.get(license) - VehicleClickEvent.benzineverbruik.get(license);
                VehicleClickEvent.benzine.put(license, dnum);
            }
            if (VehicleClickEvent.speed.get(license) < -Vehicle.getByPlate(license).getMaxSpeedBackwards()) {
                return;
            }
            VehicleClickEvent.speed.put(license, VehicleClickEvent.speed.get(license) - Vehicle.getByPlate(license).getAcceleratieSpeed());

        }
        if (ppisv.b() == 0.0) {
            BigDecimal round = new BigDecimal(VehicleClickEvent.speed.get(license)).setScale(1, BigDecimal.ROUND_DOWN);
            if (Double.parseDouble(String.valueOf(round)) == 0.0) {
                VehicleClickEvent.speed.put(license, 0.0);
                return;
            }
            if (Double.parseDouble(String.valueOf(round)) > 0.01) {
                VehicleClickEvent.speed.put(license, VehicleClickEvent.speed.get(license) - Vehicle.getByPlate(license).getAftrekkenSpeed());
                return;
            }
            if (Double.parseDouble(String.valueOf(round)) < 0.01) {
                VehicleClickEvent.speed.put(license, VehicleClickEvent.speed.get(license) + Vehicle.getByPlate(license).getAftrekkenSpeed());
                return;
            }
        }
    }

    public static void slabCheck(ArmorStand mainStand, String license) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            double xOffset = 0.7;
            double yOffset = 0.4;
            double zOffset = 0.0;
            Location locvp = mainStand.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
            float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
            float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
            Location loc = new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
            int data = loc.getBlock().getData();
            String locY = String.valueOf(mainStand.getLocation().getY());
            if (locY.substring(locY.length() - 2).contains(".5")) {
                if (loc.getBlock().getType().toString().contains("AIR")) {
                    return;
                }
                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE")) {
                        if (data < 9) {
                            return;
                        }
                    }
                }
                ((CraftArmorStand) mainStand).getHandle().setLocation(mainStand.getLocation().getX(), mainStand.getLocation().getY() + 0.5, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
                return;
            }
            if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                if (loc.getBlock().getType().toString().contains("DOUBLE")) {
                    return;
                }
                if (data < 9) {
                    ((CraftArmorStand) mainStand).getHandle().setLocation(mainStand.getLocation().getX(), mainStand.getLocation().getY() + 0.5, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
                }
            }
        });
    }

    public static void updateStand(ArmorStand mainStand, String license, Boolean space) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            Location loc = mainStand.getLocation();
            Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() - 0.2, loc.getZ(), loc.getYaw(), loc.getPitch());
            if (VehicleClickEvent.type.get(license).contains("HELICOPTER")) {
                if (!location.getBlock().getType().equals(Material.AIR)) {
                    VehicleClickEvent.speed.put(license, 0.0);
                }
                if (space) {
                    mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getX(), 0.2, mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getZ()));
                    return;
                }
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getX(), -0.2, mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getZ()));
                return;
            }
            if (VehicleClickEvent.type.get(license).contains("HOVER")) {
                if (location.getBlock().getType().equals(Material.AIR)) {
                    mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getZ()));
                    return;
                }
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getX(), 0.00001, mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getZ()));
                return;
            }
            if (location.getBlock().getType().toString().contains("AIR") || location.getBlock().getType().toString().contains("WATER")) {
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getZ()));
                return;
            }
            mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getX(), 0.0, mainStand.getLocation().getDirection().multiply(VehicleClickEvent.speed.get(license)).getZ()));
        });
    }

    public static void mainSeat(ArmorStand mainStand, CraftArmorStand mainseat, String license) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            if (!(VehicleClickEvent.seatsize.get(license) == null)) {
                for (int i = 2; i <= VehicleClickEvent.seatsize.get(license); i++) {
                    ArmorStand seatas = VehicleLeaveEvent.autostand.get("MTVEHICLES_SEAT" + i + "_" + license);
                    double xOffset = VehicleClickEvent.seatx.get("MTVEHICLES_SEAT" + i + "_" + license);
                    double yOffset = VehicleClickEvent.seaty.get("MTVEHICLES_SEAT" + i + "_" + license);
                    double zOffset = VehicleClickEvent.seatz.get("MTVEHICLES_SEAT" + i + "_" + license);
                    Location locvp = mainStand.getLocation().clone();
                    Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
                    float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
                    float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
                    Location loc = new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
                    EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
                    stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
                }
            }
            double xOffset = VehicleClickEvent.mainx.get("MTVEHICLES_MAINSEAT_" + license);
            double yOffset = VehicleClickEvent.mainy.get("MTVEHICLES_MAINSEAT_" + license);
            double zOffset = VehicleClickEvent.mainz.get("MTVEHICLES_MAINSEAT_" + license);
            Location locvp = mainStand.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
            float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
            float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
            Location loc = new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
            mainseat.getHandle().setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());

        });
    }

    public static void rotors(ArmorStand main, ArmorStand seatas, String license) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            double xOffset = VehicleClickEvent.wiekenx.get("MTVEHICLES_WIEKENS_" + license);
            double yOffset = VehicleClickEvent.wiekeny.get("MTVEHICLES_WIEKENS_" + license);
            double zOffset = VehicleClickEvent.wiekenz.get("MTVEHICLES_WIEKENS_" + license);
            final Location locvp = main.getLocation().clone();
            final Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
            final float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(seatas.getLocation().getYaw())));
            final float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(seatas.getLocation().getYaw())));
            final Location loc = new Location(main.getWorld(), xvp, main.getLocation().getY() + yOffset, zvp, seatas.getLocation().getYaw(), fbvp.getPitch());
            EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
            stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), seatas.getLocation().getYaw() + 15, seatas.getLocation().getPitch());
        });
    }
}