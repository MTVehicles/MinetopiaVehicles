package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

public class InventoryCloseEvent extends MTVEvent implements HasInventory {
    private InventoryTitle title;

    public InventoryTitle getTitle() {
        return title;
    }

    public void setTitle(InventoryTitle title) {
        this.title = title;
    }

}
