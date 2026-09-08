package sdung.ongil.domain.stations.odsay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OdsayPathSearchResponse(
        Result result
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(
            List<Path> path
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Path(
            Info info,
            List<SubPath> subPath
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Info(
            int totalTime,       // 총 소요시간(분)
            int payment,         // 요금
            int busTransitCount  // 버스 환승 횟수
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record SubPath(
            int trafficType,      // 1=지하철, 2=버스, 3=도보
            Integer sectionTime,  // 이 구간 소요시간(분)
            String startName,     // 출발 정류장/지점 이름
            String endName,       // 도착 정류장/지점 이름
            Integer stationCount, // 이 구간 정류장/역 개수
            List<Lane> lane       // 버스/지하철 노선 정보 (버스번호, 호선명 등)
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Lane(
            String busNo,   // 버스 번호 (버스 구간일 때만 값 있음)
            String name     // 지하철 호선명 (지하철 구간일 때만 값 있음)
    ) {}
}