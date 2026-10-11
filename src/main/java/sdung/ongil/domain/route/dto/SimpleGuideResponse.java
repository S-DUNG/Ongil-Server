package sdung.ongil.domain.route.dto;

import java.util.List;

public class SimpleGuideResponse {

    private final Long routeId;
    private final int totalTime;
    private final int payment;
    private final int transferCount;
    private final List<String> steps;   // 구간별 안내 문장 (프론트에서 한 줄씩 보여줄 때 사용)
    private final String guideText;     // 전체를 이어붙인 안내문 (한 번에 보여줄 때 사용)

    public SimpleGuideResponse(Long routeId, int totalTime, int payment,
                               int transferCount, List<String> steps) {
        this.routeId = routeId;
        this.totalTime = totalTime;
        this.payment = payment;
        this.transferCount = transferCount;
        this.steps = steps;
        this.guideText = String.join(" → ", steps);
    }

    public Long getRouteId() { return routeId; }
    public int getTotalTime() { return totalTime; }
    public int getPayment() { return payment; }
    public int getTransferCount() { return transferCount; }
    public List<String> getSteps() { return steps; }
    public String getGuideText() { return guideText; }
}