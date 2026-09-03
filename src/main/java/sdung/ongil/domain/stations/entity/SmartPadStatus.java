package sdung.ongil.domain.stations.entity;

public enum SmartPadStatus {
    NORMAL("정상"),
    BROKEN("고장"),
    INSPECTING("점검 중");

    private final String label;

    SmartPadStatus(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
}
