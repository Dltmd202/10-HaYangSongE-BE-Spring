package vacstage.reserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.exception.GuestAlreadyHaveWaiting;
import vacstage.reserve.exception.NotAcceptableVaccineStep;
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
        validateGuestVaccineStep(restaurant, leader);
        validateGuestAlreadyHaveWaiting(leader);

        List<Guest> members = memberIds.stream()
                .map(guestRepository::findById)
                .collect(Collectors.toList());

        for (Guest member : members) {
            validateGuestVaccineStep(restaurant, member);
            validateGuestAlreadyHaveWaiting(member);
        }

        List<GuestWaiting> guestWaitings = members.stream()
                .map(GuestWaiting::createGuestWaiting)
                .collect(Collectors.toList());

        Waiting waiting = Waiting.createWaiting(restaurant, leader, guestWaitings);
        waitingRepository.save(waiting);
        return waiting.getId();
    }

    @Transactional
    public Long waiting(
            Long restaurantId, Long leaderId, Long... memberIds)
    {
        Restaurant restaurant = restaurantRepository.findById(restaurantId);
        Guest leader = guestRepository.findById(leaderId);
        validateGuestVaccineStep(restaurant, leader);
        validateGuestAlreadyHaveWaiting(leader);

        List<Guest> members = new ArrayList<>();
        for (Long memberId: memberIds){
            Guest member = guestRepository.findById(memberId);
            validateGuestVaccineStep(restaurant, member);
            validateGuestAlreadyHaveWaiting(member);
            members.add(member);
        }

        List<GuestWaiting> guestWaitings = members.stream()
                .map(GuestWaiting::createGuestWaiting)
                .collect(Collectors.toList());

        Waiting waiting = Waiting.createWaiting(restaurant, leader, guestWaitings);

        waitingRepository.save(waiting);
        return waiting.getId();
    }

    private void validateGuestVaccineStep(Restaurant restaurant, Guest guest){
        int applyingVaccineStep = guest.getVaccineElapsed() >= 14 ? guest.getVaccineStep(): guest.getVaccineStep() - 1;
        System.out.println(guest.getVaccineElapsed());
        if(restaurant.getVaccineCondition() > applyingVaccineStep){
            throw new NotAcceptableVaccineStep("백신 조건이 맞지 않습니다.");
        }
    }

    private void validateGuestAlreadyHaveWaiting(Guest guest){
        if(guest.getCurrentWaiting() != null){
            throw new GuestAlreadyHaveWaiting("게스트가 이미 웨이팅을 가지고있습니다.");
        }
    }

}
