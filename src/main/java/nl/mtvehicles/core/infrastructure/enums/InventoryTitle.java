package nl.mtvehicles.core.infrastructure.enums;

import nl.mtvehicles.core.infrastructure.modules.ConfigModule;

import java.util.Locale;
import java.util.Objects;

public enum InventoryTitle {
    VEHICLE_MENU,
    CHOOSE_VEHICLE_MENU,
    CHOOSE_LANGUAGE_MENU,
    CONFIRM_VEHICLE_MENU,
    VEHICLE_RESTORE_MENU,
    VEHICLE_EDIT_MENU,
    VEHICLE_SETTINGS_MENU,
    VEHICLE_FUEL_MENU,
    VEHICLE_TRUNK_MENU,
    VEHICLE_MEMBERS_MENU,
    VEHICLE_SPEED_MENU,
    JERRYCAN_MENU,
    VOUCHER_REDEEM_MENU,
    VEHICLE_TRUNK;

    /**
     * Get the title which is displayed on top of an inventory
     * @return Displayed title
     */
    public String getStringTitle(){
        return ConfigModule.messagesConfig.getMessage(Message.valueOf(this.toString().toUpperCase(Locale.ROOT)));
    }

    /**
     * Get this enum from the string title displayed on top of an inventory
     * @param stringTitle Displayed title
     * @return InventoryTitle enum
     */
    public static InventoryTitle getByStringTitle(String stringTitle){
        for (InventoryTitle title: InventoryTitle.values()) {
            if (Objects.equals(stringTitle, title.getStringTitle())) return title;
        }
        return null;
    }
}
