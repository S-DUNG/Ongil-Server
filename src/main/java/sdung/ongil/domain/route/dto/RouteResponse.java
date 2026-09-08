package sdung.ongil.domain.route.dto;

import sdung.ongil.domain.route.entity.Route;
import sdung.ongil.domain.stations.odsay.OdsayPathSearchResponse;

import java.util.List;

public class RouteResponse {

    private final Long routeId;
    private final int totalTime;
    private final int payment;
    private final int transferCount;
    private final List<SegmentResponse> segments;

    // Route Entity + ODsay 원본 구간정보를 합쳐서 응답 DTO로 변환
    public RouteResponse(Route route, List<OdsayPathSearchResponse.SubPath> subPaths) {
        this.routeId = route.getId();
        this.totalTime = route.getTotalTime();
        this.payment = route.getPayment();
        this.transferCount = route.getTransferCount();
        this.segments = subPaths.stream()
                .map(SegmentResponse::new)
                .toList();
    }

    public Long getRouteId() { return routeId; }
    public int getTotalTime() { return totalTime; }
    public int getPayment() { return payment; }
    public int getTransferCount() { return transferCount; }
    public List<SegmentResponse> getSegments() { return segments; }

    // 구간(버스/지하철/도보) 하나하나를 나타내는 내부 클래스
    public static class SegmentResponse {
        private final String type;       // "BUS", "SUBWAY", "WALK"
        private final Integer sectionTime;
        private final String startName;
        private final String endName;

        public SegmentResponse(OdsayPathSearchResponse.SubPath subPath) {
            this.type = switch (subPath.trafficType()) {
                case 1 -> "SUBWAY";
                case 2 -> "BUS";
                case 3 -> "WALK";
                default -> "UNKNOWN";
            };
            this.sectionTime = subPath.sectionTime();
            this.startName = subPath.startName();
            this.endName = subPath.endName();
        }

        public String getType() { return type; }
        public Integer getSectionTime() { return sectionTime; }
        public String getStartName() { return startName; }
        public String getEndName() { return endName; }
    }
}