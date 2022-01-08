package nl.mtvehicles.core.infrastructure.enums;

public enum RegionAction {
    PLACE,
    PICKUP,
    ENTER;

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
