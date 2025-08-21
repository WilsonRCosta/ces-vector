package apim.tplm;

import java.util.List;

public record LockElementsDto(List<LockElement> lockElements) {

    public List<LockElement> getLockElements() {
        return lockElements;
    }
}
