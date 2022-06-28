package nl.mtvehicles.core.infrastructure.enums;

import java.util.Arrays;
import java.util.List;

/**
 * Custom WorldGuard flags used for more features (if WorldGuard is hooked)
 */
public enum WGFlag {
    GAS_STATION("mtv-gasstation"),
    PLACE("mtv-place"),
    ENTER("mtv-enter"),
    PICKUP("mtv-pickup"),
    USE_CAR("mtv-use-car"),
    USE_HOVER("mtv-use-hover"),
    USE_TANK("mtv-use-tank"),
    USE_HELICOPTER("mtv-use-helicopter"),
    USE_AIRPLANE("mtv-use-airplane");

    private String key;

    private WGFlag(String key){
        this.key = key;
    }

    /**
     * Get the custom flag's key (e.g. "mtv-place")
     */
    public String getKey(){
        return key;
    }

    /**
     * Get list of all custom flags (as enums)
     */
    public static List<WGFlag> getFlagList(){
        return Arrays.asList(WGFlag.values());
    }
}
