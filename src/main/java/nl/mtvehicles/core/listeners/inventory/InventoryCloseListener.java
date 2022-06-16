package nl.mtvehicles.core.listeners.inventory;

import nl.mtvehicles.core.events.inventory.InventoryCloseEvent;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.utils.LanguageUtils;
import nl.mtvehicles.core.infrastructure.utils.TextUtils;
import nl.mtvehicles.core.infrastructure.models.MTVListener;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import nl.mtvehicles.core.infrastructure.modules.ConfigModule;
import nl.mtvehicles.core.listeners.VehicleVoucherListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
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

        if (InventoryTitle.getByStringTitle(stringTitle) == null) return;

        this.setAPI(new InventoryCloseEvent(InventoryTitle.getByStringTitle(stringTitle)));
        InventoryCloseEvent api = (InventoryCloseEvent) getAPI();
        callAPI();

        InventoryTitle title = api.getTitle();

        if (title.equals(InventoryTitle.VEHICLE_TRUNK)) {
            String license = VehicleUtils.openedTrunk.get(player);
            VehicleUtils.openedTrunk.remove(player);
            List<ItemStack> chest = new ArrayList<>();
            chest.addAll(Arrays.asList(event.getInventory().getContents()));
            ConfigModule.vehicleDataConfig.getConfig().set("vehicle." + license + ".kofferbakData", chest);
            ConfigModule.vehicleDataConfig.save();
        }

        if (title.equals(InventoryTitle.CHOOSE_LANGUAGE_MENU)) {
            if (LanguageUtils.languageCheck.get(player.getUniqueId())) {
                player.sendMessage(TextUtils.colorize("&cThe language settings have not changed because the menu is closed. Do you want to change this anyway? Execute /vehicle language"));
            }
        }

        if (title.equals(InventoryTitle.VOUCHER_REDEEM_MENU)) {
            if (VehicleVoucherListener.voucher.get(player) != null) VehicleVoucherListener.voucher.remove(player);
        }
    }
}
