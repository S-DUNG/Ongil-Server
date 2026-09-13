package sdung.ongil.domain.destination.voice;

public record VoiceGuideResponse(
        String destinationName,   // 인식된 목적지명
        Long routeId,             // 생성된 경로 ID
        String guideText          // TTS로 읽어줄 최종 안내 문장
) {
}