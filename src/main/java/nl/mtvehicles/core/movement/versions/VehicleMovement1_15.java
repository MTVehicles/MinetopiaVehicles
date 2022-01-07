package nl.mtvehicles.core.movement.versions;

import net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.movement.VehicleMovement;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;

public class VehicleMovement1_15 extends VehicleMovement {

    @Override
    protected void teleportSeat(ArmorStand seat, Location loc){
        teleportSeat(((CraftEntity) seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    @Override
    protected void isObjectPacket(Object object) throws IllegalArgumentException {
        if (!(object instanceof PacketPlayInSteerVehicle)) throw new IllegalArgumentException();
    }
}
