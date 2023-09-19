package nl.mtvehicles.core.infrastructure.vehicle;

import nl.mtvehicles.core.infrastructure.annotations.ToDo;
import nl.mtvehicles.core.infrastructure.enums.VehicleType;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Maps containing vehicles' data
 */
@ToDo("make nicer, move, refactor - for better API usage")
public class VehicleData {
    /**
     * @see Vehicle#getCurrentSpeed()
     */
    public static HashMap<String, Double> speed = new HashMap<>();
    public static HashMap<String, Double> speedhigh = new HashMap<>();
    public static HashMap<String, Integer> maxheight = new HashMap<>();
    public static HashMap<String, Double> mainx = new HashMap<>();
    public static HashMap<String, Double> mainy = new HashMap<>();
    public static HashMap<String, Double> mainz = new HashMap<>();
    public static HashMap<String, Integer> seatsize = new HashMap<>();
    public static HashMap<String, Double> seatx = new HashMap<>();
    public static HashMap<String, Double> seaty = new HashMap<>();
    public static HashMap<String, Double> seatz = new HashMap<>();
    public static HashMap<String, Double> wiekenx = new HashMap<>();
    public static HashMap<String, Double> wiekeny = new HashMap<>();
    public static HashMap<String, Double> wiekenz = new HashMap<>();
    public static HashMap<String, VehicleType> type = new HashMap<>();
    /**
     * @see Vehicle#getCurrentFuel()
     */
    public static HashMap<String, Double> fuel = new HashMap<>();
    public static HashMap<String, Double> fuelUsage = new HashMap<>();
    /**
     * All armor stands - vehicle seats, rotors, etc... (by entity names)
     */
    public static HashMap<String, ArmorStand> autostand = new HashMap<>();
    /**
     * Main seat (by license plate)
     */
    public static HashMap<String, ArmorStand> autostand2 = new HashMap<>();
    public static Map<String, Long> lastUsage = new HashMap<>();
    public static HashMap<String, Boolean> fallDamage = new HashMap<>(); //Used for helicopters when 'extremely falling'

    public static HashMap<String, Integer> RotationSpeed = new HashMap<>();
    public static HashMap<String, Double> MaxSpeed = new HashMap<>();
    public static HashMap<String, Double> AccelerationSpeed = new HashMap<>();
    public static HashMap<String, Double> BrakingSpeed = new HashMap<>();
    public static HashMap<String, Double> MaxSpeedBackwards = new HashMap<>();
    public static HashMap<String, Double> FrictionSpeed = new HashMap<>();
    public static HashMap<String, Set<String>> lastRegions = new HashMap<>();
    /**
     * @see VehicleUtils#openedTrunk
     * @since 2.5.1
     */
    private static HashMap<String, Set<Player>> trunkViewers = new HashMap<>();

    private VehicleData(){}

    /**
     * Check if a player has an opened trunk
     * @param licensePlate Vehicle's license plate (this vehicle's trunk is being opened)
     * @param player Player
     * @since 2.5.1
     */
    public static boolean isTrunkViewer(String licensePlate, Player player){
        setTrunkViewers(licensePlate);
        return trunkViewers.get(licensePlate).contains(player);
    }

    /**
     * Add a player to list of players who have vehicle's trunk inventory opened
     * @param licensePlate Vehicle's license plate
     * @param player Player who's being added to the set
     * @return Returns {@code false} if player is already present in the set
     * @since 2.5.1
     */
    public static boolean trunkViewerAdd(String licensePlate, Player player){
        setTrunkViewers(licensePlate);
        Set<Player> viewers = trunkViewers.get(licensePlate);
        boolean returns = viewers.add(player);
        trunkViewers.put(licensePlate, viewers);
        return returns;
    }

    /**
     * Remove a player from list of players who have vehicle's trunk inventory opened
     * @param licensePlate Vehicle's license plate
     * @param player Player who's being removed from the set
     * @return Returns {@code false} if player is not present in the set
     * @since 2.5.1
     */
    public static boolean trunkViewerRemove(String licensePlate, Player player){
        setTrunkViewers(licensePlate);
        Set<Player> viewers = trunkViewers.get(licensePlate);
        boolean returns = viewers.remove(player);
        trunkViewers.put(licensePlate, viewers);
        return returns;
    }

    /**
     * Get a HashSet of all trunk viewers of a vehicle
     * @param licensePlate Vehicle's license plate
     */
    public static Set<Player> getTrunkViewers(String licensePlate){
        setTrunkViewers(licensePlate);
        return trunkViewers.get(licensePlate);
    }

    /**
     * Prevent issues if {@link #trunkViewers} is not initialised
     */
    private static void setTrunkViewers(String licensePlate){
        if (!trunkViewers.containsKey(licensePlate)) trunkViewers.put(licensePlate, new HashSet<>());
    }
}
