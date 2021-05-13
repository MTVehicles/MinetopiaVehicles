package nl.mtvehicles.core.Events;

import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    @EventHandler(priority = EventPriority.HIGH)
    public void onJoinEventPlayer(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!p.hasPermission("mtvehicles.update") || !Main.defaultConfig.getConfig().getBoolean("auto-update")) {
            return;
        }
        checkNewVersion(p);
    }

    public void getUpdateMessage(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
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
                ex.printStackTrace();
                Bukkit.getLogger().info("We hebben geen verbinding kunnen maken met de servers van MinetopiaVehicles.");
            }
        });
    }

    public void checkNewVersion(Player p) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
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
                ex.printStackTrace();
                Bukkit.getLogger().info("We hebben geen verbinding kunnen maken met de servers van MinetopiaVehicles.");
            }
        });
    }

}