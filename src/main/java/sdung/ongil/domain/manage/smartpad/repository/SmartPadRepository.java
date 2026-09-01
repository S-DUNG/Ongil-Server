package sdung.ongil.domain.manage.smartpad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sdung.ongil.domain.manage.smartpad.entity.SmartPadEntity;

import java.util.Optional;

public interface SmartPadRepository extends JpaRepository<SmartPadEntity, Long> {

    // 시리얼번호로 조회 (중복 체크용)
    Optional<SmartPadEntity> findBySerialNumber(String serialNumber);
}