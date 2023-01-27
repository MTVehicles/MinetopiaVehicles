package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.listeners.*;
import nl.mtvehicles.core.listeners.inventory.InventoryClickListener;
import nl.mtvehicles.core.listeners.inventory.InventoryCloseListener;

/**
 * Module which registers all listeners used by the plugin
 */
public class ListenersModule {
    private static @Getter
    @Setter
    ListenersModule instance;

    public ListenersModule() {
        Main.instance.registerListener(new InventoryClickListener());
        Main.instance.registerListener(new VehiclePlaceListener());
        Main.instance.registerListener(new VehicleClickListener());
        Main.instance.registerListener(new VehicleLeaveListener());
        Main.instance.registerListener(new ChatListener());
        Main.instance.registerListener(new VehicleEntityListener());
        Main.instance.registerListener(new JoinListener());
        Main.instance.registerListener(new VehicleVoucherListener());
        Main.instance.registerListener(new InventoryCloseListener());
        Main.instance.registerListener(new JerryCanClickListener());
        Main.instance.registerListener(new SignClickListener());
        Main.instance.registerListener(new SignChangeListener());
    }
}
