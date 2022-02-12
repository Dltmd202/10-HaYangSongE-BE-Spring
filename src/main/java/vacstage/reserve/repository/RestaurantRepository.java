package vacstage.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vacstage.reserve.domain.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByHost_Id(Long id);

}
