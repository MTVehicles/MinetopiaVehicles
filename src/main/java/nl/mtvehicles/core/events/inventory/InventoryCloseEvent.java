package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

/**
 * On inventory close
 */
public class InventoryCloseEvent extends MTVEvent implements HasInventory {
    final private InventoryTitle title;

    /**
     * Default constructor with the event-inventory's title
     * @param title Event-inventory's title
     */
    public InventoryCloseEvent(InventoryTitle title){
        this.title = title;
    }

    @Override
    public InventoryTitle getTitle() {
        return title;
    }

}
