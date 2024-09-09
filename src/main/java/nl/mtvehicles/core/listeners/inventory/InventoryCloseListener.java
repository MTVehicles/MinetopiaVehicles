package nl.mtvehicles.core.listeners.inventory;

import nl.mtvehicles.core.events.inventory.InventoryCloseEvent;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleData;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * On inventory close
 */
public class InventoryCloseListener extends MTVListener {
    public static HashMap<String, Double> speed = new HashMap<>();

    @EventHandler
    public void onInventoryClose(org.bukkit.event.inventory.InventoryCloseEvent event) {
        this.event = event;
        player = (Player) event.getPlayer();
        String stringTitle = event.getView().getTitle();

        InventoryTitle title = InventoryTitle.getByStringTitle(stringTitle);
        if (title == null) return;

        this.setAPI(new InventoryCloseEvent(title));
        InventoryCloseEvent api = (InventoryCloseEvent) getAPI();
        callAPI();

        switch (title) {
            case VEHICLE_TRUNK:
                handleVehicleTrunk(event);
                break;

            case CHOOSE_LANGUAGE_MENU:
                handleLanguageMenu();
                break;

            case VOUCHER_REDEEM_MENU:
                handleVoucherRedeemMenu();
                break;
        }
    }

    private void handleVehicleTrunk(org.bukkit.event.inventory.InventoryCloseEvent event) {
        String license = VehicleUtils.openedTrunk.remove(player);
        VehicleData.trunkViewerRemove(license, player);

        List<ItemStack> chest = Arrays.asList(event.getInventory().getContents());
        ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + license + ".kofferbakData", chest);
        ConfigModule.vehicleDataConfig.save();
    }

    private void handleLanguageMenu() {
        if (LanguageUtils.languageCheck.getOrDefault(player.getUniqueId(), false)) {
            player.sendMessage(TextUtils.colorize("&cThe language settings have not changed because the menu is closed. Do you want to change this anyway? Execute /vehicle language"));
        }
    }

    private void handleVoucherRedeemMenu() {
        VehicleVoucherListener.voucher.remove(player);
    }
}
