package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class InventoryClickEvent extends MTVEvent implements Cancellable, HasInventory {
    private int clickedSlot;
    private InventoryTitle title;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get the number of slot player has clicked (starts with 0)
     * @return Number of clicked slot
     */
    public int getClickedSlot() {
        return clickedSlot;
    }

    /**
     * Set the number of slot which will be used by the plugin (slots start with 0)
     * @param clickedSlot New number of clicked slot
     */
    public void setClickedSlot(int clickedSlot) {
        this.clickedSlot = clickedSlot;
    }

    public InventoryTitle getTitle() {
        return title;
    }

    public void setTitle(InventoryTitle title) {
        this.title = title;
    }
}
