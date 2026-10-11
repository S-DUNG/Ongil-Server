package sdung.ongil.domain.destination.voice;

public record VoiceGuideResponse(
        String destinationName,
        Long routeId,
        String guideText,
        String recognizedKeyword   // 추가: 시스템이 이해한 목적지 키워드
) {}
