package nl.mtvehicles.core.infrastructure.enums;

/**
 * Type of driving up
 */
public enum DriveUp {
    /**
     * Driving is only enabled on slabs
     */
    SLABS,
    /**
     * Driving is only enabled on full blocks
     */
    BLOCKS,
    /**
     * Driving is enabled on both slabs and full blocks
     */
    BOTH;

    public boolean isSlabs(){
        return this.equals(DriveUp.SLABS);
    }

    public boolean isBlocks(){
        return this.equals(DriveUp.BLOCKS);
    }

    public boolean isBoth(){
        return this.equals(DriveUp.BOTH);
    }
}
