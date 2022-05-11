package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.jetbrains.annotations.NotNull;

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

    public boolean isDev(){
        return this.equals(DEV);
    }

    public static PluginVersion getPluginVersion(){
        return getVersion(VersionModule.pluginVersionString);
    }

    public static PluginVersion getVersion(String version){
        if (version.toLowerCase().contains("dev")) return DEV;
        try {
            return valueOf("v" + version.replace(".", "_").replace("-", "_"));
        } catch (IllegalArgumentException e){
            return LATEST;
        }
    }

    public boolean isOlderThan(@NotNull PluginVersion compareVersion){
        return getOrder() < compareVersion.getOrder();
    }

}
