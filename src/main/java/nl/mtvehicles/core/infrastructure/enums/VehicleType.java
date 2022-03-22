package nl.mtvehicles.core.infrastructure.enums;

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
    HELICOPTER;

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
}
