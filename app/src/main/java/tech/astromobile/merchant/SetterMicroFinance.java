package tech.astromobile.merchant;

public class SetterMicroFinance {

    public SetterMicroFinance() {
    }

    private String uid, name, country, logo;
    private boolean isFixed;

    public SetterMicroFinance(String uid, String name, String country, String logo, boolean isFixed) {
        this.uid = uid;
        this.name = name;
        this.country = country;
        this.logo = logo;
        this.isFixed = isFixed;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isFixed() {
        return isFixed;
    }

    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }
}
