package service.tm.apim.xid;

import javax.transaction.xa.Xid;
import java.util.UUID;

public abstract class BaseXid implements Xid {

	private final String identifier;
	private final Long transactionId;
	private final Long branchId;

	public BaseXid(final String identifier, final Long tid, final Long branchId) {
		this.identifier = identifier;
		this.transactionId = createTransactionId(tid);
		this.branchId = branchId;
	}

	private long createTransactionId(Long tid) {
		if (tid != null) return tid;
		return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public String getIdentifier() {
		return identifier;
	}

	public long getBranchId() {
		return branchId;
	}


	@Override
	public byte[] getGlobalTransactionId() {
		throw new UnsupportedOperationException("Not implemented.");
	}

	@Override
	public int getFormatId() {
		throw new UnsupportedOperationException("Not implemented.");
	}

	@Override
	public byte[] getBranchQualifier() {
		throw new UnsupportedOperationException("Not implemented.");
	}
}