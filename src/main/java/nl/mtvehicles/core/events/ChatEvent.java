package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

/**
 * On player chat
 */
public class ChatEvent extends MTVEvent implements IsCancellable, Cancellable {
    private String message;

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Get the message that player has written
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message that player has written to something else
     * @param message New message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
