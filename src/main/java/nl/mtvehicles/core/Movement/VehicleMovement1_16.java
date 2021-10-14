package nl.mtvehicles.core.Movement;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.Infrastructure.Helpers.BossBarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehicleData;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;

public class VehicleMovement1_16 {

    public static void vehicleMovement(Player p, PacketPlayInSteerVehicle ppisv) {
        if (p.getVehicle() == null || p.getVehicle().getCustomName() == null) {
            return;
        }
        if (p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "") == null) {
            return;
        }
        String license = p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) {
            return;
        }
        if (VehicleData.speed.get(license) == null) {
            VehicleData.speed.put(license, 0.0);
            return;
        }
        if (VehicleData.fuel.get(license) < 1) {
            BossBarUtils.setBossBarValue(0 / 100.0D, license);
            return;
        }
        BossBarUtils.setBossBarValue(VehicleData.fuel.get(license) / 100.0D, license);
        ArmorStand standMain = VehicleData.autostand.get("MTVEHICLES_MAIN_" + license);
        ArmorStand standSkin = VehicleData.autostand.get("MTVEHICLES_SKIN_" + license);
        ArmorStand standMainSeat = VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + license);
        ArmorStand standRotors = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + license);
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            try {
                EntityArmorStand stand = ((CraftArmorStand) standSkin).getHandle();
                stand.setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standSkin.getLocation().getYaw(), standSkin.getLocation().getPitch());
            } catch (Exception e) {
                // e.printStackTrace();
            }
        });
        mainSeat(standMain, (CraftArmorStand) standMainSeat, license);
        updateStand(standMain, license, ppisv.d());
        slabCheck(standMain, license);
        if (VehicleData.seatsize.get(license+"addon") != null) {
            for (int i = 1; i <= VehicleData.seatsize.get(license + "addon"); i++) {
                ArmorStand standAddon = VehicleData.autostand.get("MTVEHICLES_ADDON" + i + "_" + license);
                net.minecraft.server.v1_12_R1.EntityArmorStand stand = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftArmorStand) standAddon).getHandle();
                stand.setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw(), standMain.getLocation().getPitch());
            }
        }
        if (VehicleData.type.get(license).contains("HELICOPTER")) {
            rotors(standMain, standRotors, license);
        }

        if (ppisv.b() > 0.0) {
            ((CraftArmorStand) standMain).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() - Vehicle.getByPlate(license).getRotateSpeed(), standMain.getLocation().getPitch());
            ((CraftArmorStand) standMainSeat).getHandle().setLocation(standMainSeat.getLocation().getX(), standMainSeat.getLocation().getY(), standMainSeat.getLocation().getZ(), standMainSeat.getLocation().getYaw() - Vehicle.getByPlate(license).getRotateSpeed(), standMainSeat.getLocation().getPitch());
            ((CraftArmorStand) standSkin).getHandle().setLocation(standSkin.getLocation().getX(), standSkin.getLocation().getY(), standSkin.getLocation().getZ(), standSkin.getLocation().getYaw() - Vehicle.getByPlate(license).getRotateSpeed(), standSkin.getLocation().getPitch());
        } else if (ppisv.b() < 0.0) {
            ((CraftArmorStand) standSkin).getHandle().setLocation(standSkin.getLocation().getX(), standSkin.getLocation().getY(), standSkin.getLocation().getZ(), standSkin.getLocation().getYaw() + Vehicle.getByPlate(license).getRotateSpeed(), standSkin.getLocation().getPitch());
            ((CraftArmorStand) standMainSeat).getHandle().setLocation(standMainSeat.getLocation().getX(), standMainSeat.getLocation().getY(), standMainSeat.getLocation().getZ(), standMainSeat.getLocation().getYaw() + Vehicle.getByPlate(license).getRotateSpeed(), standMainSeat.getLocation().getPitch());
            ((CraftArmorStand) standMain).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() + Vehicle.getByPlate(license).getRotateSpeed(), standMain.getLocation().getPitch());
        }
        if (ppisv.c() > 0.0) {
            if (VehicleData.speed.get(license) < 0) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) + Vehicle.getByPlate(license).getBrakingSpeed());
                return;
            }
            if (Main.defaultConfig.getConfig().getBoolean("benzine") == true && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled") == true) {
                double dnum = VehicleData.fuel.get(license) - VehicleData.fuelUsage.get(license);
                VehicleData.fuel.put(license, dnum);
            }
            if (VehicleData.speed.get(license) > Vehicle.getByPlate(license).getMaxSpeed()) {
                return;
            }
            VehicleData.speed.put(license, VehicleData.speed.get(license) + Vehicle.getByPlate(license).getAccelerationSpeed());
        }
        if (ppisv.c() < 0.0) {
            if (VehicleData.speed.get(license) > 0) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) - Vehicle.getByPlate(license).getBrakingSpeed());
                return;
            }
            if (Main.defaultConfig.getConfig().getBoolean("benzine") == true && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled") == true) {
                double dnum = VehicleData.fuel.get(license) - VehicleData.fuelUsage.get(license);
                VehicleData.fuel.put(license, dnum);
            }
            if (VehicleData.speed.get(license) < -Vehicle.getByPlate(license).getMaxSpeedBackwards()) {
                return;
            }
            VehicleData.speed.put(license, VehicleData.speed.get(license) - Vehicle.getByPlate(license).getAccelerationSpeed());

        }
        if (ppisv.c() == 0.0) {
            BigDecimal round = new BigDecimal(VehicleData.speed.get(license)).setScale(1, BigDecimal.ROUND_DOWN);
            if (Double.parseDouble(String.valueOf(round)) == 0.0) {
                VehicleData.speed.put(license, 0.0);
                return;
            }
            if (Double.parseDouble(String.valueOf(round)) > 0.01) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) - Vehicle.getByPlate(license).getFrictionSpeed());
                return;
            }
            if (Double.parseDouble(String.valueOf(round)) < 0.01) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) + Vehicle.getByPlate(license).getFrictionSpeed());
                return;
            }
        }
    }

    public static void slabCheck(ArmorStand mainStand, String license) {
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
        if (!locY.substring(locY.length() - 2).contains(".5")) {
            if (!loc.getBlock().isPassable() && !loc.getBlock().getType().toString().contains("STEP") && !loc.getBlock().getType().toString().contains("SLAB")) {
                VehicleData.speed.put(license, 0.0);
            }
        }
        if (locY.substring(locY.length() - 2).contains(".5")) {
            if (loc.getBlock().getType().toString().contains("AIR")) {
                return;
            }
            if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                if (!loc.getBlock().getType().toString().contains("DOUBLE")) {
                    if (data == 0 || data == 5) {
                        return;
                    }
                }
            }
            Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
                try {
                    ((CraftArmorStand) mainStand).getHandle().setLocation(mainStand.getLocation().getX(), mainStand.getLocation().getY() + 0.5, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            });
            return;
        }
        if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
            if (loc.getBlock().getType().toString().contains("DOUBLE")) {
                return;
            }
            if (data == 0 || data == 5) {
                Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
                    try {
                        ((CraftArmorStand) mainStand).getHandle().setLocation(mainStand.getLocation().getX(), mainStand.getLocation().getY() + 0.5, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                });
            }
        }
    }

    public static void updateStand(ArmorStand mainStand, String license, Boolean space) {
        Location loc = mainStand.getLocation();
        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() - 0.2, loc.getZ(), loc.getYaw(), loc.getPitch());
        if (VehicleData.type.get(license).contains("HELICOPTER")) {
            if (!location.getBlock().getType().equals(Material.AIR)) {
                VehicleData.speed.put(license, 0.0);
            }
            if (space) {
                if (mainStand.getLocation().getY() > Main.instance.getConfig().getInt("helicopterMaxHight")) {
                    return;
                }
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), 0.2, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                return;
            }
            mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.2, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
            return;
        }
        if (VehicleData.type.get(license).contains("HOVER")) {
            if (location.getBlock().getType().toString().contains("AIR")) {
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                return;
            }
            mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), 0.00001, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
            return;
        }
        if (location.getBlock().getType().toString().contains("AIR") || location.getBlock().getType().toString().contains("WATER")) {
            mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
            return;
        }
        mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), 0.0, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
    }

    public static void mainSeat(ArmorStand mainStand, CraftArmorStand mainseat, String license) {
        if (!(VehicleData.seatsize.get(license) == null)) {
            for (int i = 2; i <= VehicleData.seatsize.get(license); i++) {
                ArmorStand seatas = VehicleData.autostand.get("MTVEHICLES_SEAT" + i + "_" + license);
                double xOffset = VehicleData.seatx.get("MTVEHICLES_SEAT" + i + "_" + license);
                double yOffset = VehicleData.seaty.get("MTVEHICLES_SEAT" + i + "_" + license);
                double zOffset = VehicleData.seatz.get("MTVEHICLES_SEAT" + i + "_" + license);
                Location locvp = mainStand.getLocation().clone();
                Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
                float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
                float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
                Location loc = new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
                EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
                Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
                    try {
                        stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }

                });
            }
        }
        double xOffset = VehicleData.mainx.get("MTVEHICLES_MAINSEAT_" + license);
        double yOffset = VehicleData.mainy.get("MTVEHICLES_MAINSEAT_" + license);
        double zOffset = VehicleData.mainz.get("MTVEHICLES_MAINSEAT_" + license);
        Location locvp = mainStand.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        Location loc = new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            try {
                mainseat.getHandle().setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
            } catch (Exception e) {
                // e.printStackTrace();
            }


        });
    }

    public static void rotors(ArmorStand main, ArmorStand seatas, String license) {
        double xOffset = VehicleData.wiekenx.get("MTVEHICLES_WIEKENS_" + license);
        double yOffset = VehicleData.wiekeny.get("MTVEHICLES_WIEKENS_" + license);
        double zOffset = VehicleData.wiekenz.get("MTVEHICLES_WIEKENS_" + license);
        final Location locvp = main.getLocation().clone();
        final Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        final float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(seatas.getLocation().getYaw())));
        final float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(seatas.getLocation().getYaw())));
        final Location loc = new Location(main.getWorld(), xvp, main.getLocation().getY() + yOffset, zvp, seatas.getLocation().getYaw(), fbvp.getPitch());
        EntityArmorStand stand = ((CraftArmorStand) seatas).getHandle();
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            try {
                stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), seatas.getLocation().getYaw() + 15, seatas.getLocation().getPitch());
            } catch (Exception e) {
                // e.printStackTrace();
            }
        });
    }
}