package nl.mtvehicles.core.infrastructure.dependencies.skript.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import nl.mtvehicles.core.Main;
import nl.mtvehicles.core.infrastructure.vehicle.Vehicle;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Name("Add rider/member to MTV vehicle")
@Description("Add a rider or member to an MTV vehicle.")
@Examples({
        "add {_player} as a rider of the vehicle {_car}",
        "add player {_offlinePlayer} as a member to mtv vehicle {_car}"
})
@Since("2.5.5")
public class EffAddRiderMember extends Effect {

    static {
        Skript.registerEffect(EffAddRiderMember.class,
                "add [player] %offlineplayer% as [a] rider (of|to) [the] [mtv] vehicle %vehicle%",
                "add [player] %offlineplayer% as [a] member (of|to) [the] [mtv] vehicle %vehicle%"
        );
    }

    @SuppressWarnings("null")
    private Expression<Vehicle> vehicle;

    @SuppressWarnings("null")
    private Expression<OfflinePlayer> player;

    private MemberType type;

    private enum MemberType {
        RIDER,
        MEMBER
    }

    @SuppressWarnings({"unchecked", "null"})
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parser) {
        this.player = (Expression<OfflinePlayer>) expressions[0];
        this.vehicle = (Expression<Vehicle>) expressions[1];
        this.type = (matchedPattern == 0) ? MemberType.RIDER : MemberType.MEMBER;
        return true;
    }

    @Override
    public String toString(@Nullable Event event, boolean debug) {
        return String.format("Add offline player %s as a rider/member to an mtv vehicle.",
                this.player.toString(event, debug)
        );
    }

    @Override
    protected void execute(Event event) {
        Vehicle vehicle = this.vehicle.getSingle(event);
        String playerUUID = this.player.getSingle(event).getUniqueId().toString();

        if (type.equals(MemberType.RIDER)) {
            List<String> riders = vehicle.getRiders();
            if (riders.contains(playerUUID)) return;

            riders.add(playerUUID);
            vehicle.setRiders(riders);
        } else {
            List<String> members = vehicle.getMembers();
            if (members.contains(playerUUID)) return;

            members.add(playerUUID);
            vehicle.setMembers(members);
        }
        vehicle.save();

    }
}
