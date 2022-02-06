package vacstage.reserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.domain.GuestWaiting;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.dto.waiting.RegisterWaitingDto;
import vacstage.reserve.dto.waiting.WaitingMemberDto;
import vacstage.reserve.exception.*;
import vacstage.reserve.repository.GuestRepository;
import vacstage.reserve.repository.RestaurantRepository;
import vacstage.reserve.repository.WaitingRepositorySupport;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WaitingService {

    private final GuestRepository guestRepository;
    private final RestaurantRepository restaurantRepository;
    private final WaitingRepositorySupport waitingRepositorySupport;

    public Waiting findOne(Long id){
        Waiting waiting = waitingRepositorySupport.findById(id)
                .orElseThrow(NotFoundWaitingException::new);
        return waiting;
    }

    /*
     * 웨이팅 등록
     */
    @Transactional
    public Long waiting(RegisterWaitingDto registerWaitingDto){
        Restaurant restaurant = restaurantRepository.findById(registerWaitingDto.getRestaurant())
                .orElseThrow(NotFoundRestaurantException::new);

        Guest leader = guestRepository.findByUsername(
                registerWaitingDto.getLeader()
        ).orElseThrow(NotFoundGuestException::new);

        List<Guest> members = new ArrayList<>();
        for (WaitingMemberDto waitingMemberDto: registerWaitingDto.getMember()){
            Guest member = guestRepository.findByUsername(
                    waitingMemberDto.getUsername()
                    )
                    .orElseThrow(NotFoundGuestException::new);
            members.add(member);
        }
        return waiting(restaurant, leader, members);
    }

    @Transactional
    public Long waiting(
            Restaurant restaurant, Guest leader, List<Guest> members)
    {
        validateGuestVaccineStep(restaurant, leader);
        validateGuestAlreadyHaveWaiting(leader);

        for (Guest member: members){
            validateGuestVaccineStep(restaurant, member);
            validateGuestAlreadyHaveWaiting(member);
        }

        List<GuestWaiting> guestWaitings = members.stream()
                .map(GuestWaiting::createGuestWaiting)
                .collect(Collectors.toList());

        Waiting waiting = Waiting.createWaiting(restaurant, leader, guestWaitings);

        waitingRepositorySupport.save(waiting);
        return waiting.getId();
    }

    @Transactional
    public Long waiting(
            Long restaurantId, Long leaderId, List<Long> memberIds)
    {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);
        Guest leader = guestRepository.findById(leaderId).get();

        List<Guest> members = new ArrayList<>();
        for (Long memberId: memberIds){
            Guest member = guestRepository.findById(memberId)
                    .orElseThrow(NotFoundGuestException::new);
            members.add(member);
        }
        return waiting(restaurant, leader, members);
    }

    @Transactional
    public Long waiting(
            Long restaurantId, Long leaderId, Long... memberIds)
    {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(NotFoundRestaurantException::new);
        Guest leader = guestRepository.findById(leaderId).get();

        List<Guest> members = new ArrayList<>();
        for (Long memberId: memberIds){
            Guest member = guestRepository.findById(memberId)
                    .orElseThrow(NotFoundGuestException::new);
            members.add(member);
        }
        return waiting(restaurant, leader, members);
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
