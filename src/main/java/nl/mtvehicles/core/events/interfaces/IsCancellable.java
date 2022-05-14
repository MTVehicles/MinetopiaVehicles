package nl.mtvehicles.core.events.interfaces;

//Created own interface because older versions do not have the "Cancellable" interface yet

import nl.mtvehicles.core.infrastructure.models.MTVEvent;

/**
 * Interface for all cancellable events.
 * Does not include a 'isCancelled()' method - see {@link MTVEvent#isCancelled()}.
 */
public interface IsCancellable {

    /**
     * Sets the cancellation state of this event. A cancelled event will not
     * be executed in the server, but will still pass to other plugins.
     *
     * @param cancelled true if you wish to cancel this event
     */
    public void setCancelled(boolean cancelled);

}
