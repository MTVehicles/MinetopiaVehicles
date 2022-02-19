package nl.mtvehicles.core.movement.versions;

import net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.movement.VehicleMovement;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

import static nl.mtvehicles.core.movement.PacketHandler.isObjectPacket;

public class VehicleMovement1_12 extends VehicleMovement {

    @Override
    protected boolean slabCheck(ArmorStand mainStand, String license) {
        final Location loc = getLocationOfBlockAhead(mainStand);
        final String locY = String.valueOf(mainStand.getLocation().getY());
        final Location locBlockAbove = new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ(), loc.getYaw(), loc.getPitch());
        final String drivingOnY = locY.substring(locY.length() - 2);

        final boolean isOnGround = drivingOnY.contains(".0");
        final boolean isOnSlab = drivingOnY.contains(".5");
        final boolean isPassable = isPassableCustom(loc.getBlock().getType());
        final boolean isAbovePassable = isPassableCustom(locBlockAbove.getBlock().getType());

        final double difference = Double.parseDouble("0." + locY.split("\\.")[1]);
        final int data = loc.getBlock().getData();

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

        if (loc.getBlock().getType().toString().contains("SNOW") && !loc.getBlock().getType().toString().contains("SNOW_BLOCK")){
            //Reserved for future update concerning Snow. Just stop for now.
            //Does not include snow block - that's considered a full block.
            VehicleData.speed.put(license, 0.0);
            return false;
        }

        if (loc.getBlock().getType().toString().contains("FENCE") || loc.getBlock().getType().toString().contains("WALL") || loc.getBlock().getType().toString().contains("TRAPDOOR") || loc.getBlock().getType().toString().contains("TRAP_DOOR")){
            VehicleData.speed.put(license, 0.0);
            return false;
        }

        if (ConfigModule.defaultConfig.driveUpSlabs().isSlabs()){
            if (isOnSlab) {
                if (isPassable) return false; //Vehicle will go down

                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
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
                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:

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
                        pushVehicleUp(mainStand, 1);
                    } else { //Maybe they're on a carpet
                        if ((1 - difference) > 0) pushVehicleUp(mainStand, 1 - difference);
                    }

                    return true;
                }
            }
            //If it's on a slab (might have been placed there)
            if (isPassable) return false; //Vehicle will go down

            if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
                    return false; //Vehicle will continue on the slabs
                }
            }

            if (!isAbovePassable) {
                VehicleData.speed.put(license, 0.0);
                return false; //Vehicle won't continue if there's a barrier above
            }

            pushVehicleUp(mainStand, 0.5); //Vehicle will go up if there's a full block or a top/double slab
            return true;

        } else if (ConfigModule.defaultConfig.driveUpSlabs().isBoth()) {

            if (isOnSlab) {
                if (isPassable) return false; //Vehicle will go down

                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
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

                if (loc.getBlock().getType().toString().contains("STEP") || loc.getBlock().getType().toString().contains("SLAB")) {
                    if (!loc.getBlock().getType().toString().contains("DOUBLE") && data < 9) { //If it's a bottom slab, then:
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

    @Override
    protected void updateStand(ArmorStand mainStand, String license, boolean space) {
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
            if (location.getBlock().getType().equals(Material.AIR)) {
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

        if (isPassableCustom(locBlockAheadAndBelow.getBlock().getType())){
            if (isPassableCustom(location.getBlock().getType())){
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

    private boolean isPassableCustom(Material block){
        return block.toString().contains("AIR") || block.toString().contains("FLOWER") || block.toString().contains("ROSE") || block.toString().contains("PLANT") || block.equals(Material.BROWN_MUSHROOM) || block.equals(Material.RED_MUSHROOM) || block.toString().contains("LONG_GRASS") || block.toString().contains("SAPLING") || block.toString().contains("DEAD_BUSH") || block.toString().contains("TORCH") || block.toString().contains("BANNER");
    }

    @Override
    protected boolean steerIsJumping(Object packet){
        if (!isObjectPacket(packet)) return false;

        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet;
        return ppisv.c();
    }

    @Override
    protected float steerGetXxa(Object packet){
        if (!isObjectPacket(packet)) return 0;

        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet;
        return ppisv.a();
    }

    @Override
    protected float steerGetZza(Object packet){
        if (!isObjectPacket(packet)) return 0;

        PacketPlayInSteerVehicle ppisv = (PacketPlayInSteerVehicle) packet;
        return ppisv.b();
    }

}