package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.utils.ItemUtils;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Name("Give an MTV vehicle")
@Description("Give a vehicle to a player")
@Examples({
        "give mtv vehicle {_boat} to player",
        "give {_player} mtv vehicle with license plate \"DF-4J-2R\"",
        "give {_player} mtv vehicle with uuid \"XN9MKB\""
})
public class EffGiveVehicle extends Effect {

    static {
        Skript.registerEffect(EffGiveVehicle.class,
                "give [mtv] vehicle (by|with) license [plate] %string% to %player%",
                "give %player% [mtv] vehicle (by|with) license [plate] %string%",
                "give [mtv] vehicle (by|with) (UUID|uuid) %string% to %player%",
                "give %player% [mtv] vehicle (by|with) (UUID|uuid) %string%",
                "give [mtv] vehicle %object% to %player%",
                "give %player% [mtv] vehicle %object%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Object> vehicle;

    @SuppressWarnings("null")
    private Expression<String> text;

    @SuppressWarnings("null")
    private Expression<Player> player;

    private int pattern;

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        if (matchedPattern == 0) {
            this.text = (Expression<String>) expressions[0];
            this.player = (Expression<Player>) expressions[1];
            pattern = 2;
        } else if (matchedPattern == 1) {
            this.text = (Expression<String>) expressions[1];
            this.player = (Expression<Player>) expressions[0];
            pattern = 2;
        } else if (matchedPattern == 2) {
            this.text = (Expression<String>) expressions[0];
            this.player = (Expression<Player>) expressions[1];
            pattern = 3;
        } else if (matchedPattern == 3) {
            this.text = (Expression<String>) expressions[1];
            this.player = (Expression<Player>) expressions[0];
            pattern = 3;
        } else if (matchedPattern == 4) {
            this.vehicle = (Expression<Object>) expressions[0];
            this.player = (Expression<Player>) expressions[1];
            pattern = 1;
        } else {
            this.vehicle = (Expression<Object>) expressions[1];
            this.player = (Expression<Player>) expressions[0];
            pattern = 1;
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Give a vehicle to player %s",
                this.player.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {
        if (pattern == 1) {
            if (!(vehicle.getSingle(event) instanceof Vehicle) || vehicle.getSingle(event) == null) {
                Main.logSevere("Skript error: Provided variable is not a vehicle (\"give [mtv] vehicle %vehicle% to %player%\").");
                return;
            }

            player.getSingle(event).getInventory().addItem(
                    ItemUtils.getVehicleItem(((Vehicle) vehicle.getSingle(event)).getLicensePlate())
            );
        } else if (pattern == 2) {
            player.getSingle(event).getInventory().addItem(
                    ItemUtils.getVehicleItem(text.getSingle(event))
            );
        } else {
            if (!VehicleUtils.vehicleUUIDExists(text.getSingle(event))) {
                Main.logSevere("Skript error: Provided UUID does not exist (\"give %player% [mtv] vehicle (by|with) (UUID|uuid) %string%\").");
                return;
            }
            player.getSingle(event).getInventory().addItem(
                    Objects.requireNonNull(VehicleUtils.createAndGetItemByUUID(player.getSingle(event), text.getSingle(event)))
            );
        }
    }
}
