package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.modules.VersionModule;
import org.jetbrains.annotations.NotNull;

public enum PluginVersion {
    LEGACY,
    v2_2_0,
    v2_2_1_pre1,
    v2_2_1_pre2,
    v2_2_1,
    v2_3_0_pre1,
    v2_3_0_pre2,
    v2_3_0_rc1,
    v2_3_0_rc2,
    v2_3_0,
    v2_4_0_pre1,
    v2_4_0_rc1,
    v2_4_0,
    v2_4_1,
    LATEST,
    DEV;

    public int getOrder(){
        return ordinal();
    }

    public boolean isDev(){
        return this.equals(DEV);
    }

    public static PluginVersion getPluginVersion(){
        return getVersion(VersionModule.pluginVersion);
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
