package isos.tutorial.isyiesd.lu.apim.enums;


public enum LookupType {
    VECTOR("VECTOR"),
    TM("TM"),
    TPLM("TPLM");

    private final String type;

    LookupType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
