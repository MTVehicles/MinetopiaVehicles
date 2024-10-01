package nl.mtvehicles.core.listeners;

import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.infrastructure.utils.PluginUpdater;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * On player join (a packet handler is assigned + update and language message if OP / with permission)
 */
public class JoinListener extends MTVListener {

    @EventHandler
    public void onJoinEventPlayer(PlayerJoinEvent event) {
        this.event = event;
        player = event.getPlayer();

        MovementManager.MovementSelector(player);

        if (ConfigModule.secretSettings.getMessagesLanguage().contains("ns")) {
            if (player.hasPermission("mtvehicles.language") || player.hasPermission("mtvehicles.admin")) {
                player.sendMessage(TextUtils.colorize("&cHey! You have not changed the language of the plugin yet. Do this by executing &4/vehicle language&c!"));
            }
        }

        if (!player.hasPermission("mtvehicles.update") || !(boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE)) return;

        if (!VersionModule.isDevRelease) PluginUpdater.checkNewVersion(player);
    }
}
