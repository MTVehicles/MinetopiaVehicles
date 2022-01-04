package nl.mtvehicles.core.infrastructure.enums;

public enum DriveUp {
    SLABS,
    BLOCKS,
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
