package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class InventoryClickEvent extends MTVEvent implements Cancellable {

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
