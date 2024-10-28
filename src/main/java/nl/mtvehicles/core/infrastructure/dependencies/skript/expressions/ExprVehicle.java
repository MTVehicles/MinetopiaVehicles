package nl.mtvehicles.core.infrastructure.dependencies.skript.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import nl.mtvehicles.core.infrastructure.vehicle.VehicleUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

@Name("MTV Vehicle")
@Description("Get the MTV's vehicle instance")
@Examples({
        "set {_car} to a new mtv vehicle with license plate \"DF-4J-2R\"",
        "set {_helicopter} to player's driven mtv vehicle",
        "set {_car} to a new mtv vehicle with UUID \"C4UQZJ\" and owner player"
})
public class ExprVehicle extends SimpleExpression<Vehicle> {

    static {
        Skript.registerExpression(ExprVehicle.class,
                Vehicle.class,
                ExpressionType.SIMPLE,
                "[a[n]] mtv vehicle [with license [plate] %-string%]",
                "%player%'s driven mtv vehicle",
                "[a] new[ly] [created] mtv vehicle (by|with) (uuid|UUID) %string% [and] [(by|with)] owner %offlineplayer%",
                "[a] new[ly] [created] mtv vehicle (by|with) (uuid|UUID) owner %offlineplayer% [and] [(by|with)] (uuid|UUID) %string%"
        );
    }

    @SuppressWarnings("null")
    private Expression<String> licensePlate;
    private Expression<Player> player;
    private Expression<OfflinePlayer> offlinePlayer;
    private Expression<String> uuid;
    private int pattern;

    @Override
    public Class<? extends Vehicle> getReturnType() {
        return Vehicle.class;
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        if (matchedPattern == 0) this.licensePlate = (Expression<String>) expressions[0];
        else if (matchedPattern == 1) this.player = (Expression<Player>) expressions[0];
        else if (matchedPattern == 2) {
            this.uuid = (Expression<String>) expressions[0];
            this.offlinePlayer = (Expression<OfflinePlayer>) expressions[1];
        }
        else if (matchedPattern == 3) {
            this.uuid = (Expression<String>) expressions[1];
            this.offlinePlayer = (Expression<OfflinePlayer>) expressions[0];
        }
        this.pattern = matchedPattern;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return "MTVehicles vehicle";
    }

    @Override
    protected Vehicle[] get(Event event) {
        if (this.pattern == 0) {
            if (this.licensePlate == null) return null;

            return new Vehicle[] {
                    VehicleUtils.getVehicle(licensePlate.getSingle(event))
            };
        }

        else if (this.pattern == 1) {
            return new Vehicle[] {
                    VehicleUtils.getDrivenVehicle(player.getSingle(event))
            };
        }

        else {

            if (!VehicleUtils.vehicleUUIDExists(uuid.getSingle(event))) {
                Main.logSevere("Skript error: Provided UUID does not exist (\"a new mtv vehicle with UUID %string% and owner %offlineplayer%\").");
                return null;
            }

            return new Vehicle[] {
                    VehicleUtils.getVehicle(
                            VehicleUtils.createAndGetItemByUUID(offlinePlayer.getSingle(event), uuid.getSingle(event))
                    )
            };
        }


    }

}
