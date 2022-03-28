package nl.mtvehicles.core.infrastructure.enums;

public enum Version {
    v1_12,
    v1_13,
    v1_15,
    v1_16,
    v1_17,
    v1_18;

    public boolean is1_12(){
        return this.equals(Version.v1_12);
    }

    public boolean is1_13(){
        return this.equals(Version.v1_13);
    }

    public boolean is1_15(){
        return this.equals(Version.v1_15);
    }

    public boolean is1_16(){
        return this.equals(Version.v1_16);
    }

    public boolean is1_17(){
        return this.equals(Version.v1_17);
    }

    public boolean is1_18(){
        return this.equals(Version.v1_18);
    }

    public boolean isOld(){
        return is1_12() || is1_13();
    }

}
