package nl.mtvehicles.core.events;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoinEventPlayer(PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        MovementManager.MovementSelector(p);

        if (ConfigModule.secretSettings.getMessagesLanguage().contains("ns")) {
            if (p.hasPermission("mtvehicles.language") || p.hasPermission("mtvehicles.admin")) {
                p.sendMessage(TextUtils.colorize("&cHey! You have not changed the language of the plugin yet. Do this by executing &4/vehicle language&c!"));
            }
        }

        if (!p.hasPermission("mtvehicles.update") || !ConfigModule.defaultConfig.getConfig().getBoolean("auto-update"))
            return;

        if (!VersionModule.isPreRelease) checkNewVersion(p);
    }

    public void getUpdateMessage(Player p) {
        try {
            URLConnection connection = new URL("https://minetopiavehicles.nl/api/update-api-check.php").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String value = sb.toString();
            String[] vet = value.split("@");
            PluginDescriptionFile pdf = Main.instance.getDescription();
            for (String s : vet) {
                p.sendMessage(TextUtils.colorize(s.replace("<oldVer>", pdf.getVersion())));
            }
        } catch (IOException ex) {
            Main.logSevere("The plugin cannot connect to MTVehicles servers. Try again later...");
            ex.printStackTrace();
        }
    }

    public void checkNewVersion(Player p) {
        try {
            URLConnection connection = new URL("https://minetopiavehicles.nl/api/update-api-version.php").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String value = sb.toString();
            PluginDescriptionFile pdf = Main.instance.getDescription();
            if (!value.contains(pdf.getVersion())) {
                getUpdateMessage(p);
            }
        } catch (IOException ex) {
            Main.logSevere("The plugin cannot connect to MTVehicles servers. Try again later...");
            ex.printStackTrace();
        }
    }
}