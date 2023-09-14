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
import nl.mtvehicles.core.events.VehicleVoucherEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

/**
 * @see VehicleVoucherEvent
 */
@Name("Vehicle Voucher Use Event")
@Description("Called when a voucher is right clicked")
@Examples({
        "on vehicle voucher use:",
        "set {_player} to event-player",
        "set {_uuid} to event-text"
})
public class EvtVoucherUse extends SkriptEvent {

    static {
        Skript.registerEvent(
                "VehicleVoucherEvent",
                EvtVoucherUse.class,
                VehicleVoucherEvent.class,
                "[mtv] vehicle voucher use");

        EventValues.registerEventValue(VehicleVoucherEvent.class, Player.class, new Getter<Player, VehicleVoucherEvent>() {
            @Override
            public Player get(VehicleVoucherEvent event) {
                return event.getPlayer();
            }
        }, 0);

        EventValues.registerEventValue(VehicleVoucherEvent.class, String.class, new Getter<String, VehicleVoucherEvent>() {
            @Override
            public String get(VehicleVoucherEvent event) {
                return event.getVoucherUUID();
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
        return "Vehicle voucher event";
    }

}
