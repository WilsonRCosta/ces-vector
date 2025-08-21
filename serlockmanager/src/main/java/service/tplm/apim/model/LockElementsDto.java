package service.tplm.apim.model;

import java.util.List;

public class LockElementsDto {

    private List<LockElement> lockElements;

    public LockElementsDto() {}

    public LockElementsDto(List<LockElement> lockElements) {
        this.lockElements = lockElements;
    }

    public List<LockElement> getLockElements() {
        return lockElements;
    }

    public void setLockElements(List<LockElement> lockElements) {
        this.lockElements = lockElements;
    }
}
