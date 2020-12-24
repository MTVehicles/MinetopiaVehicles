package nl.mtvehicles.core.Commands.VehiclesSubs;

import nl.mtvehicles.core.Infrastructure.Helpers.TextUtils;
import nl.mtvehicles.core.Infrastructure.Models.MTVehicleSubCommand;
import nl.mtvehicles.core.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class VehicleUpdate extends MTVehicleSubCommand {

    @Override
    public boolean execute(CommandSender sender, Command cmd, String s, String[] args) {
        if (!isPlayer) return false;

        if (!checkPermission("mtvehicles.update")) return true;

        Player p = (Player) sender;

        if (Main.defaultConfig.getConfig().getBoolean("auto-update") == false){
            sendMessage(Main.messagesConfig.getMessage("updateDisabled"));
            return false;
        }
        checkNewVersion(p);

        return true;
    }

    public void checkNewVersion(Player p) {
        try {
            URLConnection connection = new URL("https://minetopiavehicles.nl/api/update-api-version.php").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                sb.append(line);
            }
            String value = sb.toString();
            PluginDescriptionFile pdf = Main.instance.getDescription();
            if (!value.contains(pdf.getVersion())) {
                p.sendMessage(TextUtils.colorize("&aWe hebben een update gevonden heb even geduld!"));
                File dest = new File("plugins");
                URL file;
                try {
                    download(file = new URL("https://minetopiavehicles.nl/api/MTVehicles.jar"), dest);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                p.sendMessage(TextUtils.colorize("&cEr is geen update gevonden, is dit een fout meld het dan in de discord. https://mtvehicles.nl"));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            Bukkit.getLogger().info("We hebben geen verbinding kunnen maken met de servers van MinetopiaVehicles.");
        }
    }

    public void download(URL file, File dest) {
        try {
            InputStream is = file.openStream();
            File finaldest = new File(dest + "/" + file.getFile().replace("/api/MTVehicles.jar", "/"+Main.fol().replace("plugins", "")));
           // File finaldest = new File(dest + "/" + file.getFile());
            finaldest.getParentFile().mkdirs();
            finaldest.createNewFile();
            System.out.println("Voor de laatste stap moeten we even de server herladen!");

            OutputStream os = new FileOutputStream(finaldest);
            byte data[] = new byte[1024];
            int count;
            while ((count = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            is.close();
            os.close();
            sendMessage(Main.messagesConfig.getMessage("updatedSucces"));


        } catch (Exception ec) {
            sendMessage(Main.messagesConfig.getMessage("updateFailed"));
            ec.printStackTrace();
        }
    }
}