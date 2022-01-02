package nl.mtvehicles.core.infrastructure.enums;

public enum RegionAction {
    PLACE,
    PICKUP,
    ENTER;

    public enum ListType {
        DISABLED,
        WHITELIST,
        BLACKLIST;
    }
}
