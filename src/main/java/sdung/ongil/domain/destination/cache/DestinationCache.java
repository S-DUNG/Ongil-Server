package sdung.ongil.domain.destination.cache;

import org.springframework.stereotype.Component;
import sdung.ongil.domain.destination.dto.DestinationSearchResult;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DestinationCache {

    // TTL: 10분 (필요하면 조정)
    private static final long TTL_SECONDS = 600;

    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();

    public void save(DestinationSearchResult result) {
        Instant expireAt = Instant.now().plusSeconds(TTL_SECONDS);
        store.put(result.destinationId(), new CacheEntry(result, expireAt));
    }

    public DestinationSearchResult get(String destinationId) {
        CacheEntry entry = store.get(destinationId);

        if (entry == null) {
            return null;
        }

        if (Instant.now().isAfter(entry.expireAt())) {
            store.remove(destinationId);
            return null;
        }

        return entry.result();
    }

    private record CacheEntry(DestinationSearchResult result, Instant expireAt) {
    }
}

//임시저장