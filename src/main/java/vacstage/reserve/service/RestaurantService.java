package vacstage.reserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.repository.RestaurantRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public Long register(Restaurant restaurant){
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    public Restaurant findOne(Long restaurantId) {
        return restaurantRepository.findById(restaurantId);
    }

    public List<Restaurant> findRestaurants(){
        return restaurantRepository.findAll();
    }
}
