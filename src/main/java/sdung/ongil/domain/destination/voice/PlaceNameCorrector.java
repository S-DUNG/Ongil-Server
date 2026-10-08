package sdung.ongil.domain.destination.voice;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaceNameCorrector {

    private final List<String> knownNames;

    public PlaceNameCorrector() {
        this.knownNames = loadNames().stream().distinct().toList();
    }

    // TODO: 실제 정류장/역 이름 목록(DB의 station 테이블, 자주 가는 목적지 등)으로 연결하세요
    private List<String> loadNames() {
        return List.of("강남역", "서울역", "서울대입구역", "홍대입구역", "신촌역");
    }

    public String correct(String keyword) {
        String key = keyword.replaceAll("\\s+", "");
        String keyJamo = toJamo(key);

        String best = null;
        int bestDist = Integer.MAX_VALUE;
        int secondDist = Integer.MAX_VALUE;

        for (String name : knownNames) {
            int d = levenshtein(keyJamo, toJamo(name.replaceAll("\\s+", "")));
            if (d == 0) {
                return name;
            }
            if (d < bestDist) {
                secondDist = bestDist;
                bestDist = d;
                best = name;
            } else if (d < secondDist) {
                secondDist = d;
            }
        }

        int limit = Math.max(1, keyJamo.length() / 4);   // 허용 오차: 자모 길이의 약 25%
        boolean confident = best != null && bestDist <= limit && bestDist < secondDist;
        return confident ? best : keyword;
    }

    private static String toJamo(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c >= 0xAC00 && c <= 0xD7A3) {
                int i = c - 0xAC00;
                sb.append((char) (0x1100 + i / 588));            // 초성
                sb.append((char) (0x1161 + (i % 588) / 28));     // 중성
                int jong = i % 28;
                if (jong > 0) {
                    sb.append((char) (0x11A7 + jong));           // 종성
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int levenshtein(String a, String b) {
        int[] prev = new int[b.length() + 1];
        int[] curr = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) prev[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[b.length()];
    }

    // 발화와 후보 이름의 거리 (0에 가까울수록 비슷함)
    public int distance(String keyword, String placeName) {
        String k = keyword.replaceAll("\\s+", "");
        String p = placeName.replaceAll("\\s+", "");
        if (p.contains(k)) {
            return 0;   // "강남역" vs "강남역 2호선" 같은 경우
        }
        return levenshtein(toJamo(k), toJamo(p));
    }

    // 허용 오차 (자모 길이의 약 25%, 최소 1)
    public int allowedDistance(String keyword) {
        return Math.max(1, toJamo(keyword.replaceAll("\\s+", "")).length() / 4);
    }
}