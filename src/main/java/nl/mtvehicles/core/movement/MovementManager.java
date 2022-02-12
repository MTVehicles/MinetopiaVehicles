package nl.mtvehicles.core.movement;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.entity.Player;

public class MovementManager {
    public static void MovementSelector(Player p) {
        switch (VersionModule.serverVersion) {
            case "v1_12_R1":
                PacketHandler.movement_1_12(p);
                break;
            case "v1_13_R2":
                PacketHandler.movement_1_13(p);
                break;
            case "v1_15_R1":
                PacketHandler.movement_1_15(p);
                break;
            case "v1_16_R3":
                PacketHandler.movement_1_16(p);
                break;
            case "v1_17_R1":
                PacketHandler.movement_1_17(p);
                break;
            case "v1_18_R1":
                PacketHandler.movement_1_18(p);
                break;    
        }
    }
}
