package service.tm.apim.xid;

public class BranchXid extends BaseXid {
    public BranchXid(String resourceName, long transactionId, long branchId) {
        super(resourceName, transactionId, branchId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GlobalXid)) {
            return false;
        }
        final GlobalXid xid = (GlobalXid) o;
        return getTransactionId() == xid.getTransactionId()
                && getBranchId() == xid.getBranchId()
                && getIdentifier().equals(xid.getIdentifier());
    }
}
