package sdung.ongil.domain.statistics.aspect;

// @TrackUsage 가 붙은 메서드가 예외 없이 끝났을 때만 이용 통계 1건을 남김

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import sdung.ongil.domain.statistics.annotation.TrackUsage;
import sdung.ongil.domain.statistics.entity.ServiceUsageLog;
import sdung.ongil.domain.statistics.repository.ServiceUsageLogRepository;

import org.aspectj.lang.reflect.MethodSignature;
import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class ServiceUsageLoggingAspect {
    private final ServiceUsageLogRepository serviceUsageLogRepository;

    @Async
    @AfterReturning("@annotation(sdung.ongil.domain.statistics.annotation.TrackUsage)")
    public void logUsage(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TrackUsage trackUsage = signature.getMethod().getAnnotation(TrackUsage.class);
        ServiceUsageLog log = new ServiceUsageLog(
                trackUsage.value(),
                LocalDateTime.now(),
                null
        );
        serviceUsageLogRepository.save(log);
    }
}
