package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasJerryCan;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;

/**
 * On right click with a jerry can - its refuelling (the gas stations feature)
 */
public class JerryCanClickEvent extends MTVEvent implements IsCancellable, HasJerryCan {
    final private int jerryCanFuel;
    final private int jerryCanSize;

    public JerryCanClickEvent(int jerryCanFuel, int jerryCanSize){
        this.jerryCanFuel = jerryCanFuel;
        this.jerryCanSize = jerryCanSize;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public int getJerryCanFuel() {
        return jerryCanFuel;
    }

    @Override
    public int getJerryCanSize() {
        return jerryCanSize;
    }

}
