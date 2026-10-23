package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.movement.PacketHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Enum of supported server versions (used for different NMS and Spigot API
 * changes)
 */
@VersionSpecific
public enum ServerVersion {
    /**
     * 1.12-1.12.2
     */
    v1_12_R1(PacketHandler::movement_1_12_R1),
    /**
     * 1.13.1-1.13.2
     */
    v1_13_R2(PacketHandler::movement_1_13_R2),
    /**
     * 1.14-1.14.4
     */
    v1_14_R1(PacketHandler::movement_1_14_R1),
    /**
     * 1.15-1.15.2
     */
    v1_15_R1(PacketHandler::movement_1_15_R1),
    /**
     * 1.16.4-1.16.5
     */
    v1_16_R3(PacketHandler::movement_1_16_R3),
    /**
     * 1.17-1.17.1
     */
    v1_17_R1(PacketHandler::movement_1_17_R1),
    /**
     * 1.18-1.18.1
     */
    v1_18_R1(PacketHandler::movement_1_18_R1),
    /**
     * 1.18.2
     */
    v1_18_R2(PacketHandler::movement_1_18_R2),
    /**
     * 1.19-1.19.2
     * 
     * @since 2.4.3
     */
    v1_19_R1(PacketHandler::movement_1_19_R1),
    /**
     * 1.19.3
     * 
     * @since 2.5.0
     */
    v1_19_R2(PacketHandler::movement_1_19_R2),
    /**
     * 1.19.4
     * 
     * @since 2.5.0
     */
    v1_19_R3(PacketHandler::movement_1_19_R3),
    /**
     * 1.20 and 1.20.1
     * 
     * @since 2.5.6
     */
    v1_20_R1(PacketHandler::movement_1_20_R1),
    /**
     * 1.20.2
     * 
     * @since 2.5.2
     */
    v1_20_R2(PacketHandler::movement_1_20_R2),
    /**
     * 1.20.3 and 1.20.4
     * 
     * @since 2.5.2
     */
    v1_20_R3(PacketHandler::movement_1_20_R3),
    /**
     * 1.20.5 and 1.20.6
     * 
     * @since 2.5.4
     */
    v1_20_R4(PacketHandler::movement_1_20_R4),
    /**
     * 1.21 and 1.21.1
     * 
     * @since 2.5.4
     */
    v1_21_R1(PacketHandler::movement_1_21_R1),
    /**
     * 1.21.2 and 1.21.3
     * 
     * @since 2.5.5
     */
    v1_21_R2(PacketHandler::movement_1_21_R2),
    /**
     * 1.21.4
     * 
     * @since 2.5.5
     */
    v1_21_R3(PacketHandler::movement_1_21_R3),
    /**
     * 1.21.5
     * 
     * @since 2.5.6
     */
    v1_21_R4(PacketHandler::movement_1_21_R4),
    /**
     * 1.21.6, 1.21.7 and 1.21.8
     * 
     * @since 2.5.6
     */
    v1_21_R5(PacketHandler::movement_1_21_R5),
    /**
     * 1.21.9 and 1.21.10
     * 
     * @since 2.5.8
     */
    v1_21_R6(PacketHandler::movement_1_21_R6),
    /**
     * 1.21.11
     * 
     * @since 2.5.9
     */
    v1_21_R7(PacketHandler::movement_1_21_R7),
    /**
     * 26.1, 26.1.1 and 26.1.2
     * 
     * @since 2.5.9
     */
    v26_1(PacketHandler::movement_modern),
    /**
     * 26.2
     * 
     * @since 2.5.9
     */
    v26_2(PacketHandler::movement_modern),
    /**
     * 26.3
     * 
     * @since 2.5.10
     */
    v26_3(PacketHandler::movement_modern);

    /**
     * Function interface for packet movement handlers
     */
    public interface MovementHandler {
        void handle(Player player);
    }

    private final MovementHandler movementHandler;

    ServerVersion(MovementHandler movementHandler) {
        this.movementHandler = movementHandler;
    }

    /**
     * Get the movement handler for this server version
     * 
     * @return The movement handler for this version
     */
    public MovementHandler getMovementHandler() {
        return this.movementHandler;
    }

    /**
     * Check whether the server version is older than the given one
     */
    public boolean isOlderThan(@NotNull ServerVersion version) {
        return this.ordinal() < version.ordinal();
    }

    /**
     * Check whether the server version is older than the given one or whether it is
     * the same
     */
    public boolean isOlderOrEqualTo(@NotNull ServerVersion version) {
        return this.ordinal() <= version.ordinal();
    }

    /**
     * Check whether the server version is newer than the given one
     */
    public boolean isNewerThan(@NotNull ServerVersion version) {
        return this.ordinal() > version.ordinal();
    }

    /**
     * Check whether the server version is newer than the given one or whether it is
     * the same
     */
    public boolean isNewerOrEqualTo(@NotNull ServerVersion version) {
        return this.ordinal() >= version.ordinal();
    }

}
