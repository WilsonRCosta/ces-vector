package service.tm.apim.enums;

public enum XidStatus {
    ACTIVE,
    CANCELED,

    PREPARING,
    PREPARED,

    COMMITTING,
    COMMITTED,
    COMMIT_FAILED,

    ROLLING_BACK,
    ROLLED_BACK,
    ROLLBACK_FAILED
}
