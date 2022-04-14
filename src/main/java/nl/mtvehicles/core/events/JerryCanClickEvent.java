package nl.mtvehicles.core.events;

import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

@ToDo(comment = "More methods.")
public class JerryCanClickEvent extends MTVEvent implements Cancellable {

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
