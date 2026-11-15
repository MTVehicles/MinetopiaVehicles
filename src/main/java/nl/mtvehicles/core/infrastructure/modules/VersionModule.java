package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.annotations.VersionSpecific;
import nl.mtvehicles.core.infrastructure.enums.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Module containing information about the plugin and server version
 */
public class VersionModule {
    private static @Getter @Setter VersionModule instance;

    /**
     * The plugin's current version
     *
     * @deprecated Use {@link #getPluginVersion()} to access the variable. (This
     *             variable may be private in future versions.)
     * @see #getPluginVersion()
     */
    @Deprecated
    public static String pluginVersionString;
    /**
     * True if the plugin is a pre-release, release candidate or a dev-build
     */
    public static boolean isPreRelease;
    /**
     * True if the plugin is a dev-build (auto-updater is disabled)
     */
    public static boolean isDevRelease;
    /**
     * The server's software (e.g. 'Paper')
     */
    private static String serverSoftware;
    /**
     * Server Version
     */
    private static ServerVersion serverVersion;

    private Logger logger = Main.instance.getLogger();

    public VersionModule() {
        PluginDescriptionFile pdf = Main.instance.getDescription();
        pluginVersionString = pdf.getVersion();

        // Pre-releases should thus be named "vX.Y.Z-preU" etc... (Instead of pre, dev
        // for developing and rc for release candidates are acceptable too.)
        isPreRelease = pluginVersionString.toLowerCase().contains("pre")
                || pluginVersionString.toLowerCase().contains("rc")
                || pluginVersionString.toLowerCase().contains("dev");
        isDevRelease = pluginVersionString.toLowerCase().contains("dev");
        serverSoftware = Bukkit.getName();

        // Check Server Version
        if (!serverSoftware.contains("Arclight")) {
            String serverVersionString;
            try {
                serverVersionString = Bukkit.getServer().getMinecraftVersion();
            } catch (NoSuchMethodError e) {
                String packageName = Bukkit.getServer().getClass().getPackage().getName();
                if (packageName.split("\\.").length < 4) {
                    serverVersionString = Bukkit.getServer().getBukkitVersion().split("-")[0];
                } else {
                    serverVersionString = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                }
            }
            selectServerVersion(serverVersionString);
        }
    }

    /**
     * The correct approach to get the plugin's version (as String – e.g. '2.4.2')
     * 
     * @since 2.5.8
     */
    public static String getPluginVersion() {
        return pluginVersionString;
    }

    /**
     * Get the server version as enum
     * 
     * @return Server version
     */
    public static ServerVersion getServerVersion() {
        return serverVersion;
    }

    /**
     * Select the server version based on a string
     * 
     * @param serverVersionString Server version string
     */
    private static void selectServerVersion(String serverVersionString) {
        switch (serverVersionString) {
            case "1.12":
            case "1.12.1":
            case "1.12.2":
            case "v1_12_R1":
                serverVersion = ServerVersion.v1_12_R1;
                break;
            case "1.13.1":
            case "1.13.2":
            case "v1_13_R2":
                serverVersion = ServerVersion.v1_13_R2;
                break;
            case "1.14":
            case "1.14.1":
            case "1.14.2":
            case "1.14.3":
            case "1.14.4":
            case "v1_14_R1":
                serverVersion = ServerVersion.v1_14_R1;
                break;
            case "1.15":
            case "1.15.1":
            case "1.15.2":
            case "v1_15_R1":
                serverVersion = ServerVersion.v1_15_R1;
                break;
            case "1.16.4":
            case "1.16.5":
            case "v1_16_R3":
                serverVersion = ServerVersion.v1_16_R3;
                break;
            case "1.17":
            case "1.17.1":
            case "v1_17_R1":
                serverVersion = ServerVersion.v1_17_R1;
                break;
            case "1.18":
            case "1.18.1":
            case "v1_18_R1":
                serverVersion = ServerVersion.v1_18_R1;
                break;
            case "1.18.2":
            case "v1_18_R2":
                serverVersion = ServerVersion.v1_18_R2;
                break;
            case "1.19":
            case "1.19.1":
            case "1.19.2":
            case "v1_19_R1":
                serverVersion = ServerVersion.v1_19_R1;
                break;
            case "1.19.3":
            case "v1_19_R2":
                serverVersion = ServerVersion.v1_19_R2;
                break;
            case "1.19.4":
            case "v1_19_R3":
                serverVersion = ServerVersion.v1_19_R3;
                break;
            case "1.20":
            case "1.20.1":
            case "v1_20_R1":
                serverVersion = ServerVersion.v1_20_R1;
                break;
            case "1.20.2":
            case "1.20.3":
            case "v1_20_R2":
                serverVersion = ServerVersion.v1_20_R2;
                break;
            case "1.20.4":
            case "1.20.5":
            case "v1_20_R3":
                serverVersion = ServerVersion.v1_20_R3;
                break;
            case "1.20.6":
            case "v1_20_R4":
                serverVersion = ServerVersion.v1_20_R4;
                break;
            case "1.21":
            case "1.21.1":
            case "1.21.2":
            case "v1_21_R1":
                serverVersion = ServerVersion.v1_21_R1;
                break;
            case "1.21.3":
            case "v1_21_R2":
                serverVersion = ServerVersion.v1_21_R2;
                break;
            case "1.21.4":
            case "v1_21_R3":
                serverVersion = ServerVersion.v1_21_R3;
                break;
            case "1.21.5":
            case "v1_21_R4":
                serverVersion = ServerVersion.v1_21_R4;
                break;
            case "1.21.6":
            case "1.21.7":
            case "1.21.8":
            case "v1_21_R5":
                serverVersion = ServerVersion.v1_21_R5;
                break;
            case "1.21.9":
            case "1.21.10":
            case "v1_21_R6":
                serverVersion = ServerVersion.v1_21_R6;
                break;
            case "1.21.11":
            case "v1_21_R7":
                serverVersion = ServerVersion.v1_21_R7;
                break;
            case "26.1":
            case "26.1.1":
            case "26.1.2":
                serverVersion = ServerVersion.v26_1;
                break;
            case "26.2":
                serverVersion = ServerVersion.v26_2;
                break;
            case "26.3":
                serverVersion = ServerVersion.v26_3;
                break;

        }
    }

