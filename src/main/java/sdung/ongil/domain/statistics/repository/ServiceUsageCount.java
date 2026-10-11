package sdung.ongil.domain.statistics.repository;

// JPQL의 "AS serviceName / AS count"와 이름을 맞춰야 스프링이 자동 매핑함
public interface ServiceUsageCount {
    String getServiceName();
    Long getCount();
}
