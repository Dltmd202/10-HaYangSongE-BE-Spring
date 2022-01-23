package vacstage.reserve.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.exception.GuestAlreadyHaveWaiting;
import vacstage.reserve.exception.NotAcceptableVaccineStep;
import vacstage.reserve.repository.WaitingRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class WaitingServiceTest {

    @Autowired WaitingService waitingService;

    @Autowired WaitingRepository waitingRepository;

    @Autowired RestaurantService restaurantService;

    @Autowired GuestService guestService;

    @Test
    public void 웨이팅_등록() throws Exception{
        //given
        Guest host = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(host, "맥도날드", 1);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));

        //when
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);

        restaurantService.register(restaurant);
        Long waitingId = waitingService.waiting(
                restaurant.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());

        //then
        Waiting findWaiting = waitingRepository.findById(waitingId);
        assertEquals(findWaiting.getWaitingStatus(), WaitingStatus.WAITING);
        assertEquals(findWaiting.getLeader(), leader);
        assertEquals(findWaiting.getMember().get(0).getGuest(), guest1);
        assertEquals(findWaiting.getMember().get(1).getGuest(), guest2);
        assertEquals(findWaiting.getMember().get(2).getGuest(), guest3);

        assertEquals(restaurant.getWaitings().get(0), findWaiting);

    }

    @Test(expected = NotAcceptableVaccineStep.class)
    public void 멤버_백신조건_미달() throws Exception{
        //given
        Guest host = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(host, "맥도날드", 2);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 1, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));

        //when
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);

        restaurantService.register(restaurant);
        Long waitingId = waitingService.waiting(
                restaurant.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());

        //then
        fail();
    }

    @Test(expected = NotAcceptableVaccineStep.class)
    public void 리더_백신조건_미달() throws Exception{
        //given
        Guest host = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(host, "맥도날드", 2);

        Guest leader = createGuest("leader", 1, LocalDateTime.now().minusDays(21));
        leader.setVaccineDate(LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));

        //when
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);

        restaurantService.register(restaurant);
        Long waitingId = waitingService.waiting(
                restaurant.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());

        //then
        fail();
    }

    @Test(expected = NotAcceptableVaccineStep.class)
    public void 백신_날짜_조건_미달() throws Exception{
        //given
        Guest host = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(host, "맥도날드", 2);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(13));

        //when
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);

        restaurantService.register(restaurant);
        Long waitingId = waitingService.waiting(
                restaurant.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());

        //then
        fail();
    }

    @Test(expected = GuestAlreadyHaveWaiting.class)
    public void 이미_예약있는_게스트의_얘약() throws Exception{
        //given
        Guest host1 = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant1 = createRestaurant(host1, "맥도날드", 2);

        Guest host2 = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant2 = createRestaurant(host2, "버거킹", 1);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));
        Guest guest4 = createGuest("guest4", 2, LocalDateTime.now().minusDays(21));

        //when
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);
        guestService.join(guest4);

        restaurantService.register(restaurant1);
        restaurantService.register(restaurant2);

        Long waitingId1 = waitingService.waiting(
                restaurant1.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());

        Long waitingId2 = waitingService.waiting(
                restaurant2.getId(), leader.getId(), guest4.getId());

        //then
        fail();
    }

    private Guest createGuest(String username, int vaccineStep, LocalDateTime vaccineDate) {
        Guest guest = new Guest();
        guest.setUsername(username);
        guest.setFullName("이승환");
        guest.setVaccineStep(vaccineStep);
        guest.setPassword("1234");
        guest.setPhoneNumber("010-1234-1234");
        guest.setVaccineDate(vaccineDate);
        return guest;
    }

    private Restaurant createRestaurant(Guest guest, String name, int vaccineCondition) {
        Restaurant restaurant = Restaurant.createBaseRestaurant(guest);
        restaurant.setName(name);
        restaurant.setBranchName("터미널점");
        restaurant.setDistrict("빵빵동");
        restaurant.setDetailAddress("댕댕길 21");
        restaurant.setPhoneNumber("041-1234-1234");
        restaurant.setWaitingAverage(3);
        restaurant.setTotalSeat(20);
        restaurant.setRemainSeat(10);
        restaurant.setVaccineCondition(vaccineCondition);
        return restaurant;
    }
}