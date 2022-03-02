package nl.mtvehicles.core.infrastructure.modules;

import lombok.Getter;
import lombok.Setter;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.events.*;
import nl.mtvehicles.core.events.inventory.InventoryClickEvent;
import nl.mtvehicles.core.events.inventory.InventoryCloseEvent;

public class ListenersModule {
    private static @Getter
    @Setter
    ListenersModule instance;

    public ListenersModule() {
        Main.instance.registerListener(new InventoryClickEvent());
        Main.instance.registerListener(new VehiclePlaceEvent());
        Main.instance.registerListener(new VehicleClickEvent());
        Main.instance.registerListener(new VehicleLeaveEvent());
        Main.instance.registerListener(new ChatEvent());
        Main.instance.registerListener(new VehicleEntityEvent());
        Main.instance.registerListener(new JoinEvent());
        Main.instance.registerListener(new VehicleVoucherEvent());
        Main.instance.registerListener(new InventoryCloseEvent());
        Main.instance.registerListener(new JerryCanClickEvent());
    }
}
