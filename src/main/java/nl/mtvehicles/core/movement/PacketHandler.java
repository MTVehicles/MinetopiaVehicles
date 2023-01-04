package nl.mtvehicles.core.movement;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import nl.mtvehicles.core.movement.versions.VehicleMovement1_12;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import static nl.mtvehicles.core.infrastructure.modules.VersionModule.getServerVersion;

/**
 * Packet handling system in different minecraft versions.
 */
@VersionSpecific
public class PacketHandler {

    /**
     * Packet handler for vehicle steering in 1.19.3
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_19_R2(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer) player).getHandle();

            Field pc = entityPlayer.getClass().getField("b");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) pc.get(entityPlayer);

            Field nm = playerConnection.getClass().getField("b");
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) nm.get(playerConnection);

            Field c = networkManager.getClass().getField("m");
            Channel channel = (Channel) c.get(networkManager);

            pipeline = channel.pipeline();
        } catch (Exception e){
            e.printStackTrace();
            Main.disablePlugin();
            return;
        }
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) {
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Packet handler for vehicle steering in 1.19
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_19(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer) player).getHandle();

            Field pc = entityPlayer.getClass().getField("b");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) pc.get(entityPlayer);

            Field nm = playerConnection.getClass().getField("b");
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) nm.get(playerConnection);

            Field c = networkManager.getClass().getField("m");
            Channel channel = (Channel) c.get(networkManager);

            pipeline = channel.pipeline();
        } catch (Exception e){
            e.printStackTrace();
            Main.disablePlugin();
            return;
        }
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) {
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Packet handler for vehicle steering in 1.18.2
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_18_R2(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline;
        try {
            Object networkManager = ((org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer) player).getHandle().b.a;
            Field m = networkManager.getClass().getField("m");
            Channel channel = (Channel) m.get(networkManager);
            pipeline = channel.pipeline();
        } catch (Exception e){
            e.printStackTrace();
            Main.disablePlugin();
            return;
        }
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) {
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Packet handler for vehicle steering in 1.18 and 1.18.1
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_18_R1(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer) player).getHandle().b.a.k.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) {
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Packet handler for vehicle steering in 1.17 and 1.17.1
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_17(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer) player).getHandle().b.a.k.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) {
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Packet handler for vehicle steering in 1.16.5 and 1.16.4 (NMS versions 1_16_R2 and 1_16_R1 are not supported)
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_16(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
    }

    /**
     * Packet handler for vehicle steering in versions 1.15-1.15.2
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_15(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
    }

    /**
     * Packet handler for vehicle steering in 1.13.2 and 1.13.1 (NMS version 1_13_R1 is not supported)
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_13(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle) packet;
                    VehicleMovement movement = new VehicleMovement();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
    }

    /**
     * Packet handler for vehicle steering in versions 1.12-1.12.2
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_12(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_12 movement = new VehicleMovement1_12();
                    movement.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
    }

    /**
     * Check whether a given object is a valid packet. If not, return false and send an error to the console.
     *
     * @param object Checked object (likely a packet)
     * @return True if the given object is an instance of the steering packet (PacketPlayInSteerVehicle).
     */
    @VersionSpecific
    public static boolean isObjectPacket(Object object) {
        final String errorMessage = "An unexpected error occurred. Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle";

        if (getServerVersion().is1_12()) {
            if (!(object instanceof net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle)) {
                Main.logSevere(errorMessage);
                return false;
            }
        } else if (getServerVersion().is1_13()) {
            if (!(object instanceof net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle)){
                Main.logSevere(errorMessage);
                return false;
            }
        } else if (getServerVersion().is1_15()) {
            if (!(object instanceof net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle)){
                Main.logSevere(errorMessage);
                return false;
            }
        } else if (getServerVersion().is1_16()) {
            if (!(object instanceof net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle)){
                Main.logSevere(errorMessage);
                return false;
            }
        } else if (getServerVersion().isNewerOrEqualTo(ServerVersion.v1_17)) {
            if (!(object instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle)){
                Main.logSevere(errorMessage);
                return false;
            }
        }
        return true;
    }

}
