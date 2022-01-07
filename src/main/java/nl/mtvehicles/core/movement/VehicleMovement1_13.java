package nl.mtvehicles.core.movement;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.BossBarUtils;
import nl.mtvehicles.core.infrastructure.helpers.VehicleData;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class VehicleMovement1_13 extends VehicleMovement {

    @Override
    protected void teleportSeat(ArmorStand seat, Location loc){
        teleportSeat(((CraftEntity) seat).getHandle(), loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    @Override
    protected void isObjectPacket(Object object) throws IllegalArgumentException {
        if (!(object instanceof PacketPlayInSteerVehicle)) throw new IllegalArgumentException();
    }

    @Override
    protected void spawnParticles(ArmorStand stand, Location loc){
        stand.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 2);
        stand.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 2);
        stand.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 5);
    }
}
