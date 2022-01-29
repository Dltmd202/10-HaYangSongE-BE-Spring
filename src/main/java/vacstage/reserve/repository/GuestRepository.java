package vacstage.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vacstage.reserve.domain.guest.Guest;

import java.util.List;
import java.util.Optional;


public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByUsernameAndFullName(String username, String fullname);

    Optional<Guest> findByUsername(String username);

    List<Guest> findByFullName(String username);


}