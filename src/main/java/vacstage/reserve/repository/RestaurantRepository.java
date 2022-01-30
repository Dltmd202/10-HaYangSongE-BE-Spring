package vacstage.reserve.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vacstage.reserve.domain.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {


}
