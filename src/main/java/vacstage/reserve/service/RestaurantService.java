package vacstage.reserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.Acceptation;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.domain.waiting.WaitingSearch;
import vacstage.reserve.exception.NoWaitingToAccept;
import vacstage.reserve.exception.NotFoundRestaurantException;
import vacstage.reserve.repository.AcceptationRepository;
import vacstage.reserve.repository.RestaurantRepository;
import vacstage.reserve.repository.WaitingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final WaitingRepository waitingRepository;

    private final AcceptationRepository acceptationRepository;

    @Transactional
    public Long register(Restaurant restaurant){
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    public Restaurant findOne(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);
    }

    public List<Restaurant> findRestaurants(){
        return restaurantRepository.findAll();
    }

    /**
     * 가장 최근의 웨이팅 수락
     */
    @Transactional
    public Long acceptWaiting(Long restaurantId){
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);
        WaitingSearch waitingSearch = createAcceptSearchCondition(restaurantId, 1);
        List<Waiting> findWaiting = waitingRepository.findAll(waitingSearch);
        validateWaitingToAccept(findWaiting);
        Acceptation acceptation = Acceptation.createAccept(restaurant, findWaiting.get(0));
        acceptationRepository.save(acceptation);
        return acceptation.getId();
    }

    private WaitingSearch createAcceptSearchCondition(Long restaurantId, int limit) {
        WaitingSearch waitingSearch = new WaitingSearch();
        LocalDateTime now = LocalDateTime.now();
        waitingSearch.setRestaurantId(restaurantId);
        waitingSearch.setSearchFromDateTime(
                LocalDateTime.now().minusDays(1).withHour(9).withMinute(0).withSecond(0)
        );
        waitingSearch.setSearchToDateTime(now);
        waitingSearch.setWaitingStatus(WaitingStatus.WAITING);
        waitingSearch.setLimit(limit);
        return waitingSearch;
    }

    public void validateWaitingToAccept(List<Waiting> restaurantWaiting){
        if(restaurantWaiting.size() == 0){
            throw new NoWaitingToAccept("수락할 웨이팅이 없습니다.");
        }
    }
}
