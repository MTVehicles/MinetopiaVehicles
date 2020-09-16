package nl.mtvehicles.core.Infrastructure.Models;

import nl.mtvehicles.core.Main;

import java.util.List;
import java.util.Map;

public class Vehicle {
    private String licensePlate;

    public static Vehicle getByPlate(String plate) {
        List<Map<?, ?>> vehiclesData = Main.vehicleDataConfig.getConfig().getMapList(plate);

        if (!existsByPlate(plate)) return null;

        Map<?, ?> vehicleData = vehiclesData.get(0);

        Vehicle vehicle = new Vehicle();
        vehicle.setLicensePlate((String) vehicleData.get("licensePlate"));

        return vehicle;
    }

    public static boolean existsByPlate(String plate) {
        List<Map<?, ?>> vehiclesData = Main.vehicleDataConfig.getConfig().getMapList(plate);

        return vehiclesData.size() == 1;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
}
