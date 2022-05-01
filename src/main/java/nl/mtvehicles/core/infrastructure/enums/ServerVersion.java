package nl.mtvehicles.core.infrastructure.enums;

public enum ServerVersion {
    v1_12,
    v1_13,
    v1_15,
    v1_16,
    v1_17,
    v1_18_R1,
    v1_18_R2;

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

}
