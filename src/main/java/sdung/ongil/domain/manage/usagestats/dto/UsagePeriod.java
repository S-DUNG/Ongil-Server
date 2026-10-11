package sdung.ongil.domain.manage.usagestats.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public enum UsagePeriod {
    TODAY {
        @Override
        public LocalDateTime startDateTime() {
            return LocalDate.now().atStartOfDay();
        }
        @Override
        public int days() {
            return 1;
        }
    },
    WEEK {
        @Override
        public LocalDateTime startDateTime() {
            return LocalDate.now().minusDays(6).atStartOfDay();
        }
        @Override
        public int days() {
            return 7;
        }
    },
    MONTH {
        @Override
        public LocalDateTime startDateTime() {
            return LocalDate.now().minusDays(29).atStartOfDay();
        }
        @Override
        public int days() {
            return 30;
        }
    };

    public abstract LocalDateTime startDateTime();
    public abstract int days();

    public LocalDateTime endDateTime() {
        return LocalDate.now().atTime(LocalTime.MAX);
    }
}