package nl.mtvehicles.core.infrastructure.helpers;

import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;

public class VehicleData {
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
    public static HashMap<String, String> type = new HashMap<>();
    public static HashMap<String, Double> fuel = new HashMap<>();
    public static HashMap<String, Double> fuelUsage = new HashMap<>();
    public static HashMap<String, ArmorStand> autostand = new HashMap<>();
    public static HashMap<String, ArmorStand> autostand2 = new HashMap<>();
    public static Map<String, Long> lastUsage = new HashMap<>();

    public static HashMap<String, Integer> RotationSpeed = new HashMap<>();
    public static HashMap<String, Double> MaxSpeed = new HashMap<>();
    public static HashMap<String, Double> AccelerationSpeed = new HashMap<>();
    public static HashMap<String, Double> BrakingSpeed = new HashMap<>();
    public static HashMap<String, Double> MaxSpeedBackwards = new HashMap<>();
    public static HashMap<String, Double> FrictionSpeed = new HashMap<>();
}
