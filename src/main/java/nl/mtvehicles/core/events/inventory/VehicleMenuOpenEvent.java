package nl.mtvehicles.core.events.inventory;

import nl.mtvehicles.core.events.interfaces.HasInventory;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.enums.InventoryTitle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.entity.Player;

/**
 * On /vehicle menu open
 */
public class VehicleMenuOpenEvent extends MTVEvent implements IsCancellable, HasInventory {

    /**
     * Default constructor with a player (calls {@link MTVEvent#setPlayer(Player)}).
     * @param player Player
     */
    public VehicleMenuOpenEvent(Player player){
        setPlayer(player);
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public InventoryTitle getTitle() {
        return InventoryTitle.VEHICLE_MENU;
    }

}
