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
import nl.mtvehicles.core.events.TankShootEvent;
import nl.mtvehicles.core.events.VehiclePlaceEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see VehiclePlaceEvent
 */
@Name("Vehicle Place Event")
@Description("Called when a vehicle is placed")
@Examples({
        "on vehicle place:",
        "set {_driver} to event-player",
        "set {_licensePlate} to event-text",
        "set {_placeLocation} to event-location"
})
public class EvtVehiclePlace extends SkriptEvent {

    static {
        Skript.registerEvent(
                "VehiclePlaceEvent",
                EvtVehiclePlace.class,
                VehiclePlaceEvent.class,
                "[mtv] vehicle place");

        EventValues.registerEventValue(VehiclePlaceEvent.class, Player.class, new Getter<Player, VehiclePlaceEvent>() {
            @Override
            public Player get(VehiclePlaceEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(VehiclePlaceEvent.class, String.class, new Getter<String, VehiclePlaceEvent>() {
            @Override
            public String get(VehiclePlaceEvent event) {
                return event.getLicensePlate();
            }
        }, 0);

        EventValues.registerEventValue(VehiclePlaceEvent.class, Location.class, new Getter<Location, VehiclePlaceEvent>() {
            @Override
            public Location get(VehiclePlaceEvent event) {
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
        return "Vehicle place event";
    }

}
