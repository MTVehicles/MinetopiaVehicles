package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehicleRegionLeaveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see VehicleRegionLeaveEvent
 */
public class EvtVehicleRegionLeave extends SkriptEvent {

    static {
        Skript.registerEvent(
                "VehicleRegionLeave",
                EvtVehicleRegionLeave.class,
                VehicleRegionLeaveEvent.class,
                "vehicle region leave");

        EventValues.registerEventValue(VehicleRegionLeaveEvent.class, Player.class, new Getter<Player, VehicleRegionLeaveEvent>() {
            @Override
            public Player get(VehicleRegionLeaveEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(VehicleRegionLeaveEvent.class, String.class, new Getter<String, VehicleRegionLeaveEvent>() {
            @Override
            public String get(VehicleRegionLeaveEvent event) {
                return event.getRegionName();
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
        return "Vehicle region enter event";
    }

}
