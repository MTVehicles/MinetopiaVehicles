package nl.mtvehicles.core.infrastructure.enums;

import java.util.Locale;

/**
 * Configurable messages in MTV
 */
public enum Message {
    HELP_INFO,
    HELP_HELP,
    HELP_PUBLIC,
    HELP_PRIVATE,
    HELP_ADD_RIDER,
    HELP_REMOVE_RIDER,
    HELP_ADD_MEMBER,
    HELP_REMOVE_MEMBER,
    HELP_ADMIN,
    ADMIN_LANGUAGE,
    ADMIN_VERSION,
    ADMIN_EDIT,
    ADMIN_MENU,
    ADMIN_RESTORE,
    ADMIN_FUEL("adminBenzine"),
    ADMIN_RELOAD,
    ADMIN_GIVEVOUCHER,
    ADMIN_GIVECAR,
    ADMIN_SETOWNER,
    ADMIN_UPDATE,
    ADMIN_DELETE,
    RELOAD_SUCCESSFUL("reloadSuccesvol"),
    NOT_FOR_CONSOLE,
    COMMAND_DOES_NOT_EXIST("cmdNotExists"),
    MENU_OPEN,
    NO_PERMISSION("noPerms"),
    NOT_YOUR_CAR,
    COMPLETED_VEHICLE_GIVE("completedvehiclegive"),
    WRONG_HAND,
    ACTION_SUCCESSFUL,
    ACTION_CANCELLED("actionCanceled"),
    ACTION_FAILED_DUP_LICENSE,
    TYPE_LICENSE_IN_CHAT,
    TYPE_NAME_IN_CHAT,
    TYPE_NEW_BENZINE_IN_CHAT,
    TYPE_NEW_ROWS_IN_CHAT,
    TYPE_SPEED_IN_CHAT,
    VEHICLE_PLACE,
    VEHICLE_NO_RIDER_ENTER,
    VEHICLE_NO_OWNER_PICKUP,
    VEHICLE_NO_RIDER_TRUNK("vehicleNoRiderKofferbak"),
    VEHICLE_PICKUP,
    VEHICLE_ENTER_RIDER,
    VEHICLE_ENTER_MEMBER,
    MEMBER_CHANGE,
    PLAYER_NOT_FOUND,
    VEHICLE_NOT_FOUND,
    NO_VEHICLE_IN_HAND,
    VEHICLE_IN_WATER,
    VEHICLE_FULL,
    NO_FUEL,
    JERRYCAN_FULL,
    USE_SET_OWNER,
    USE_ADD_MEMBER,
    USE_REMOVE_MEMBER,
    USE_ADD_RIDER,
    USE_REMOVE_RIDER,
    USE_GIVE_CAR,
    USE_GIVE_VOUCHER,
    VOUCHER_REDEEM,
    UPDATE_SUCCESSFUL("updatedSucces2"),
    UPDATE_FAILED,
    UPDATE_DISABLED,
    INVENTORY_FULL,
    VEHICLE_DELETED,
    VEHICLE_ALREADY_DELETED,
    BOSSBAR_FUEL,
    RELOAD_IN_VEHICLE,
    BLOCK_NOT_IN_WHITELIST,
    GIVE_CAR_NOT_FOUND,
    GIVE_CAR_SUCCESS("giveCarSucces"),
    GIVE_VOUCHER_SUCCESS("giveVoucherSucces"),
    VEHICLE_INFO_INFORMATION,
    VEHICLE_INFO_TYPE,
    VEHICLE_INFO_NAME,
    VEHICLE_INFO_LICENSE,
    VEHICLE_INFO_UUID("vehicleInfoUUID"),
    VEHICLE_INFO_SPEED,
    VEHICLE_INFO_ACCELERATION,
    VEHICLE_INFO_OWNER,
    VEHICLE_INFO_RIDERS_NONE("vehicleInfoRiders"),
    VEHICLE_INFO_RIDERS("vehicleInfoRiders2"),
    VEHICLE_INFO_MEMBERS_NONE("vehicleInfoMembers"),
    VEHICLE_INFO_MEMBERS("vehicleInfoMembers2"),
    VEHICLE_INFO_NO_MEMBERS,
    LANGUAGE_HAS_CHANGED,
    USING_PRE_RELEASE,
    NOT_IN_A_GAS_STATION,
    CANNOT_DO_THAT_HERE,
    INSUFFICIENT_FUNDS,
    TRANSACTION_SUCCESSFUL,
    CONFIRM,
    CANCEL,
    CANCEL_ACTION,
    CANCEL_VOUCHER,
    CONFIRM_ACTION,
    CONFIRM_VOUCHER,
    CONFIRM_VEHICLE_MENU,
    VOUCHER_DESCRIPTION,
    VOUCHER_VALIDITY,
    INVALID_INPUT,
    TOO_MANY_VEHICLES,
    REPAIR_SUCCESSFUL,
    REFILL_SUCCESSFUL,
    VEHICLE_SETTINGS,
    FUEL_SETTINGS,
    TRUNK_SETTINGS,
    MEMBER_SETTINGS,
    SPEED_SETTINGS,
    DELETE_VEHICLE,
    DELETE_WARNING_LORE,
    CLOSE,
    CLOSE_DESCRIPTION,
    BACK,
    BACK_DESCRIPTION;

    final private String key;

    private Message(){
        String key = this.toString().toLowerCase(Locale.ROOT);
        while (key.contains("_")) {
            key = key.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(key.charAt(key.indexOf("_") + 1))));
        }
        this.key = key;
    }

    private Message(String key){
        this.key = key;
    }

    /**
     * Get string key of message
     * @return Message key
     */
    public String getKey() {
        return key;
    }
}