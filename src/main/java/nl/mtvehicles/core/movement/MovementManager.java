package nl.mtvehicles.core.movement;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.entity.Player;

public class MovementManager {
    public static void MovementSelector(Player p) {
        if (VersionModule.getServerVersion().is1_12()) PacketHandler.movement_1_12(p);
        else if (VersionModule.getServerVersion().is1_13()) PacketHandler.movement_1_13(p);
        else if (VersionModule.getServerVersion().is1_15()) PacketHandler.movement_1_15(p);
        else if (VersionModule.getServerVersion().is1_16()) PacketHandler.movement_1_16(p);
        else if (VersionModule.getServerVersion().is1_17()) PacketHandler.movement_1_17(p);
        else if (VersionModule.getServerVersion().is1_18()) PacketHandler.movement_1_18(p);
    }
}
