package sdung.ongil.domain.statistics.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrackUsage {
    // 이 어노테이션 붙인 메서드가 정상적으로 실행될 때마다 서비스 이용 통계 로그가 1건 쌓임
    String value();
}
