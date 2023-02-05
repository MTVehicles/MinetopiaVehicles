package nl.mtvehicles.core.movement;

import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import org.bukkit.entity.Player;

import static nl.mtvehicles.core.infrastructure.modules.VersionModule.getServerVersion;

/**
 * Movement selector depending on what version the server uses.
 */
public class MovementManager {
    /**
     * Select a packet handler for a player
     */
    @VersionSpecific
    public static void MovementSelector(Player player) {
        if (getServerVersion().is1_12()) PacketHandler.movement_1_12(player);
        else if (getServerVersion().is1_13()) PacketHandler.movement_1_13(player);
        else if (getServerVersion().is1_15()) PacketHandler.movement_1_15(player);
        else if (getServerVersion().is1_16()) PacketHandler.movement_1_16(player);
        else if (getServerVersion().is1_17()) PacketHandler.movement_1_17(player);
        else if (getServerVersion().is1_18_R1()) PacketHandler.movement_1_18_R1(player);
        else if (getServerVersion().is1_18_R2()) PacketHandler.movement_1_18_R2(player);
        else if (getServerVersion().is1_19()) PacketHandler.movement_1_19(player);
        else if (getServerVersion().is1_19_R2()) PacketHandler.movement_1_19_R2(player);
    }
}
