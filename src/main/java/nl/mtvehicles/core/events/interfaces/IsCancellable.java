package nl.mtvehicles.core.events.interfaces;

import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

/**
 * Interface for all cancellable events.
 * Does not include a 'isCancelled()' method - see {@link MTVEvent#isCancelled()}.
 *
 * @deprecated Use {@link Cancellable} instead.
 */
@Deprecated
public interface IsCancellable {

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancelled true if you wish to cancel this event
     */
    public void setCancelled(boolean cancelled);

}
