package nl.mtvehicles.core.movement.versions;

import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.movement.VehicleMovement;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;

public class VehicleMovement1_18 extends VehicleMovement {

    @Override
    protected void teleportSeat(ArmorStand seat, Location loc){
        teleportSeat(((CraftEntity) seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    @Override
    protected String getTeleportMethod(){
        return "a";
    }

    @Override
    protected void isObjectPacket(Object object) throws IllegalArgumentException {
        if (!(object instanceof PacketPlayInSteerVehicle)) throw new IllegalArgumentException();
    }
}
