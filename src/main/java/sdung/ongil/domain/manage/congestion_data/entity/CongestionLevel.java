package sdung.ongil.domain.manage.congestion_data.entity;

public enum CongestionLevel {
    LOW("여유"),
    MEDIUM("보통"),
    HIGH("혼잡");

    private final String label;
    CongestionLevel(String label) {this.label = label;}
    public String getLabel() {return label;}
}
