package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

/**
 * On right click with a voucher
 */
public class VehicleVoucherEvent extends MTVEvent implements IsCancellable, Cancellable {
    private String voucherUUID;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get UUID of the car that will be given from the clicked voucher
     * @return Car UUID
     */
    public String getVoucherUUID(){
        return voucherUUID;
    }

    /**
     * Set a new UUID of the car that will be given from the clicked voucher
     * @param voucherUUID New car UUID
     */
    public void setVoucherUUID(String voucherUUID) {
        this.voucherUUID = voucherUUID;
    }
}
