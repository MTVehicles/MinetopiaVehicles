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
import nl.mtvehicles.core.events.HornUseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see HornUseEvent
 */
@Name("Horn Use Event")
@Description("Called when a horn is used")
@Examples({
        "on horn use:",
        "set {_driver} to event-player",
        "set {_licensePlate} to event-text"
})
public class EvtHornUse extends SkriptEvent {

    static {
        Skript.registerEvent(
                "HornUseEvent",
                EvtHornUse.class,
                HornUseEvent.class,
                "[mtv] horn use");

        EventValues.registerEventValue(HornUseEvent.class, Player.class, new Getter<Player, HornUseEvent>() {
            @Override
            public Player get(HornUseEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(HornUseEvent.class, String.class, new Getter<String, HornUseEvent>() {
            @Override
            public String get(HornUseEvent event) {
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
        return "Horn use event";
    }

}
