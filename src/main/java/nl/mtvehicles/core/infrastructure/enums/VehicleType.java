package nl.mtvehicles.core.infrastructure.enums;

import java.util.Locale;

/**
 * Type of vehicle
 */
public enum VehicleType {
    /**
     * Car includes cars, bicycles, as well as motorcycles
     */
    CAR,
    /**
     * Vehicles floating in the air (hovercraft and UFO)
     */
    HOVER,
    /**
     * Tanks - with the ability to shoot
     */
    TANK,
    /**
     * Helicopters - with the ability to fly, with blades
     */
    HELICOPTER,
    /**
     * Airplanes - with the ability to fly, their movement differs from helicopters
     */
    AIRPLANE;

    public String getName(){
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase(Locale.ROOT);
    }

    public boolean isCar(){
        return this.equals(CAR);
    }

    public boolean isHover(){
        return this.equals(HOVER);
    }

    public boolean isTank(){
        return this.equals(TANK);
    }

    public boolean isHelicopter(){
        return this.equals(HELICOPTER);
    }

    public boolean isAirplane(){
        return this.equals(AIRPLANE);
    }

    public boolean canFly(){
        return this.equals(AIRPLANE) || this.equals(HELICOPTER);
    }
}
