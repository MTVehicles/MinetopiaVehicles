package nl.mtvehicles.core.infrastructure.enums;

import java.util.Locale;

/**
 * Configurable messages in MTV
 */
public enum Message {
    HELP_INFO("helpInfo"),
    HELP_HELP("helpHelp"),
    HELP_PUBLIC("helpPublic"),
    HELP_PRIVATE("helpPrivate"),
    HELP_ADD_RIDER("helpAddRider"),
    HELP_REMOVE_RIDER("helpRemoveRider"),
    HELP_ADD_MEMBER("helpAddMember"),
    HELP_REMOVE_MEMBER("helpRemoveMember"),
    HELP_ADMIN("helpAdmin"),
    ADMIN_LANGUAGE("adminLanguage"),
    ADMIN_VERSION("adminVersion"),
    ADMIN_EDIT("adminEdit"),
    ADMIN_MENU("adminMenu"),
    ADMIN_RESTORE("adminRestore"),
    ADMIN_FUEL("adminBenzine"),
    ADMIN_RELOAD("adminReload"),
    ADMIN_GIVEVOUCHER("adminGiveVoucher"),
    ADMIN_GIVECAR("adminGiveCar"),
    ADMIN_SETOWNER("adminSetOwner"),
    ADMIN_UPDATE("adminUpdate"),
    ADMIN_DELETE("adminDelete"),
    ADMIN_REFILL("adminRefill"),
    ADMIN_REPAIR("adminRepair"),
    ADMIN_GIVEFUEL("adminGiveFuel"),
    RELOAD_SUCCESSFUL("reloadSuccesvol"),
    NOT_FOR_CONSOLE("notForConsole"),
    COMMAND_DOES_NOT_EXIST("cmdNotExists"),
    MENU_OPEN("menuOpen"),
    NO_PERMISSION("noPerms"),
    NOT_YOUR_CAR("notYourCar"),
    COMPLETED_VEHICLE_GIVE("completedvehiclegive"),
    WRONG_HAND("wrongHand"),
    ACTION_SUCCESSFUL("actionSuccessful"),
    ACTION_CANCELLED("actionCanceled"),
    ACTION_FAILED_DUP_LICENSE("actionFailedDupLicense"),
    TYPE_LICENSE_IN_CHAT("typeLicenseInChat"),
    TYPE_NAME_IN_CHAT("typeNameInChat"),
    TYPE_NEW_BENZINE_IN_CHAT("typeNewBenzineInChat"),
    TYPE_NEW_ROWS_IN_CHAT("typeNewRowsInChat"),
    TYPE_SPEED_IN_CHAT("typeSpeedInChat"),
    VEHICLE_PLACE("vehiclePlace"),
    VEHICLE_ERROR("vehicleError", "&cAn error occurred with your vehicle. Please contact an administrator."),
    VEHICLE_NO_RIDER_ENTER("vehicleNoRiderEnter"),
    VEHICLE_NO_OWNER_PICKUP("vehicleNoOwnerPickup"),
    VEHICLE_NO_RIDER_TRUNK("vehicleNoRiderKofferbak"),
    VEHICLE_PICKUP("vehiclePickup"),
    VEHICLE_ENTER_RIDER("vehicleEnterRider"),
    VEHICLE_ENTER_MEMBER("vehicleEnterMember"),
    MEMBER_CHANGE("memberChange"),
    PLAYER_NOT_FOUND("playerNotFound"),
    VEHICLE_NOT_FOUND("vehicleNotFound"),
    NO_VEHICLE_IN_HAND("noVehicleInHand"),
    VEHICLE_IN_WATER("vehicleInWater"),
    VEHICLE_FULL("vehicleFull"),
    NO_FUEL("noFuel"),
    JERRYCAN_FULL("jerrycanFull"),
    USE_SET_OWNER("useSetOwner"),
    USE_ADD_MEMBER("useAddMember"),
    USE_REMOVE_MEMBER("useRemoveMember"),
    USE_ADD_RIDER("useAddRider"),
    USE_REMOVE_RIDER("useRemoveRider"),
    USE_GIVE_CAR("useGiveCar"),
    USE_GIVE_VOUCHER("useGiveVoucher"),
    VOUCHER_REDEEM("voucherRedeem"),
    UPDATE_SUCCESSFUL("updatedSucces2"),
    UPDATE_FAILED("updateFailed"),
    UPDATE_DISABLED("updateDisabled"),
    INVENTORY_FULL("inventoryFull"),
    VEHICLE_DELETED("vehicleDeleted"),
    VEHICLE_ALREADY_DELETED("vehicleAlreadyDeleted"),
    BOSSBAR_FUEL("bossbarFuel"),
    RELOAD_IN_VEHICLE("reloadInVehicle"),
    BLOCK_NOT_IN_WHITELIST("blockNotInWhitelist"),
    GIVE_CAR_NOT_FOUND("giveCarNotFound"),
    GIVE_CAR_SUCCESS("giveCarSucces"),
    GIVE_VOUCHER_SUCCESS("giveVoucherSucces"),
    VEHICLE_INFO_INFORMATION("vehicleInfoInformation"),
    VEHICLE_INFO_TYPE("vehicleInfoType"),
    VEHICLE_INFO_NAME("vehicleInfoName"),
    VEHICLE_INFO_LICENSE("vehicleInfoLicense"),
    VEHICLE_INFO_UUID("vehicleInfoUUID"),
    VEHICLE_INFO_PRICE("vehicleInfoPrice"),
    VEHICLE_INFO_SPEED("vehicleInfoSpeed"),
    VEHICLE_INFO_ACCELERATION("vehicleInfoAcceleration"),
    VEHICLE_INFO_OWNER("vehicleInfoOwner"),
    VEHICLE_INFO_RIDERS_NONE("vehicleInfoRiders"),
    VEHICLE_INFO_RIDERS("vehicleInfoRiders2"),
    VEHICLE_INFO_MEMBERS_NONE("vehicleInfoMembers"),
    VEHICLE_INFO_MEMBERS("vehicleInfoMembers2"),
    @Deprecated
    VEHICLE_INFO_NO_MEMBERS("vehicleInfoNoMembers"),
    LANGUAGE_HAS_CHANGED("languageHasChanged"),
    USING_PRE_RELEASE("usingPreRelease"),
    NOT_IN_A_GAS_STATION("notInAGasStation"),
    CANNOT_DO_THAT_HERE("cannotDoThatHere"),
    INSUFFICIENT_FUNDS("insufficientFunds"),
    TRANSACTION_SUCCESSFUL("transactionSuccessful"),
    CONFIRM("confirm"),
    CANCEL("cancel"),
    CANCEL_ACTION("cancelAction"),
    CANCEL_VOUCHER("cancelVoucher"),
    CONFIRM_ACTION("confirmAction"),
    CONFIRM_VOUCHER("confirmVoucher"),
    CONFIRM_VEHICLE_GIVE("confirmVehicleGive"),
    VOUCHER_DESCRIPTION("voucherDescription"),
    VOUCHER_VALIDITY("voucherValidity"),
    INVALID_INPUT("invalidInput"),
    TOO_MANY_VEHICLES("tooManyVehicles"),
    REPAIR_SUCCESSFUL("repairSuccessful"),
    REFILL_SUCCESSFUL("refillSuccessful"),
    VEHICLE_SETTINGS("vehicleSettings"),
    FUEL_SETTINGS("fuelSettings"),
    TRUNK_SETTINGS("trunkSettings"),
    MEMBER_SETTINGS("memberSettings"),
    SPEED_SETTINGS("speedSettings"),
    DELETE_VEHICLE("deleteVehicle"),
    DELETE_WARNING_LORE("deleteWarningLore"),
    CLOSE("close"),
    CLOSE_DESCRIPTION("closeDescription"),
    BACK("back"),
    BACK_DESCRIPTION("backDescription"),
    CURRENTLY("currently"),
    ACCELERATION_SPEED("accelerationSpeed"),
    MAX_SPEED("maxSpeed"),
    MAX_SPEED_BACKWARDS("maxSpeedBackwards"),
    BRAKING_SPEED("brakingSpeed"),
    FRICTION_SPEED("frictionSpeed"),
    ROTATION_SPEED("rotationSpeed"),
    OWNER("owner"),
    RIDERS("riders"),
    MEMBERS("members"),
    NAME("name"),
    NEXT_PAGE("nextPage"),
    PREVIOUS_PAGE("previousPage"),
    EDIT_NAME("editName"),
    EDIT_LICENSE_PLATE("editLicensePlate"),
    TOGGLE_GLOW("toggleGlow"),
    TURNED_ON("turnedOn"),
    TURNED_OFF("turnedOff"),
    TOGGLE_FUEL("toggleFuel"),
    FUEL_USAGE("fuelUsage"),
    CURRENT_FUEL("currentFuel"),
    TOGGLE_TRUNK("toggleTrunk"),
    EDIT_TRUNK_ROWS("editTrunkRows"),
    OPEN_TRUNK("openTrunk"),
    CLICK_TO_OPEN("clickToOpen"),
    VEHICLE_MENU("vehicleMenu"),
    CHOOSE_VEHICLE_MENU("chooseVehicleMenu"),
    CHOOSE_LANGUAGE_MENU("chooseLanguageMenu"),
    CONFIRM_VEHICLE_MENU("confirmVehicleMenu"),
    VEHICLE_RESTORE_MENU("vehicleRestoreMenu"),
    VEHICLE_EDIT_MENU("vehicleEditMenu"),
    VEHICLE_SETTINGS_MENU("vehicleSettingsMenu"),
    VEHICLE_FUEL_MENU("vehicleFuelMenu"),
    VEHICLE_TRUNK_MENU("vehicleTrunkMenu"),
    VEHICLE_MEMBERS_MENU("vehicleMembersMenu"),
    VEHICLE_SPEED_MENU("vehicleSpeedMenu"),
    JERRYCAN_MENU("jerrycanMenu"),
    VOUCHER_REDEEM_MENU("voucherRedeemMenu"),
    VEHICLE_TRUNK("vehicleTrunk"),
    JERRYCAN("jerrycan"),
    COMMAND_NO_VEHICLE("commandNoVehicle"),
    ALREADY_MEMBER("alreadyMember"),
    ALREADY_RIDER("alreadyRider"),
    NOT_A_MEMBER("notAMember"),
    NOT_A_RIDER("notARider"),
    OFFLINE_PLAYER_NOT_FOUND("offlinePlayerNotFound"),
    ECONOMY_NOT_SET_UP("economyNotSetUp"),
    USE_BUY_CAR("useBuyCar"),
    USE_BUY_VOUCHER("useBuyVoucher"),
    USE_GIVE_FUEL("useGiveFuel"),
    GIVE_FUEL_SUCCESS("giveFuelSuccess"),
    USE_VEHICLE_DESPAWN("useVehicleDespawn"),
    NO_VEHICLE_TO_DESPAWN("noVehicleToDespawn"),
    VEHICLE_DESPAWNED("vehicleDespawned"),
    USE_EDIT("useEdit"),
    FUEL_TOO_HIGH("fuelTooHigh"),
    MUST_BE_INTEGER("mustBeInteger"),
    MUST_BE_DOUBLE("mustBeDouble"),
    MUST_BE_BOOLEAN("mustBeBoolean");

    private final String key;
    private final String defaultMessage;

    Message() {
        String key = this.toString().toLowerCase(Locale.ROOT);
        while (key.contains("_")) {
            key = key.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(key.charAt(key.indexOf("_") + 1))));
        }
        this.key = key;
        this.defaultMessage = "";
    }

    Message(String key) {
        this.key = key;
        this.defaultMessage = "";
    }

    Message(String key, String defaultMessage) {
        this.key = key;
        this.defaultMessage = defaultMessage;
    }

    /**
     * Get string key of message
     * @return Message key
     */
    public String getKey() {
        return key;
    }

    /**
     * Get default message text
     * @return Default message text
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }
}