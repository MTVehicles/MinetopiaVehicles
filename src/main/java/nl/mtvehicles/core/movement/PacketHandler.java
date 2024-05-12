package nl.mtvehicles.core.movement;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.network.PlayerConnection;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
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
     * Packet handler for vehicle steering in 1.20.5 and 1.20.6
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_20_R4(Player player) {
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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R4.entity.CraftPlayer) player).getHandle();

            Field playerConnectionField = entityPlayer.getClass().getField("c");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = net.minecraft.server.network.ServerCommonPacketListenerImpl.class.getDeclaredField("e");
            networkManagerField.setAccessible(true);
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("n");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_20_R4(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
        }
    }

    /**
     * Packet handler for vehicle steering in 1.20.3 and 1.20.4
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_20_R3(Player player) {
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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer) player).getHandle();

            Field playerConnectionField = entityPlayer.getClass().getField("c");
            playerConnectionField.setAccessible(true);
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = net.minecraft.server.network.ServerCommonPacketListenerImpl.class.getDeclaredField("c");
            networkManagerField.setAccessible(true);
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("n");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_20_R3(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
        }
    }

    /**
     * Packet handler for vehicle steering in 1.20.2
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_20_R2(Player player) {
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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer) player).getHandle();

            Field playerConnectionField = entityPlayer.getClass().getField("c");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = net.minecraft.server.network.ServerCommonPacketListenerImpl.class.getDeclaredField("c");
            networkManagerField.setAccessible(true);
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("n");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_20_R2(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
        }
    }


    /**
     * Packet handler for vehicle steering in 1.20
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_20_R1(Player player) {
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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer) player).getHandle();

            Field playerConnectionField = entityPlayer.getClass().getField("c");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = playerConnection.getClass().getDeclaredField("h");
            networkManagerField.setAccessible(true);
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("m");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_20_R1(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
        }
    }

    /**
     * Packet handler for vehicle steering in 1.19.4
     * @param player Player whose steering is being regarded
     */
    public static void movement_1_19_R3(Player player) {
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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer) player).getHandle();
            Field playerConnectionField = entityPlayer.getClass().getField("b");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = playerConnection.getClass().getDeclaredField("h");
            networkManagerField.setAccessible(true);
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("m");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_19_R3(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
        }
    }

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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R2.entity.CraftPlayer) player).getHandle();
            Field playerConnectionField = entityPlayer.getClass().getField("b");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = playerConnection.getClass().getField("b");
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("m");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_19_R2(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
        }
    }

    /**
     * Packet handler for vehicle steering in 1.19-1.19.2
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
        Channel channel = null;
        try {
            Object entityPlayer = ((org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer) player).getHandle();
            Field playerConnectionField = entityPlayer.getClass().getField("b");
            net.minecraft.server.network.PlayerConnection playerConnection = (net.minecraft.server.network.PlayerConnection) playerConnectionField.get(entityPlayer);
            Field networkManagerField = playerConnection.getClass().getField("b");
            net.minecraft.network.NetworkManager networkManager = (net.minecraft.network.NetworkManager) networkManagerField.get(playerConnection);
            Field channelField = networkManager.getClass().getField("m");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_19(player);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
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
        Channel channel = null;
        try {
            Object networkManager = ((org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer) player).getHandle().b.a;
            Field channelField = networkManager.getClass().getField("m");
            channel = (Channel) channelField.get(networkManager);

            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_18_R2(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        } catch (IllegalAccessException | NoSuchFieldException e) {
            unexpectedException(e);
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
        Channel channel = null;
        try {
            channel = ((org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer) player).getHandle().b.a.k; // #wtf
            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_18_R1(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
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
        Channel channel = null;
        try {
            channel = ((org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer) player).getHandle().b.a.k; // #wtf
            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_17(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
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
        Channel channel = null;
        try {
            channel = ((org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_16(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
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
        Channel channel = null;
        try {
            channel = ((org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_15(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
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
        Channel channel = null;
        try {
            channel = ((org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_13(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
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
        Channel channel = null;
        try {
            channel = ((org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
            channel.pipeline()
                    .addBefore("packet_handler", player.getName(), channelDuplexHandler);
        } catch (IllegalArgumentException e) { //in case of plugin reload, prevent duplicate handler name exception
            if (channel == null) {
                unexpectedException(e);
                return;
            }
            if (!channel.pipeline().names().contains(player.getName())) return;
            channel.pipeline().remove(player.getName());
            movement_1_16(player);
        } catch (NoSuchElementException e) {
            //It isn't good practice to ignore exceptions, but I'll keep it like this for now :)
        }
    }

    @ToDo("make language specific")
    private static void unexpectedException(){
        Main.logSevere("An unexpected error occurred. Disabling the plugin...");
        Main.disablePlugin();
    }

    @ToDo("make language specific")
    private static void unexpectedException(Exception e){
        Main.logSevere("An unexpected error occurred, disabling the plugin... Check the exception log:");
        e.printStackTrace();
        Main.disablePlugin();
    }

    /**
     * Check whether a given object is a valid steering packet (PacketPlayInSteerVehicle). If not, return false and send an error to the console.
     *
     * @param object Checked object (likely a packet)
     * @return True if the given object is an instance of the steering packet (PacketPlayInSteerVehicle).
     */
    @VersionSpecific
    @ToDo("make language specific")
    public static boolean isObjectPacket(Object object) {
        final String errorMessage = "An unexpected error occurred (given object is not a valid steering packet). Try reinstalling the plugin or contact the developer: https://discord.gg/vehicle";

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
