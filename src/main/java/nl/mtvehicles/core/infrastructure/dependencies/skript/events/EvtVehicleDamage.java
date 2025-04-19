package nl.mtvehicles.core.infrastructure.dependencies.skript.events;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import nl.mtvehicles.core.events.VehicleDamageEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see nl.mtvehicles.core.events.VehicleDamageEvent
 */
@Name("Vehicle Damage Event")
@Description("Called when a vehicle is damaged")
@Examples({
        "on mtv vehicle damage:",
        "set {_damager} to event-entity",
        "set {_licensePlate} to event-text",
        "set {_damage} to event-number"
})
@Since("2.5.6")
public class EvtVehicleDamage extends SkriptEvent {

    static {
        Skript.registerEvent(
                "VehicleDamageEvent",
                EvtVehicleDamage.class,
                VehicleDamageEvent.class,
                "[mtv] vehicle damage");

        EventValues.registerEventValue(VehicleDamageEvent.class, Entity.class, new Getter<Entity, VehicleDamageEvent>() {
            @Override
            public Entity get(VehicleDamageEvent event) {
                return event.getDamager();
            }
        }, 0);

        EventValues.registerEventValue(VehicleDamageEvent.class, String.class, new Getter<String, VehicleDamageEvent>() {
            @Override
            public String get(VehicleDamageEvent event) {
                return event.getLicensePlate();
            }
        }, 0);

        EventValues.registerEventValue(VehicleDamageEvent.class, Double.class, new Getter<Double, VehicleDamageEvent>() {
            @Override
            public Double get(VehicleDamageEvent event) {
                return event.getDamage();
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
        return "Vehicle damage event";
    }

}
