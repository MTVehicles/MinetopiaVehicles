package nl.mtvehicles.core.movement;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.DriveUp;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Snow;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

public abstract class VehicleMovement {

    protected boolean slabCheck(ArmorStand mainStand, String license) { //Returns true if is moving upwards (in any way)
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

        if (blockData instanceof Fence || loc.getBlock().getType().toString().contains("WALL") || blockData instanceof TrapDoor){
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

    protected void mainSeat(ArmorStand mainStand, ArmorStand mainSeat, String license) {
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
                teleportSeat(seatas, loc);
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
        teleportSeat(mainSeat, loc);
    }

    protected abstract void teleportSeat(ArmorStand seat, Location loc);

    protected void updateStand(ArmorStand mainStand, String license, boolean space, boolean isMovingUpwards) {
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

    protected void rotors(ArmorStand main, ArmorStand seatas, String license) {
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

    protected void pushVehicleUp(ArmorStand mainStand, double plus){
        Location newLoc = new Location(mainStand.getLocation().getWorld(), mainStand.getLocation().getX(), mainStand.getLocation().getY() + plus, mainStand.getLocation().getZ(), mainStand.getLocation().getYaw(), mainStand.getLocation().getPitch());
        Bukkit.getScheduler().runTask(Main.instance, () -> {
            mainStand.teleport(newLoc);
        });
    }

    protected Location getLocationOfBlockAhead(ArmorStand mainStand){
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

