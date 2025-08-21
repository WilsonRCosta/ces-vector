package service.tplm.apim.model;

/**
 * Indicates the lock request elements for further operations done on the specified resource manager
 */
public class LockElement {
    /**
     * The transaction that is using the lock element
     */
    public long tid;

    /**
     * The vector index that will be locked
     */
    public int index;

    /**
     * The resource host for the specified lock
     */
    public String resource;

    /**
     * The lock type for the specified index (READ/WRITE)
     */
    public String lockType;

    public LockElement() {}

    public LockElement(int index, String resource, String lockType) {
        this.index = index;
        this.resource = resource;
        this.lockType = lockType;
    }
}
