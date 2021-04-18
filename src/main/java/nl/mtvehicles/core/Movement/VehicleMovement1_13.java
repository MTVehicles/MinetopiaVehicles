package nl.mtvehicles.core.Movement;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import net.minecraft.server.v1_13_R2.EntityArmorStand;
import net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.Infrastructure.Helpers.BossbarUtils;
import nl.mtvehicles.core.Infrastructure.Helpers.VehicleData;
import nl.mtvehicles.core.Infrastructure.Models.Vehicle;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.Utils.RunnableUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.math.BigDecimal;

public class VehicleMovement1_13 extends PacketAdapter {
    float yaw;
    int w;

    public VehicleMovement1_13() {
        super(Main.instance, ListenerPriority.HIGHEST, PacketType.Play.Client.STEER_VEHICLE);
        this.yaw = 0.0f;
        this.w = 0;
    }


    public void onPacketReceiving(final PacketEvent event) {
        RunnableUtils.catchAsync(() -> {
            PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) event.getPacket().getHandle();
            final Player p = event.getPlayer();

            if (p.getVehicle() == null) return;
            if (p.getVehicle().getCustomName() == null) return;
            String license = p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");
            if (license.isEmpty()) return; // vehicle doesn't have license.

            if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null)
                return;
            if (VehicleData.speed.get(license) == null) {
                VehicleData.speed.put(license, 0.0);
                return;
            }
            if (VehicleData.benzine.get(license) < 1) {
                BossbarUtils.setbossbarvalue(0 / 100.0D, license);
                return;
            }
            BossbarUtils.setbossbarvalue(VehicleData.benzine.get(license) / 100.0D, license);
            ArmorStand standMain = VehicleData.autostand.get("MTVEHICLES_MAIN_" + license);
            ArmorStand standSkin = VehicleData.autostand.get("MTVEHICLES_SKIN_" + license);
            ArmorStand standMainSeat = VehicleData.autostand.get("MTVEHICLES_MAINSEAT_" + license);
            ArmorStand standRotors = VehicleData.autostand.get("MTVEHICLES_WIEKENS_" + license);
            ((CraftArmorStand) standSkin).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw(), standMain.getLocation().getPitch());
            mainSeat(standMain, (CraftArmorStand) standMainSeat, license);
            updateStand(standMain, license, ppisv.d());
            slabCheck(standMain, license);
            if (VehicleData.type.get(license).contains("HELICOPTER")) {
                rotors(standMain, standRotors, license);
            }
            if (ppisv.c() > 0.0) {
                ((CraftArmorStand) standMain).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() - Vehicle.getByPlate(license).getRotateSpeed(), standMain.getLocation().getPitch());
            } else if (ppisv.c() < 0.0) {
                ((CraftArmorStand) standMain).getHandle().setLocation(standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() + Vehicle.getByPlate(license).getRotateSpeed(), standMain.getLocation().getPitch());
            }
            if (ppisv.b() > 0.0) {
                if (VehicleData.speed.get(license) < 0) {
                    VehicleData.speed.put(license, VehicleData.speed.get(license) + Vehicle.getByPlate(license).getBrakingSpeed());
                    return;
                }
                if (Main.defaultConfig.getConfig().getBoolean("benzine") && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled")) {
                    double dnum = VehicleData.benzine.get(license) - VehicleData.benzineverbruik.get(license);
                    VehicleData.benzine.put(license, dnum);
                }
                if (VehicleData.speed.get(license) > Vehicle.getByPlate(license).getMaxSpeed()) {
                    return;
                }
                VehicleData.speed.put(license, VehicleData.speed.get(license) + Vehicle.getByPlate(license).getAcceleratieSpeed());
            }
            if (ppisv.b() < 0.0) {
                if (VehicleData.speed.get(license) > 0) {
                    VehicleData.speed.put(license, VehicleData.speed.get(license) - Vehicle.getByPlate(license).getBrakingSpeed());
                    return;
                }
                if (Main.defaultConfig.getConfig().getBoolean("benzine") && Main.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled")) {
                    double dnum = VehicleData.benzine.get(license) - VehicleData.benzineverbruik.get(license);
                    VehicleData.benzine.put(license, dnum);
                }
                if (VehicleData.speed.get(license) < -Vehicle.getByPlate(license).getMaxSpeedBackwards()) {
                    return;
                }
                VehicleData.speed.put(license, VehicleData.speed.get(license) - Vehicle.getByPlate(license).getAcceleratieSpeed());

            }
            if (ppisv.b() == 0.0) {
                BigDecimal round = BigDecimal.valueOf(VehicleData.speed.get(license)).setScale(1, BigDecimal.ROUND_DOWN);
                if (round.doubleValue() == 0.0) {
                    VehicleData.speed.put(license, 0.0);
                } else if (round.doubleValue() > 0.01) {
                    VehicleData.speed.put(license, VehicleData.speed.get(license) - Vehicle.getByPlate(license).getAftrekkenSpeed());
                } else if (round.doubleValue() < 0.01) {
                    VehicleData.speed.put(license, VehicleData.speed.get(license) + Vehicle.getByPlate(license).getAftrekkenSpeed());
                }
            }
        });
    }

    public static void slabCheck(ArmorStand mainStand, String license) {
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            double xOffset = 0.7;
            double yOffset = 0.4;
            double zOffset = 0.0;
            Location locvp = mainStand.getLocation().clone();
            Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
            float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
            float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
            Location loc = new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
            int data = loc.getBlock().getData();
            System.out.println(loc.getBlock().getType().toString() + " " + data);
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
                ((CraftArmorStand) mainStand).getHandle().setLocation(mainStand.getLocation().getX(), mainStand.getLocation().getY() + 0.5, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
                return;
            }
            if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                if (loc.getBlock().getType().toString().contains("DOUBLE")) {
                    return;
                }
                if (data == 0 || data == 5) {
                    ((CraftArmorStand) mainStand).getHandle().setLocation(mainStand.getLocation().getX(), mainStand.getLocation().getY() + 0.5, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
                }
            }
        });
    }

    public static void updateStand(ArmorStand mainStand, String license, Boolean space) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
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
                if (location.getBlock().getType().equals(Material.AIR)) {
                    mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                    return;
                }
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), 0.00001, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                return;
            }
            if (location.getBlock().getType().equals(Material.AIR) || location.getBlock().getType().toString().contains("WATER")) {
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                return;
            }
            mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), 0.0, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
        });
    }

    public static void mainSeat(ArmorStand mainStand, CraftArmorStand mainseat, String license) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
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
                    stand.setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
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
            mainseat.getHandle().setLocation(loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());

        });
    }

    public static void rotors(ArmorStand main, ArmorStand seatas, String license) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            double xOffset = VehicleData.wiekenx.get("MTVEHICLES_WIEKENS_" + license);
            double yOffset = VehicleData.wiekeny.get("MTVEHICLES_WIEKENS_" + license);
            double zOffset = VehicleData.wiekenz.get("MTVEHICLES_WIEKENS_" + license);
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