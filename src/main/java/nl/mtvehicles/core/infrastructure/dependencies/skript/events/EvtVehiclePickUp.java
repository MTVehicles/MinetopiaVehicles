package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehiclePickUpEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see VehiclePickUpEvent
 */
@Name("Vehicle Pick Up Event")
@Description("Called when a vehicle is picked up")
@Examples({
        "on vehicle pick up:",
        "set {_player} to event-player",
        "set {_licensePlate} to event-text"
})
public class EvtVehiclePickUp extends SkriptEvent {

    static {
        Skript.registerEvent(
                "VehiclePickUpEvent",
                EvtVehiclePickUp.class,
                VehiclePickUpEvent.class,
                "[mtv] vehicle pick up");

        EventValues.registerEventValue(VehiclePickUpEvent.class, Player.class, new Getter<Player, VehiclePickUpEvent>() {
            @Override
            public Player get(VehiclePickUpEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(VehiclePickUpEvent.class, String.class, new Getter<String, VehiclePickUpEvent>() {
            @Override
            public String get(VehiclePickUpEvent event) {
                return event.getLicensePlate();
            }
        }, 0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Literal<?>[] args, int matchedPattern, SkriptParser.ParseResult parseResult) {
        return true;
    }

    @Override
    public boolean check(Event e) {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean debug) {
        return "Vehicle pick up event";
    }

}
