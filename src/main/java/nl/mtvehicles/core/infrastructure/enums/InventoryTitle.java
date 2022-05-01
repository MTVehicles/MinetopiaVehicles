package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.annotations.ToDo;

@ToDo(comment = "Use this enum in the future to make inventory titles translatable. More info below.")
public enum InventoryTitle {
    VEHICLE_MENU("Vehicle Menu"),
    CHOOSE_VEHICLE_MENU("Choose your vehicle"),
    CHOOSE_LANGUAGE_MENU("Choose your language"),
    CONFIRM_VEHICLE_MENU("Confirm getting vehicle"),
    VEHICLE_RESTORE_MENU("Vehicle Restore"),
    VEHICLE_EDIT_MENU("Vehicle Edit"),
    VEHICLE_SETTINGS_MENU("Vehicle Settings"),
    VEHICLE_FUEL_MENU("Vehicle Benzine"),
    VEHICLE_TRUNK_MENU("Vehicle Kofferbak"),
    VEHICLE_MEMBERS_MENU("Vehicle Members"),
    VEHICLE_SPEED_MENU("Vehicle Speed"),
    JERRYCAN_MENU("Benzine menu"),
    VOUCHER_REDEEM_MENU("Voucher Redeem Menu"),
    VEHICLE_TRUNK("Vehicle's Trunk");

    @ToDo(comment = "This variable will be removed.")
    final String stringTitle;

    @ToDo(comment = "When stringTitle variable is removed, this constructor is no longer necessary.")
    private InventoryTitle(String stringTitle){
        this.stringTitle = stringTitle;
    }

    /**
     * Get the title which is displayed on top of an inventory
     * @return Displayed title
     */
    @ToDo(comment = "This method will get the title from MessagesConfig.")
    public String getStringTitle(){
        return stringTitle;
    }

    /**
     * Get this enum from the string title displayed on top of an inventory
     * @param stringTitle Displayed title
     * @return InventoryTitle enum
     */
    @ToDo(comment = "I suppose that after everything will have been using enums, this method will no longer be necessary. If that is not the case, it'll have to be redone, entirely.")
    public static InventoryTitle getByStringTitle(String stringTitle){
        switch (stringTitle){
            case "Vehicle Menu": return VEHICLE_MENU;
            case "Choose your vehicle": return CHOOSE_VEHICLE_MENU;
            case "Choose your language": return CHOOSE_LANGUAGE_MENU;
            case "Confirm getting vehicle": return CONFIRM_VEHICLE_MENU;
            case "Vehicle Restore": return VEHICLE_RESTORE_MENU;
            case "Vehicle Edit": return VEHICLE_EDIT_MENU;
            case "Vehicle Settings": return VEHICLE_SETTINGS_MENU;
            case "Vehicle Benzine": return VEHICLE_FUEL_MENU;
            case "Vehicle Kofferbak": return VEHICLE_TRUNK_MENU;
            case "Vehicle Members": return VEHICLE_MEMBERS_MENU;
            case "Vehicle Speed": return VEHICLE_SPEED_MENU;
            case "Benzine menu": return JERRYCAN_MENU;
            case "Voucher Redeem Menu": return VOUCHER_REDEEM_MENU;
            case "Vehicle's Trunk": return VEHICLE_TRUNK;
            default: return null;
        }
    }
}
