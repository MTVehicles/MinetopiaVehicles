package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class VehicleVoucherEvent extends MTVEvent implements Cancellable {
    private String voucherUUID;

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
