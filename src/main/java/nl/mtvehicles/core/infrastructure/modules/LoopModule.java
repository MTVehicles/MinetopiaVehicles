package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class LoopModule {
    private static @Getter
    @Setter
    LoopModule instance;

    public LoopModule() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.isInsideVehicle()) {
                p.kickPlayer(TextUtils.colorize(ConfigModule.messagesConfig.getMessage("reloadInVehicle")));
            }
            MovementManager.MovementSelector(p);
        }
    }
}
