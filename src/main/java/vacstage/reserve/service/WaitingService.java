package vacstage.reserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.Waiting;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.repository.GuestRepository;
import vacstage.reserve.repository.RestaurantRepository;
import vacstage.reserve.repository.WaitingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {

    private final GuestRepository guestRepository;
    private final RestaurantRepository restaurantRepository;
    private final WaitingRepository waitingRepository;

    /*
     * 웨이팅 등록
     */
    @Transactional
    public Long waiting(
            Long restaurantId, Long leaderId, List<Long> memberIds)
    {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        Guest leader = guestRepository.findById(leaderId);
        List<Guest> members = memberIds.stream()
                .map(guestRepository::findById)
                .collect(Collectors.toList());

        List<GuestWaiting> guestWaitings = members.stream()
                .map(GuestWaiting::createGuestWaiting)
                .collect(Collectors.toList());

        Waiting waiting = Waiting.createWaiting(restaurant, leader, guestWaitings);
        return waiting.getId();
    }

    @Transactional
    public Long waiting(
            Long restaurantId, Long leaderId, Long... memberIds)
    {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        Guest leader = guestRepository.findById(leaderId);

        List<Guest> members = new ArrayList<>();
        for (Long memberId: memberIds){
            Guest member = guestRepository.findById(memberId);
            members.add(member);
        }

        List<GuestWaiting> guestWaitings = members.stream()
                .map(GuestWaiting::createGuestWaiting)
                .collect(Collectors.toList());

        Waiting waiting = Waiting.createWaiting(restaurant, leader, guestWaitings);

        waitingRepository.save(waiting);
        return waiting.getId();
    }

}
