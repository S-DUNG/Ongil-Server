package sdung.ongil.domain.manage.dashboard.repository;

import sdung.ongil.domain.manage.smartpad.entity.SmartPadStatus;

public interface SmartPadStatusCount {
    SmartPadStatus getStatus();
    Long getCount();
}
