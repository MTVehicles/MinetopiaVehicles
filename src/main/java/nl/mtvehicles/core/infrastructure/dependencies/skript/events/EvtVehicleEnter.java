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
import nl.mtvehicles.core.events.VehicleEnterEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see VehicleEnterEvent
 */
@Name("Vehicle Enter Event")
@Description("Called when a vehicle is entered")
@Examples({
        "on mtv vehicle enter:",
        "set {_player} to event-player",
        "set {_licensePlate} to event-text",
        "set {_vehicleLocation} to event-location"
})
public class EvtVehicleEnter extends SkriptEvent {

    static {
        Skript.registerEvent(
                "VehicleEnterEvent",
                EvtVehicleEnter.class,
                VehicleEnterEvent.class,
                "[mtv] vehicle enter");

        EventValues.registerEventValue(VehicleEnterEvent.class, Player.class, new Getter<Player, VehicleEnterEvent>() {
            @Override
            public Player get(VehicleEnterEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(VehicleEnterEvent.class, String.class, new Getter<String, VehicleEnterEvent>() {
            @Override
            public String get(VehicleEnterEvent event) {
                return event.getLicensePlate();
            }
        }, 0);

        EventValues.registerEventValue(VehicleEnterEvent.class, Location.class, new Getter<Location, VehicleEnterEvent>() {
            @Override
            public Location get(VehicleEnterEvent event) {
                return event.getLocation();
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
        return "Vehicle enter event";
    }

}
