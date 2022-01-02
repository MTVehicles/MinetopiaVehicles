package nl.mtvehicles.core.movement;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.world.entity.Entity;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.DriveUp;
import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.*;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class VehicleMovement1_17 {
    public static void vehicleMovement(Player p, PacketPlayInSteerVehicle ppisv) {
        long lastUsed = 0L;
        if (p.getVehicle() == null) return;

        if (!p.getVehicle().getType().toString().contains("ARMOR_STAND")) return;

        if (p.getVehicle().getCustomName() == null) return;

        if (p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "") == null) return;

        String license = p.getVehicle().getCustomName().replace("MTVEHICLES_MAINSEAT_", "");

        if (VehicleData.autostand.get("MTVEHICLES_MAIN_" + license) == null) return;

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
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            standSkin.teleport(new Location(standMain.getLocation().getWorld(), standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standSkin.getLocation().getYaw(), standSkin.getLocation().getPitch()));
        });
        int RotationSpeed = VehicleData.RotationSpeed.get(license);
        double MaxSpeed = VehicleData.MaxSpeed.get(license);
        double AccelerationSpeed = VehicleData.AccelerationSpeed.get(license);
        double BrakingSpeed = VehicleData.BrakingSpeed.get(license);
        double MaxSpeedBackwards = VehicleData.MaxSpeedBackwards.get(license);
        double FrictionSpeed = VehicleData.FrictionSpeed.get(license);

        boolean isMovingUpwards = slabCheck(standMain, license);
        updateStand(standMain, license, steerIsJumping(ppisv), isMovingUpwards);
        mainSeat(standMain, standMainSeat, license);

        if (VehicleData.seatsize.get(license + "addon") != null) {
            for (int i = 1; i <= VehicleData.seatsize.get(license + "addon"); i++) {
                ArmorStand standAddon = VehicleData.autostand.get("MTVEHICLES_ADDON" + i + "_" + license);
                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    standAddon.teleport(standMain.getLocation());
                });
            }
        }
        if (VehicleData.type.get(license) != null) {
            if (VehicleData.type.get(license).contains("HELICOPTER")) {
                rotors(standMain, standRotors, license);
            }
            if (VehicleData.type.get(license).contains("TANK")) {
                if (steerIsJumping(ppisv)) {
                    if (VehicleData.lastUsage.containsKey(p.getName())) {
                        lastUsed = ((Long) VehicleData.lastUsage.get(p.getName())).longValue();
                    }
                    if (System.currentTimeMillis() - lastUsed >= ConfigModule.defaultConfig.getConfig().getInt("hornCooldown") * 1000) {
                        standMain.getWorld().playEffect(standMain.getLocation(), Effect.BLAZE_SHOOT, 1, 1);
                        standMain.getWorld().playEffect(standMain.getLocation(), Effect.GHAST_SHOOT, 1, 1);
                        standMain.getWorld().playEffect(standMain.getLocation(), Effect.WITHER_BREAK_BLOCK, 1, 1);
                        double xOffset = 4;
                        double yOffset = 1.6;
                        double zOffset = 0;
                        Location locvp = standMain.getLocation().clone();
                        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
                        float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
                        float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
                        Location loc = new Location(standMain.getWorld(), xvp, standMain.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
                        standMain.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 2);
                        standMain.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 2);
                        standMain.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 5);
                        standMain.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 5);
                        VehicleData.lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
                    }
                }
            }
            if (!VehicleData.type.get(license).contains("HELICOPTER")) {
                if (!VehicleData.type.get(license).contains("TANK")) {
                    if (steerIsJumping(ppisv)) {
                        if (VehicleData.lastUsage.containsKey(p.getName())) {
                            lastUsed = ((Long) VehicleData.lastUsage.get(p.getName())).longValue();
                        }
                        if (System.currentTimeMillis() - lastUsed >= ConfigModule.defaultConfig.getConfig().getInt("hornCooldown") * 1000) {
                            standMain.getWorld().playSound(standMain.getLocation(), ConfigModule.defaultConfig.getConfig().getString("hornType"), 0.9f, 1f);
                            VehicleData.lastUsage.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
                        }
                    }
                }
            }
        }
        if (steerGetXxa(ppisv) > 0.0) {
            Bukkit.getScheduler().runTask(Main.instance, () -> {
                standMain.teleport(new Location(standMain.getLocation().getWorld(), standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() - RotationSpeed, standMain.getLocation().getPitch()));
                standMainSeat.teleport(new Location(standMain.getLocation().getWorld(), standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() - RotationSpeed, standMain.getLocation().getPitch()));
                standSkin.teleport(new Location(standSkin.getLocation().getWorld(), standSkin.getLocation().getX(), standSkin.getLocation().getY(), standSkin.getLocation().getZ(), standSkin.getLocation().getYaw() - RotationSpeed, standSkin.getLocation().getPitch()));
            });
        } else if (steerGetXxa(ppisv) < 0.0) {
            Bukkit.getScheduler().runTask(Main.instance, () -> {
                standSkin.teleport(new Location(standSkin.getLocation().getWorld(), standSkin.getLocation().getX(), standSkin.getLocation().getY(), standSkin.getLocation().getZ(), standSkin.getLocation().getYaw() + RotationSpeed, standSkin.getLocation().getPitch()));
                standMainSeat.teleport(new Location(standMain.getLocation().getWorld(), standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() + RotationSpeed, standMain.getLocation().getPitch()));
                standMain.teleport(new Location(standMain.getLocation().getWorld(), standMain.getLocation().getX(), standMain.getLocation().getY(), standMain.getLocation().getZ(), standMain.getLocation().getYaw() + RotationSpeed, standMain.getLocation().getPitch()));
            });
        }
        if (steerGetZza(ppisv) > 0.0) {
            if (VehicleData.speed.get(license) < 0) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) + BrakingSpeed);
                return;
            }
            if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled")) {
                double fuelMultiplier = ConfigModule.defaultConfig.getConfig().getDouble("fuelMultiplier");
                if (fuelMultiplier < 0.1 || fuelMultiplier > 10) fuelMultiplier = 1; //Must be between 0.1 and 10. Default: 1
                double dnum = VehicleData.fuel.get(license) - (fuelMultiplier * VehicleData.fuelUsage.get(license));
                VehicleData.fuel.put(license, dnum);
            }
            if (VehicleData.speed.get(license) > MaxSpeed-AccelerationSpeed) {
                return;
            }
            VehicleData.speed.put(license, VehicleData.speed.get(license) + AccelerationSpeed);
        }
        if (steerGetZza(ppisv) < 0.0) {
            if (VehicleData.speed.get(license) > 0) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) - BrakingSpeed);
                return;
            }
            if (ConfigModule.defaultConfig.getConfig().getBoolean("benzine") && ConfigModule.vehicleDataConfig.getConfig().getBoolean("vehicle." + license + ".benzineEnabled")) {
                double fuelMultiplier = ConfigModule.defaultConfig.getConfig().getDouble("fuelMultiplier");
                if (fuelMultiplier < 0.1 || fuelMultiplier > 10) fuelMultiplier = 1; //Must be between 0.1 and 10. Default: 1
                double dnum = VehicleData.fuel.get(license) - (fuelMultiplier * VehicleData.fuelUsage.get(license));
                VehicleData.fuel.put(license, dnum);
            }
            if (VehicleData.speed.get(license) < -MaxSpeedBackwards) {
                return;
            }
            VehicleData.speed.put(license, VehicleData.speed.get(license) - AccelerationSpeed);
        }
        if (steerGetZza(ppisv) == 0.0) {
            BigDecimal round = BigDecimal.valueOf(VehicleData.speed.get(license)).setScale(1, BigDecimal.ROUND_DOWN);
            if (Double.parseDouble(String.valueOf(round)) == 0.0) {
                VehicleData.speed.put(license, 0.0);
                return;
            }
            if (Double.parseDouble(String.valueOf(round)) > 0.01) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) - FrictionSpeed);
                return;
            }
            if (Double.parseDouble(String.valueOf(round)) < 0.01) {
                VehicleData.speed.put(license, VehicleData.speed.get(license) + FrictionSpeed);
            }
        }
    }

    public static boolean slabCheck(ArmorStand mainStand, String license) { //Returns true if is moving upwards (in any way)
        Location loc = getLocationOfBlockAhead(mainStand);
        String locY = String.valueOf(mainStand.getLocation().getY());
        Location locBlockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ(), loc.getYaw(), loc.getPitch());

        final String drivingOnY = locY.substring(locY.length() - 2);
        boolean isOnGround = drivingOnY.contains(".0");
        boolean isOnSlab = drivingOnY.contains(".5");
        boolean isPassable = loc.getBlock().isPassable();
        boolean isAbovePassable = locBlockAbove.getBlock().isPassable();

        double difference = Double.parseDouble("0." + locY.split("\\.")[1]);
        BlockData blockData = loc.getBlock().getBlockData();

        if (loc.getBlock().getType().toString().contains("CARPET")){
            if (!ConfigModule.defaultConfig.getConfig().getBoolean("driveOnCarpets")){ //if carpets are turned off in config
                VehicleData.speed.put(license, 0.0);
                return false;
            }

            if (!isAbovePassable) {
                VehicleData.speed.put(license, 0.0);
                return false;
            }

            if (isOnGround) pushVehicleUp(mainStand, 0.0625);
            return true;
        }

        if (blockData instanceof Snow){
            //Reserved for future update concerning Snow. Just stop for now.
            //Does not include snow block - that's considered a full block.
            VehicleData.speed.put(license, 0.0);
            return false;
        }

        if (blockData instanceof Fence || blockData instanceof Wall || blockData instanceof TrapDoor){
            VehicleData.speed.put(license, 0.0);
            return false;
        }

        if (ConfigModule.defaultConfig.driveUpSlabs().equals(DriveUp.SLABS)){
            if (isOnSlab) {
                if (isPassable) return false; //Vehicle will go down

                if (blockData instanceof Slab) {
                    Slab slab = (Slab) blockData;
                    if (slab.getType().toString().equals("BOTTOM")) {
                        return false; //Vehicle will continue on the slabs
                    }
                }

                if (!isAbovePassable) {
                    VehicleData.speed.put(license, 0.0);
                    return false; //Vehicle won't continue if there's a barrier above
                }


                pushVehicleUp(mainStand, 0.5); //Vehicle will go up if there's a full block or a top/double slab
                return true;
            }

            if (!isPassable) {
                if (blockData instanceof Slab) {
                    Slab slab = (Slab) blockData;
                    if (slab.getType().toString().equals("BOTTOM")) {

                        if (!isAbovePassable) {
                            VehicleData.speed.put(license, 0.0);
                            return false; //Vehicle won't go up the slab if there's a barrier above
                        }

                        if (isOnGround) {
                            pushVehicleUp(mainStand, 0.5);
                        } else { //Maybe they're on a carpet
                            if ((0.5 - difference) > 0) pushVehicleUp(mainStand, 0.5 - difference);
                        }
                    }
                }

                VehicleData.speed.put(license, 0.0);
                return false; //If you're on the ground and there isn't bottom slab or a passable block, stop
            }

        } else if (ConfigModule.defaultConfig.driveUpSlabs().equals(DriveUp.BLOCKS)) {

            if (!isOnSlab) {
                if (!isPassable) {
                    if (blockData instanceof Slab){
                        Slab slab = (Slab) blockData;
                        if (slab.getType().toString().equals("BOTTOM")){
                            VehicleData.speed.put(license, 0.0);
                            return false; //If it's a bottom slab, stop.
                        }
                    }

                    if (!isAbovePassable) { //if more than 1 block high
                        VehicleData.speed.put(license, 0.0);
                        return false;
                    }

                    if (isOnGround) {
                        pushVehicleUp(mainStand, 1);
                    } else { //Maybe they're on a carpet
                        if ((1 - difference) > 0) pushVehicleUp(mainStand, 1 - difference);
                    }

                    return true;
                }
            }
            //If it's on a slab (might have been placed there)
            if (isPassable) return false; //Vehicle will go down

            if (blockData instanceof Slab) {
                Slab slab = (Slab) blockData;
                if (slab.getType().toString().equals("BOTTOM")) {
                    return false; //Vehicle will continue on the slabs
                }
            }

            if (!isAbovePassable) {
                VehicleData.speed.put(license, 0.0);
                return false; //Vehicle won't continue if there's a barrier above
            }

            pushVehicleUp(mainStand, 0.5); //Vehicle will go up if there's a full block or a top/double slab
            return true;

        } else if (ConfigModule.defaultConfig.driveUpSlabs().equals(DriveUp.BOTH)) {

            if (isOnSlab) {
                if (isPassable) return false; //Vehicle will go down

                if (blockData instanceof Slab) {
                    Slab slab = (Slab) blockData;
                    if (slab.getType().toString().equals("BOTTOM")) {
                        return false; //Vehicle will continue on the slabs
                    }
                }

                if (!isAbovePassable) {
                    VehicleData.speed.put(license, 0.0);
                    return false; //Vehicle won't continue if there's a barrier above
                }

                pushVehicleUp(mainStand, 0.5); //Vehicle will go up if there's a full block or a top/double slab
                return true;
            }

            if (!isPassable) {

                if (!isAbovePassable) { //If more than 1 block high
                    VehicleData.speed.put(license, 0.0);
                    return false;
                }

                if (blockData instanceof Slab){
                    Slab slab = (Slab) blockData;
                    if (slab.getType().toString().equals("BOTTOM")){ //If it's a bottom slab
                        if (isOnGround) {
                            pushVehicleUp(mainStand, 0.5);
                        } else { //Maybe they're on a carpet
                            if ((0.5 - difference) > 0) pushVehicleUp(mainStand, 0.5 - difference);
                        }
                        return true;
                    }
                }

                //If it's another block or a top/double slab
                if (isOnGround) {
                    pushVehicleUp(mainStand, 1);
                } else { //Maybe they're on a carpet
                    if ((1 - difference) > 0) pushVehicleUp(mainStand, 1 - difference);
                }

                return true;
            }
        }

        return false;
    }

    public static void updateStand(ArmorStand mainStand, String license, boolean space, boolean isMovingUpwards) {
        Location loc = mainStand.getLocation();
        Location locBlockAhead = getLocationOfBlockAhead(mainStand);
        Location locBlockAheadAndBelow = new Location(locBlockAhead.getWorld(), locBlockAhead.getX(), locBlockAhead.getY() - 1, locBlockAhead.getZ(), locBlockAhead.getPitch(), locBlockAhead.getYaw());
        Location location = new Location(loc.getWorld(), loc.getX(), loc.getY() - 0.2, loc.getZ(), loc.getYaw(), loc.getPitch());

        if (VehicleData.type.get(license) == null) return;

        if (VehicleData.type.get(license).contains("HELICOPTER")) {
            if (!location.getBlock().getType().equals(Material.AIR)) {
                VehicleData.speed.put(license, 0.0);
            }
            if (space) {
                if (mainStand.getLocation().getY() > Main.instance.getConfig().getInt("helicopterMaxHeight")) {
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

        if (location.getBlock().getType().toString().contains("WATER")) {
            mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
            return;
        }

        if (!isMovingUpwards && locBlockAheadAndBelow.getBlock().isPassable()){
            if (location.getBlock().isPassable()){
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.8, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                return;
            }

            if (location.getBlock().getType().toString().contains("CARPET")){
                mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), -0.7375, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
                return;
            }
        }

        mainStand.setVelocity(new Vector(mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getX(), 0.0, mainStand.getLocation().getDirection().multiply(VehicleData.speed.get(license)).getZ()));
    }

    public static void mainSeat(ArmorStand mainStand, ArmorStand mainSeat, String license) {
        if (VehicleData.seatsize.get(license) != null) {
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
                Entity seat = ((CraftEntity) seatas).getHandle();
                Bukkit.getScheduler().runTask(Main.instance, () -> {
                    try {
                        Method method = seat.getClass().getSuperclass().getSuperclass().getDeclaredMethod("a", double.class, double.class, double.class, float.class, float.class); //teleporting an occupied vehicle
                        method.invoke(seat, loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
                    } catch (Exception e) {
                        e.printStackTrace();
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
        Entity seat = ((CraftEntity) mainSeat).getHandle();
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            try {
                Method method = seat.getClass().getSuperclass().getSuperclass().getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
                method.invoke(seat, loc.getX(), loc.getY(), loc.getZ(), fbvp.getYaw(), loc.getPitch());
            } catch (Exception e) {
                e.printStackTrace();
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
        final Location loc = new Location(main.getWorld(), xvp, main.getLocation().getY() + yOffset, zvp, seatas.getLocation().getYaw() + 15, seatas.getLocation().getPitch());
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            seatas.teleport(loc);
        });
    }
    private static void pushVehicleUp(ArmorStand mainStand, double plus){
        Location newLoc = new Location(mainStand.getLocation().getWorld(), mainStand.getLocation().getX(), mainStand.getLocation().getY() + plus, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            mainStand.teleport(newLoc);
        });
    }

    private static boolean steerIsJumping(PacketPlayInSteerVehicle packet){
        boolean isJumping;
        try {
            Method method = packet.getClass().getDeclaredMethod("d");
            isJumping = (Boolean) method.invoke(packet);
        } catch (Exception e) {
            isJumping = false;
            e.printStackTrace();
        }
        return isJumping;
    }

    private static float steerGetXxa(PacketPlayInSteerVehicle packet){
        float Xxa = 0;
        try {
            Method method = packet.getClass().getDeclaredMethod("b");
            Xxa = (float) method.invoke(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Xxa;
    }

    private static float steerGetZza(PacketPlayInSteerVehicle packet){
        float Zza = 0;
        try {
            Method method = packet.getClass().getDeclaredMethod("c");
            Zza = (float) method.invoke(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Zza;
    }

    private static Location getLocationOfBlockAhead(ArmorStand mainStand){
        double xOffset = 0.7;
        double yOffset = 0.4;
        double zOffset = 0.0;
        Location locvp = mainStand.getLocation().clone();
        Location fbvp = locvp.add(locvp.getDirection().setY(0).normalize().multiply(xOffset));
        float zvp = (float) (fbvp.getZ() + zOffset * Math.sin(Math.toRadians(fbvp.getYaw())));
        float xvp = (float) (fbvp.getX() + zOffset * Math.cos(Math.toRadians(fbvp.getYaw())));
        return new Location(mainStand.getWorld(), xvp, mainStand.getLocation().getY() + yOffset, zvp, fbvp.getYaw(), fbvp.getPitch());
    }
}
