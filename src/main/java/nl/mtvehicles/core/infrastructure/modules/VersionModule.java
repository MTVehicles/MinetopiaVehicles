package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.logging.Logger;

public class VersionModule {
    private static @Getter
    @Setter
    VersionModule instance;

    public static String pluginVersion;
    public static boolean isPreRelease;
    private static String serverVersion;
    public static String serverSoftware;
    Logger logger = Main.instance.getLogger();

    public VersionModule() {
        PluginDescriptionFile pdf = Main.instance.getDescription();
        pluginVersion = pdf.getVersion();

        //Pre-releases should thus be named "vX.Y.Z-preU" etc... (Instead of pre, dev for developing and rc for release candidates are acceptable too.)
        isPreRelease = pluginVersion.toLowerCase().contains("pre") || pluginVersion.toLowerCase().contains("rc") || pluginVersion.toLowerCase().contains("dev");

        serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        serverSoftware = Bukkit.getName();
    }

    public static ServerVersion getServerVersion(){
        ServerVersion returns = null;
        switch (serverVersion) {
            case "v1_12_R1":
                returns = ServerVersion.v1_12;
                break;
            case "v1_13_R2":
                returns = ServerVersion.v1_13;
                break;
            case "v1_15_R1":
                returns = ServerVersion.v1_15;
                break;
            case "v1_16_R3":
                returns = ServerVersion.v1_16;
                break;
            case "v1_17_R1":
                returns = ServerVersion.v1_17;
                break;
            case "v1_18_R1":
                returns = ServerVersion.v1_18_R1;
                break;
            case "v1_18_R2":
                returns = ServerVersion.v1_18_R2;
                break;
        }
        return returns;
    }

    public boolean isSupportedVersion(){
        if (getServerVersion() == null) {
            logger.severe("--------------------------");
            logger.severe("Your Server version is not supported. The plugin will NOT load.");
            logger.severe("Check the supported versions here: https://wiki.mtvehicles.eu/faq.html");
            logger.severe("--------------------------");
            Main.disablePlugin();
            return false;
        }

        else if (!Bukkit.getVersion().contains("1.12.2") && !Bukkit.getVersion().contains("1.13.2") && !Bukkit.getVersion().contains("1.15.2") && !Bukkit.getVersion().contains("1.16.5") && !Bukkit.getVersion().contains("1.17.1") && !Bukkit.getVersion().contains("1.18.2")) {
            logger.warning("--------------------------");
            logger.warning("Your Server does not run the latest patch version (e.g. you may be running 1.18 instead of 1.18.2 etc...).");
            logger.warning("The plugin WILL load but it MAY NOT work properly. UPDATE.");
            logger.warning("Check the supported versions here: https://wiki.mtvehicles.eu/faq.html");
            logger.warning("--------------------------");
        }

        else if (!serverSoftware.equals("Spigot") && !serverSoftware.equals("Paper") && !serverSoftware.equals("CraftBukkit")){
            logger.warning("--------------------------");
            logger.warning("Your Server is not running Spigot, nor Paper (" + serverSoftware + " detected).");
            logger.warning("The plugin WILL load but it MAY NOT work properly. Full support is guaranteed only on Spigot/Paper.");
            logger.warning("We'll be more than happy to help you on our Discord server (https://discord.gg/vehicle).");
            logger.warning("--------------------------");
        }

        return true;
    }
}
