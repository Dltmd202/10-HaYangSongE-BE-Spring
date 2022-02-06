package vacstage.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vacstage.reserve.domain.waiting.Waiting;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
}
