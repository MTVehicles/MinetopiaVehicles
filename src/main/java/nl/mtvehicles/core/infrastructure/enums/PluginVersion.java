package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.jetbrains.annotations.NotNull;

/**
 * The plugin's version
 */
@Deprecated
public enum PluginVersion {
    /**
     * Legacy versions (older than 2.3.0)
     * @deprecated
     */
    LEGACY,
    /**
     * 2.3.0 (The fuel and dependency update)
     */
    v2_3_0,
    /**
     * 2.4.0-pre1
     */
    v2_4_0_pre1,
    /**
     * 2.4.0-rc1
     */
    v2_4_0_rc1,
    /**
     * 2.4.0 (The Planes and the API update)
     */
    v2_4_0,
    /**
     * 2.4.1 (Hotfixes)
     */
    v2_4_1,
    /**
     * 2.4.2
     */
    v2_4_2,
    /**
     * 2.4.3 (with 1.19 support)
     */
    v2_4_3,
    /**
     * 2.5.0-pre1
     */
    v2_5_0_pre1,
    /**
     * 2.5.0-pre2
     */
    v2_5_0_pre2,
    /**
     * 2.5.0 (Boats and API overhaul, 1.20 support)
     */
    v2_5_0,
    /**
     * 2.5.1 (Skript)
     */
    v2_5_1,
    /**
     * 2.5.2
     * @deprecated <b>(auto-updater not working)</b>
     */
    v2_5_2,
    /**
     * 2.5.3 (auto-updater hotfix)
     */
    v2_5_3,
    /**
     * 2.5.4 (1.20.6 Update & Bug fixes)
     */
    v2_5_4,
    /**
     * 2.5.5 (next expected version)
     */
    v2_5_5,
    /**
     * Latest version (from auto-updater)
     */
    LATEST,
    /**
     * Dev-builds (auto-updater is disabled)
     */
    DEV;

    public int getOrder(){
        return ordinal();
    }

    /**
     * Check whether the plugin version is a dev-version (auto-updater is disabled)
     * @return True if plugin is a dev-build
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
