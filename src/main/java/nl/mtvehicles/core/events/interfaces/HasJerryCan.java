package nl.mtvehicles.core.events.interfaces;

public interface HasJerryCan {

    /**
     * Get current fuel in the jerry can (before the event is executed)
     * @return Current fuel in the jerry can
     */
    public int getJerryCanFuel();

    /**
     * Get the size of the jerry can (in litres)
     * @return Size of the jerry can
     */
    public int getJerryCanSize();

}
