package service.tm.apim.xid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class GlobalXid extends BaseXid {

    private final AtomicLong branchId = new AtomicLong();
    private final Map<Long, List<BranchXid>> branchesMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(GlobalXid.class);

    public GlobalXid(final String transactionName) {
        super(transactionName, null,null);
    }

    public Map<Long, List<BranchXid>> getBranchesMap() {
        return branchesMap;
    }

    public void addResourceToBranches(final String resource) {
        long tid = getTransactionId();
        String hostname = resource;
        if (!hostname.contains("http")) hostname = "http://" + hostname;

        BranchXid branchXid = new BranchXid(hostname, tid, branchId.getAndIncrement());
        if (!branchesMap.containsKey(tid)) {
            logger.info("New transaction registered: " + tid);
            List<BranchXid> branchList = new ArrayList<>();
            branchList.add(branchXid);
            branchesMap.put(tid, branchList);
        } else {
            branchesMap.get(tid).add(branchXid);
            logger.info("Resource " + hostname + " added to transaction branches.");
        }
    }

    public void removeResourceFromBranches(final String resource) {
        if (branchesMap.containsKey(getTransactionId())) {
            branchesMap.get(getTransactionId()).removeIf(t -> t.getIdentifier().equals(resource));
            logger.info("Resource " + resource + " removed from transaction branches.");
        } else {
            logger.error("Resource " + resource + " not found in transaction branches.");
        }
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
        return getTransactionId() == xid.getTransactionId() && getIdentifier().equals(xid.getIdentifier());
    }

}
