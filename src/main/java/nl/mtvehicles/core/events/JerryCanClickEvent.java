package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class JerryCanClickEvent extends MTVEvent implements Cancellable {

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
