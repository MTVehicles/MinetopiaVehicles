package nl.mtvehicles.core.movement.versions;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.movement.VehicleMovement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Class concerning the movement of vehicles in 1_12_R2 (because the NMS differ a lot)
 */
public class VehicleMovement1_12 extends VehicleMovement {

    @Override
    protected boolean blockCheck() {
        final Location loc = getLocationOfBlockAhead();
        final String locY = String.valueOf(standMain.getLocation().getY());
        final Location locBlockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ(), loc.getYaw(), loc.getPitch());
        final Location locBlockBelow = new Location(loc.getWorld(), loc.getX(), loc.getY() - 1, loc.getZ(), loc.getYaw(), loc.getPitch());
        final String drivingOnY = locY.substring(locY.length() - 2);

        final boolean isOnGround = drivingOnY.contains(".0");
        final boolean isOnSlab = drivingOnY.contains(".5");
        final boolean isPassable = isPassable(loc.getBlock());
        final boolean isAbovePassable = isPassable(locBlockAbove.getBlock());

        final double difference = Double.parseDouble("0." + locY.split("\\.")[1]);
        final int data = loc.getBlock().getData();
        final int dataBelow = locBlockBelow.getBlock().getData();

        if (loc.getBlock().getType().toString().contains("CARPET")){
            if (!(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.DRIVE_ON_CARPETS)){ //if carpets are turned off in config
                VehicleData.speed.put(license, 0.0);
                return false;
            }

            if (!isAbovePassable) {
                VehicleData.speed.put(license, 0.0);
                return false;
            }

            if (isOnGround) pushVehicleUp(0.0625);
            return true;
        }

        if (loc.getBlock().getType().toString().contains("SNOW") && !loc.getBlock().getType().toString().contains("SNOW_BLOCK")){ //SNOW! - Does not include snow block - that's considered a full block.
            final int layers = data + 1;
            double layerHeight = getLayerHeight(layers);
            if (VehicleData.speed.get(license) > 0.1) VehicleData.speed.put(license, 0.1);

            if (layerHeight == difference) return false; //Vehicle will continue

            final double snowDifference = layerHeight - difference;
            pushVehicleUp(snowDifference); //Will push either up or down, depending on the difference

            return false;
        }

        if (loc.getBlock().getType().toString().contains("FENCE") || loc.getBlock().getType().toString().contains("WALL") || loc.getBlock().getType().toString().contains("TRAPDOOR") || loc.getBlock().getType().toString().contains("TRAP_DOOR")){
            VehicleData.speed.put(license, 0.0);
            return false;
        }

        if (ConfigModule.defaultConfig.driveUpSlabs().isSlabs()){
            if (isOnSlab) {
                if (isPassable) {
                    pushVehicleDown(0.5);
                    return false; //Vehicle will go down
                }

                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
                        return false; //Vehicle will continue on the slabs
                    }
                }

                if (!isAbovePassable) {
                    VehicleData.speed.put(license, 0.0);
                    return false; //Vehicle won't continue if there's a barrier above
                }


                pushVehicleUp(0.5); //Vehicle will go up if there's a full block or a top/double slab
                return true;
            }

            if (!isPassable) {
                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:

                        if (!isAbovePassable) {
                            VehicleData.speed.put(license, 0.0);
                            return false; //Vehicle won't go up the slab if there's a barrier above
                        }

                        if (isOnGround) {
                            pushVehicleUp(0.5);
                        } else { //Maybe they're on a carpet
                            if ((0.5 - difference) > 0) pushVehicleUp(0.5 - difference);
                        }
                    }
                }

                VehicleData.speed.put(license, 0.0);
                return false; //If you're on the ground and there isn't bottom slab or a passable block, stop
            }
        } else if (ConfigModule.defaultConfig.driveUpSlabs().isBlocks()) {

            if (!isOnSlab) {
                if (!isPassable) {
                    if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                        if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
                            VehicleData.speed.put(license, 0.0);
                            return false; //If it's a bottom slab, stop.
                        }
                    }

                    if (!isAbovePassable) { //if more than 1 block high
                        VehicleData.speed.put(license, 0.0);
                        return false;
                    }

                    if (isOnGround) {
                        pushVehicleUp(1);
                    } else { //Maybe they're on a carpet
                        if ((1 - difference) > 0) pushVehicleUp(1 - difference);
                    }

                    return true;
                }
            }

            //If it's on a slab (might have been placed there)
            if (isPassable) {
                pushVehicleDown(0.5);
                return false; //Vehicle will go down
            }

            if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
                    return false; //Vehicle will continue on the slabs
                }
            }

            if (!isAbovePassable) {
                VehicleData.speed.put(license, 0.0);
                return false; //Vehicle won't continue if there's a barrier above
            }

            pushVehicleUp(0.5); //Vehicle will go up if there's a full block or a top/double slab
            return true;

        } else if (ConfigModule.defaultConfig.driveUpSlabs().isBoth()) {

            if (isOnSlab) {
                if (isPassable) {
                    pushVehicleDown(0.5);
                    return false; //Vehicle will go down
                }

                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
                        return false; //Vehicle will continue on the slabs
                    }
                }

                if (!isAbovePassable) {
                    VehicleData.speed.put(license, 0.0);
                    return false; //Vehicle won't continue if there's a barrier above
                }

                pushVehicleUp(0.5); //Vehicle will go up if there's a full block or a top/double slab
                return true;
            }

            if (!isPassable) {

                if (!isAbovePassable) { //If more than 1 block high
                    VehicleData.speed.put(license, 0.0);
                    return false;
                }

                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
                        if (isOnGround) {
                            pushVehicleUp(0.5);
                        } else { //Maybe they're on a carpet
                            if ((0.5 - difference) > 0) pushVehicleUp(0.5 - difference);
                        }
                        return true;
                    }
                }

                //If it's another block or a top/double slab
                if (isOnGround) {
                    pushVehicleUp(1);
                } else { //Maybe they're on a carpet
                    if ((1 - difference) > 0) pushVehicleUp(1 - difference);
                }

                return true;

            }

            if (locBlockBelow.getBlock().getType().toString().contains("STEP") || locBlockBelow.getBlock().getType().toString().contains("SLAB")) {
                if (!locBlockBelow.getBlock().getType().toString().contains("DOUBLE") && dataBelow < 9) { //If on a block, in front of it is air and below is bottom slab:
                    pushVehicleDown(0.5);
                    return false; //Vehicle will go down
                }
            }
        }

        return false;
    }

    @Override
    protected boolean isPassable(Block block){
        String blockName = block.getType().toString();
        return blockName.contains("AIR")
                || blockName.contains("FLOWER")
                || blockName.contains("ROSE")
                || blockName.contains("PLANT")
                || block.getType().equals(Material.BROWN_MUSHROOM)
                || block.getType().equals(Material.RED_MUSHROOM)
                || blockName.contains("LONG_GRASS")
                || blockName.contains("SAPLING")
                || blockName.contains("DEAD_BUSH")
                || blockName.contains("TORCH")
                || blockName.contains("BANNER");
    }

    @Override
    protected void rotateVehicle(float yaw){
        final Location loc = standMain.getLocation();

        standMain.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
        standMainSeat.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
        standSkin.teleport(new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), yaw, loc.getPitch()));
    }

    @Override
    protected boolean steerIsJumping(){
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet;
        return ppisv.c();
    }

    @Override
    protected float steerGetXxa(){
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet;
        return ppisv.a();
    }

    @Override
    protected float steerGetZza(){
        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet;
        return ppisv.b();
    }

}
