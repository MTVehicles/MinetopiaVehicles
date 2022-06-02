package nl.mtvehicles.core.infrastructure.enums;

/**
 * Enum of supported server versions (used for different NMS and Spigot API changes)
 */
public enum ServerVersion {
    /**
     * 1.12-1.12.2
     */
    v1_12,
    /**
     * 1.13.1-1.13.2
     */
    v1_13,
    /**
     * 1.15-1.15.2
     */
    v1_15,
    /**
     * 1.16.4-1.16.5
     */
    v1_16,
    /**
     * 1.17-1.17.1
     */
    v1_17,
    /**
     * 1.18-1.18.1
     */
    v1_18_R1,
    /**
     * 1.18.2
     */
    v1_18_R2,
    /**
     * 1.19 (expected)
     */
    v1_19;

    public boolean is1_12(){
        return this.equals(v1_12);
    }

    public boolean is1_13(){
        return this.equals(v1_13);
    }

    public boolean is1_15(){
        return this.equals(v1_15);
    }

    public boolean is1_16(){
        return this.equals(v1_16);
    }

    public boolean is1_17(){
        return this.equals(v1_17);
    }

    public boolean is1_18_R1(){
        return this.equals(v1_18_R1);
    }

    public boolean is1_18_R2(){
        return this.equals(v1_18_R2);
    }

    public boolean is1_19(){
        return this.equals(v1_19);
    }

}
