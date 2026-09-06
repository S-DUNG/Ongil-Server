package sdung.ongil.domain.statistics.dto;

import lombok.Getter;
import sdung.ongil.domain.statistics.repository.ServiceUsageCount;

@Getter
public class ServiceUsageResponse {
    private final String serviceName;
    private final long count;

    public ServiceUsageResponse(ServiceUsageCount projection) {
        this.serviceName = projection.getServiceName();
        this.count = projection.getCount();
    }
}
