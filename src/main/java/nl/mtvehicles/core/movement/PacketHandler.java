package nl.mtvehicles.core.movement;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import org.bukkit.entity.Player;
import java.util.NoSuchElementException;

public class PacketHandler {

    public static void movement_1_18(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_18.vehicleMovement(player, ppisv);
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

    public static void movement_1_17(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) {
                    net.minecraft.network.protocol.game.PacketPlayInSteerVehicle ppisv = (net.minecraft.network.protocol.game.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_17.vehicleMovement(player, ppisv);
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

    public static void movement_1_16(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_16.vehicleMovement(player, ppisv);
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

    public static void movement_1_15(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_15.vehicleMovement(player, ppisv);
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

    public static void movement_1_13(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_13.vehicleMovement(player, ppisv);
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

    public static void movement_1_12(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {
            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                super.channelRead(channelHandlerContext, packet);
                if (packet instanceof net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle) {
                    net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle ppisv = (net.minecraft.server.v1_12_R1.PacketPlayInSteerVehicle) packet;
                    VehicleMovement1_12.vehicleMovement(player, ppisv);
                }
            }
        };
        ChannelPipeline pipeline = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        try {
            pipeline.remove(player.getName());
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
            System.out.println(e);
        }
        try {
            pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (NoSuchElementException e) { //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
            System.out.println(e);
        }
    }
}