    /**
     * Check whether the server version is supported by the plugin.
     * Otherwise, send a warning and disable the plugin.
     * 
     * @return True if the server version is supported
     */
    @VersionSpecific
    public boolean isSupportedVersion() {

        List<String> highestVersions = Arrays.asList(
                "1.12.2", "1.13.2", "1.15.2", "1.16.5", "1.17.1", "1.18.2", "1.19.4", "1.20.6", "1.21.11", "26.1.2",
                "26.2", "26.3",
                "v1_21_R7", "v1_20_R4", "v1_19_R3", "v1_18_R2", "v1_17_R1", "v1_16_R3", "v1_15_R1", "v1_14_R1",
                "v1_13_R2", "v1_12_R1"

        );
        String serverVersionString = null;
        // Check Server Version
        if (!serverSoftware.contains("Arclight")) {
            try {
                serverVersionString = Bukkit.getServer().getMinecraftVersion();
            } catch (NoSuchMethodError e) {
                String packageName = Bukkit.getServer().getClass().getPackage().getName();
                if (packageName.split("\\.").length < 4) {
                    serverVersionString = Bukkit.getServer().getBukkitVersion().split("-")[0];
                } else {
                    serverVersionString = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                }
            }
        }

        if (getServerVersion() == null) {
            logger.severe("--------------------------");
            logger.severe("Your Server version is not supported. The plugin will NOT load.");
            logger.severe("Check the supported versions here: https://wiki.mtvehicles.eu/faq.html");
            logger.severe("--------------------------");
            Main.disablePlugin();
            return false;
        }

        else if (!highestVersions.contains(serverVersionString)) {
            logger.warning("--------------------------");
            logger.warning(
                    "Your Server does not run the latest patch version (e.g. you may be running 1.18 instead of 1.18.2 etc...).");
            logger.warning("The plugin WILL load but it MAY NOT work properly. UPDATE.");
            logger.warning("Check the supported versions here: https://wiki.mtvehicles.eu/faq.html");
            logger.warning("--------------------------");
        }

        else if (!serverSoftware.equals("Spigot") && !serverSoftware.equals("Paper")
                && !serverSoftware.equals("CraftBukkit")) {
            logger.warning("--------------------------");
            logger.warning("Your Server is not running Spigot, nor Paper (" + serverSoftware + " detected).");
            logger.warning(
                    "The plugin WILL load but it MAY NOT work properly. Full support is guaranteed only on Spigot/Paper.");
            logger.warning("We'll be more than happy to help you on our Discord server (https://discord.gg/vehicle).");
            logger.warning("--------------------------");
        }

        return true;
    }
}
