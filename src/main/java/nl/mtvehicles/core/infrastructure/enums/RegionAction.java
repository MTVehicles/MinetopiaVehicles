package nl.mtvehicles.core.infrastructure.enums;

/**
 * Action performed with a vehicle
 */
public enum RegionAction {
    PLACE,
    PICKUP,
    ENTER;

    /**
     * Type of WorldGuard list - a region action may either be whitelisted or blacklisted.
     */
    public enum ListType {
        DISABLED,
        WHITELIST,
        BLACKLIST;

        public boolean isEnabled(){
            return !(this.equals(ListType.DISABLED));
        }

        public boolean isWhitelist(){
            return !(this.equals(ListType.WHITELIST));
        }

        public boolean isBlacklist(){
            return !(this.equals(ListType.BLACKLIST));
        }
    }
}
