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
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see TankShootEvent
 */
@Name("Tank Shoot Event")
@Description("Called when a tank shoots")
@Examples({
        "on tank shoot:",
        "set {_driver} to event-player",
        "set {_licensePlate} to event-text"
})
public class EvtTankShoot extends SkriptEvent {

    static {
        Skript.registerEvent(
                "TankShootEvent",
                EvtTankShoot.class,
                TankShootEvent.class,
                "[mtv] tank shoot");

        EventValues.registerEventValue(TankShootEvent.class, Player.class, new Getter<Player, TankShootEvent>() {
            @Override
            public Player get(TankShootEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(TankShootEvent.class, String.class, new Getter<String, TankShootEvent>() {
            @Override
            public String get(TankShootEvent event) {
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
        return "Tank shoot event";
    }

}
