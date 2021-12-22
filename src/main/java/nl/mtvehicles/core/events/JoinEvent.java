package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.helpers.ItemUtils;
import nl.mtvehicles.core.infrastructure.helpers.TextUtils;
import nl.mtvehicles.core.infrastructure.models.ConfigUtils;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.movement.MovementManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class JoinEvent implements Listener {
    public static HashMap<UUID, Boolean> languageCheck = new HashMap<>();

    @EventHandler
    public void onJoinEventPlayer(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        MovementManager.MovementSelector(p);

        if (ConfigModule.defaultConfig.getConfig().getString("messagesLanguage").contains("ns")) {
            if (p.hasPermission("mtvehicles.language")) {
                p.sendMessage(TextUtils.colorize("&cHey! You have not yet changed the language of the plugin. Do this by with &4/vehicle language&c!"));
            }
        }

        if (!p.hasPermission("mtvehicles.update") || !ConfigModule.defaultConfig.getConfig().getBoolean("auto-update")) {
            return;
        }

        if (!Main.isPreRelease) checkNewVersion(p);
    }

    public static void checkLanguage(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "Choose your language");
        inv.setItem(10, ItemUtils.mItem("GOLD_BLOCK", 1, (short) 0, "&eEnglish", "&7Press to set all messages to English."));
        inv.setItem(12, ItemUtils.mItem("DIAMOND_BLOCK", 1, (short) 0, "&9Dutch (Nederlands)", "&7Druk om alle berichten op Nederlands te zetten."));
        inv.setItem(14, ItemUtils.mItem("EMERALD_BLOCK", 1, (short) 0, "&2Spanish (Español)", "&7Presione para configurar todos los mensajes en español."));
        inv.setItem(16, ItemUtils.mItem("REDSTONE_BLOCK", 1, (short) 0, "&4Czech (Čeština)", "&7Klikni pro nastavení všech zpráv do češtiny."));
        p.openInventory(inv);
        languageCheck.put(p.getUniqueId(), true);
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
            ex.printStackTrace();
            Bukkit.getLogger().info("We hebben geen verbinding kunnen maken met de servers van MinetopiaVehicles.");
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
            ex.printStackTrace();
            Bukkit.getLogger().info("We hebben geen verbinding kunnen maken met de servers van MinetopiaVehicles.");
        }
    }
}