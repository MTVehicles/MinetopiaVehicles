package nl.mtvehicles.core.infrastructure.helpers;

import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.dataconfig.DefaultConfig;
import nl.mtvehicles.core.infrastructure.enums.Message;
import nl.mtvehicles.core.infrastructure.enums.PluginVersion;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.bukkit.command.CommandSender;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ToDo(comment = "Translate to multiple languages.")
public class PluginUpdater {
    private static boolean isEnabled = (boolean) ConfigModule.defaultConfig.get(DefaultConfig.Option.AUTO_UPDATE);
    private static PluginVersion pluginVersion = PluginVersion.getPluginVersion();
    private static PluginVersion latestVersion = receiveLatestVersion();
    private static String latestVersionString;

    private static PluginVersion receiveLatestVersion(){
        if (!isEnabled) {
            Main.logWarning(ConfigModule.messagesConfig.getMessage(Message.UPDATE_DISABLED));
            return null;
        }
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
            String receivedValue = sb.toString();
            latestVersionString = receivedValue;
            return PluginVersion.getVersion(receivedValue);
        } catch (IOException ex) {
            Main.logSevere("The plugin cannot connect to MTVehicles servers. Try again later...");
            ex.printStackTrace();
            return null;
        }
    }

    @Deprecated
    private static String[] receiveUpdateMessage(){
        if (!isEnabled) {
            Main.logWarning(ConfigModule.messagesConfig.getMessage(Message.UPDATE_DISABLED));
            return null;
        }
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
            String receivedValue = sb.toString();
            String[] valueMultipleLines = receivedValue.split("@");
            for (int i = 0; i < valueMultipleLines.length; i++) {
                valueMultipleLines[i] = valueMultipleLines[i].replace("<oldVer>", VersionModule.pluginVersion);
            }
            return valueMultipleLines;
        } catch (IOException ex) {
            Main.logSevere("The plugin cannot connect to MTVehicles servers. Try again later...");
            ex.printStackTrace();
            return new String[]{""};
        }
    }

    @ToDo(comment = "Make this translatable, maybe?")
    private static List<String> getUpdateMessage(){
        return TextUtils.list(
                "&7---------------------------------------",
                "A new version of &2MTVehicles&f is available!",
                String.format("We have already released &av%s &fbut you are still using &cv%s&f!", latestVersionString, VersionModule.pluginVersion),
                "Use &2/mtv update&f to update! (Don't forget to reload the plugin!)",
                "For more information visit &nhttps://mtvehicles.eu&f!",
                "&7---------------------------------------"
        );
    }

    public static PluginVersion getLatestVersion(){
        return latestVersion;
    }

    public static boolean isLatestVersion(){
        if (latestVersion == null){ //If previous connection was not successful, try connecting one more time
            latestVersion = receiveLatestVersion();
            if (latestVersion == null) return true; //If this connection failed as well, abort checking versions.
        }
        return !pluginVersion.isOlderThan(latestVersion);
    }

    public static void checkNewVersion(CommandSender sender){
        if (!isLatestVersion()) sendUpdateMessage(sender);
    }

    private static void sendUpdateMessage(CommandSender sender){
        for (String line: getUpdateMessage()) {
            sender.sendMessage(TextUtils.colorize(line));
        }
    }

    public static void updatePlugin(CommandSender sender){
        if (isLatestVersion()) {
            sender.sendMessage(TextUtils.colorize("&cYou're already using the latest version."));
            return;
        }

        sender.sendMessage(TextUtils.colorize("&aPlugin update in process..."));
        if (downloadUpdate()) sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.UPDATE_SUCCESSFUL));
        else sender.sendMessage(ConfigModule.messagesConfig.getMessage(Message.UPDATE_FAILED));
    }

    private static boolean downloadUpdate() {
        if (!isEnabled) {
            Main.logWarning(ConfigModule.messagesConfig.getMessage(Message.UPDATE_DISABLED));
            return false;
        }
        try {
            URL file = new URL("https://minetopiavehicles.nl/api/MTVehicles.jar");
            File dest = new File("plugins");
            InputStream is = file.openStream();
            File finaldest = new File(dest + "/" + file.getFile().replace("/api/MTVehicles.jar", "/" + Main.getFileAsString().replace("plugins", "")));
            // File finaldest = new File(dest + "/" + file.getFile());
            finaldest.getParentFile().mkdirs();
            finaldest.createNewFile();
            Main.logInfo("Reload the server/plugin to finish the plugin update!");

            OutputStream os = new FileOutputStream(finaldest);
            byte[] data = new byte[1024];
            int count;
            while ((count = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, count);
            }
            os.flush();
            is.close();
            os.close();
            return true;

        } catch (Exception ec) {
            Main.logSevere("An error occurred whilst trying to download the plugin. (Java 11+ required)");
            ec.printStackTrace();
            return false;
        }
    }

}
