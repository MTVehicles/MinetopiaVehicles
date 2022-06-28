package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Module looping all players after a plugin reload.
 * An appropriate packet handler is assigned and players sitting in vehicles are kicked.
 */
public class LoopModule {
    private static @Getter
    @Setter
    LoopModule instance;

    public LoopModule() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.isInsideVehicle()) {
                p.kickPlayer(TextUtils.colorize(ConfigModule.messagesConfig.getMessage(Message.RELOAD_IN_VEHICLE)));
            }
            MovementManager.MovementSelector(p);
        }
    }
}
