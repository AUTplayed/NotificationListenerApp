package codes.fepi.notificationlistener.dto;

import java.util.HashSet;
import java.util.Set;

public class ActionDto {
    private Set<String> clear;

    public ActionDto() {
        clear = new HashSet<>();
    }

    public ActionDto(Set<String> clear) {
        this.clear = clear;
    }

    public Set<String> getClear() {
        return clear;
    }

    public void setClear(Set<String> clear) {
        this.clear = clear;
    }

    public void addAction(ActionDto add) {
        this.clear.addAll(add.getClear());
    }

    public boolean hasActions() {
        return clear.size() > 0;
    }
}
