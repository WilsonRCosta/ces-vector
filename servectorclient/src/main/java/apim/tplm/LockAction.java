package apim.tplm;

public enum LockAction {
    ACQUIRE("acquire"),
    RELEASE("release");

    private final String action;

    LockAction(String action) {
        this.action = action;
    }

    public String action() {
        return action;
    }
}
