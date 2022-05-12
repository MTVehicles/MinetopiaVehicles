package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.jetbrains.annotations.NotNull;

/**
 * The plugin's version
 */
public enum PluginVersion {
    LEGACY,
    v2_3_0,
    v2_4_0_pre1,
    v2_4_0_rc1,
    v2_4_0,
    v2_4_1,
    v2_4_2,
    LATEST,
    DEV;

    public int getOrder(){
        return ordinal();
    }

    /**
     * Check whether the plugin version is a dev-version (auto-updater is disabled)
     * @return True if plugin is a dev-version
     */
    public boolean isDev(){
        return this.equals(DEV);
    }

    /**
     * Get the plugin's version as enum
     */
    public static PluginVersion getPluginVersion(){
        return getVersion(VersionModule.pluginVersionString);
    }

    /**
     * Get plugin version from a String
     */
    public static PluginVersion getVersion(String version){
        if (version.toLowerCase().contains("dev")) return DEV;
        try {
            return valueOf("v" + version.replace(".", "_").replace("-", "_"));
        } catch (IllegalArgumentException e){
            return LATEST;
        }
    }

    /**
     * Check whether the version is older than a given version
     */
    public boolean isOlderThan(@NotNull PluginVersion compareVersion){
        return getOrder() < compareVersion.getOrder();
    }

}
