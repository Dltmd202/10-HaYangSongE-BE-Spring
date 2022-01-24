package vacstage.reserve.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.Acceptation;
import vacstage.reserve.domain.Menu;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.exception.NoWaitingToAccept;
import vacstage.reserve.repository.AcceptationRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RestaurantServiceTest {

    @Autowired GuestService guestService;
    @Autowired RestaurantService restaurantService;
    @Autowired WaitingService waitingService;
    @Autowired AcceptationRepository acceptationRepository;


    @Test
    public void 식당등록() throws Exception{
        //given
        Guest guest = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(guest, "맥도날드", 1);

        Menu menu1 = Menu.createMenu("감자튀김", 3000);
        Menu menu2 = Menu.createMenu("햄버거", 2000);

        //when
        restaurant.addMenu(menu1);
        restaurant.addMenu(menu2);

        Long saveId = restaurantService.register(restaurant);

        Restaurant findRestaurant = restaurantService.findOne(saveId);

        //then
        assertEquals(restaurant, findRestaurant);
        assertEquals(restaurant.getName(), "맥도날드");
        assertEquals(restaurant.getVaccineCondition(), 1);

        assertEquals(findRestaurant.getMenus().get(0), menu1);
        assertEquals(findRestaurant.getMenus().get(0).getName(), "감자튀김");
        assertEquals(findRestaurant.getMenus().get(0).getPrice(), 3000);

        assertNotEquals(findRestaurant.getMenus().get(0), menu2);
        assertEquals(findRestaurant.getMenus().get(1), menu2);
        assertNotEquals(findRestaurant.getMenus().get(1), menu1);

        assertEquals(findRestaurant.getHost(), guest);
        assertEquals(findRestaurant.getHost().getUsername(), "admin1");
        assertTrue(findRestaurant.getHost().getIsHost());



    }

    @Test
    public void 웨이팅_수락() throws Exception{
        //given
        Guest host = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(host, "맥도날드", 1);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));

        guestService.join(host);
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);
        restaurantService.register(restaurant);

        Long waitingId = waitingService.waiting(
                restaurant.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());
        Waiting waiting = waitingService.findOne(waitingId);

        //when
        restaurantService.acceptWaiting(restaurant.getId());

        //then
        assertEquals(waiting.getWaitingStatus(), WaitingStatus.ACCEPT);
        assertEquals(leader.getLeading().size(), 1);
        assertEquals(leader.getCurrentWaiting(), null);
        assertEquals(leader.getLeading().get(0), waiting);
        assertEquals(guest1.getGuestWaiting().size(), 1);
        assertEquals(guest1.getCurrentWaiting(), null);
        assertEquals(guest2.getGuestWaiting().size(), 1);
        assertEquals(guest2.getCurrentWaiting(), null);
        assertEquals(guest3.getGuestWaiting().size(), 1);
        assertEquals(guest3.getCurrentWaiting(), null);
    }

    @Test(expected = NoWaitingToAccept.class)
    public void 수락할_웨이팅이_없는_상황의_수락() throws Exception{
        //given
        Guest host = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant = createRestaurant(host, "맥도날드", 1);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));

        guestService.join(host);
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);
        restaurantService.register(restaurant);

        //when
        Long waitingId = waitingService.waiting(
                restaurant.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());
        Waiting waiting = waitingService.findOne(waitingId);

        restaurantService.acceptWaiting(restaurant.getId());
        restaurantService.acceptWaiting(restaurant.getId());

        //then
        fail();
    }

    @Test
    public void 다수의_웨이팅_수락() throws Exception{
        //given
        Guest host1 = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant1 = createRestaurant(host1, "맥도날드", 1);

        Guest host2 = createGuest("admin2", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant2 = createRestaurant(host2, "버거킹", 1);

        Guest leader1 = createGuest("leader1", 2, LocalDateTime.now().minusDays(21));
        Guest leader2 = createGuest("leader2", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));
        Guest guest4 = createGuest("guest4", 2, LocalDateTime.now().minusDays(21));
        Guest guest5 = createGuest("guest5", 2, LocalDateTime.now().minusDays(21));
        Guest guest6 = createGuest("guest6", 2, LocalDateTime.now().minusDays(21));

        guestService.join(host1);
        guestService.join(host2);
        guestService.join(leader1);
        guestService.join(leader2);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);
        guestService.join(guest4);
        guestService.join(guest5);
        guestService.join(guest6);
        restaurantService.register(restaurant1);
        restaurantService.register(restaurant2);

        //when
        Long waitingId1 = waitingService.waiting(
                restaurant1.getId(), leader1.getId(), guest1.getId(), guest2.getId(), guest3.getId());
        Waiting waiting1 = waitingService.findOne(waitingId1);
        Long waitingId2 = waitingService.waiting(
                restaurant1.getId(), leader2.getId(), guest4.getId(), guest5.getId(), guest6.getId());
        Waiting waiting2 = waitingService.findOne(waitingId2);

        restaurantService.acceptWaiting(restaurant1.getId());
        restaurantService.acceptWaiting(restaurant2.getId());


        //then
        assertEquals(leader1.getCurrentWaiting(), null);
        assertEquals(leader1.getLeading().size(), 1);
        assertEquals(leader2.getCurrentWaiting(), null);
        assertEquals(leader2.getLeading().size(), 1);
        assertEquals(guest1.getCurrentWaiting(), null);
        assertEquals(guest1.getGuestWaiting().size(), 1);
        assertEquals(guest2.getCurrentWaiting(), null);
        assertEquals(guest2.getGuestWaiting().size(), 1);
        assertEquals(guest3.getCurrentWaiting(), null);
        assertEquals(guest3.getGuestWaiting().size(), 1);
        assertEquals(guest4.getCurrentWaiting(), null);
        assertEquals(guest4.getGuestWaiting().size(), 1);
        assertEquals(guest5.getCurrentWaiting(), null);
        assertEquals(guest5.getGuestWaiting().size(), 1);
        assertEquals(guest6.getCurrentWaiting(), null);
        assertEquals(guest6.getGuestWaiting().size(), 1);

        assertNotEquals(waitingId1, waitingId2);
    }

    @Test
    public void 수락받은_이후_다른_식당_웨이팅() throws Exception{
        //given
        Guest host1 = createGuest("admin1", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant1 = createRestaurant(host1, "맥도날드", 1);

        Guest host2 = createGuest("admin2", 2, LocalDateTime.now().minusDays(21));
        Restaurant restaurant2 = createRestaurant(host2, "버거킹", 1);

        Guest leader = createGuest("leader", 2, LocalDateTime.now().minusDays(21));

        Guest guest1 = createGuest("guest1", 2, LocalDateTime.now().minusDays(21));
        Guest guest2 = createGuest("guest2", 2, LocalDateTime.now().minusDays(21));
        Guest guest3 = createGuest("guest3", 2, LocalDateTime.now().minusDays(21));

        guestService.join(host1);
        guestService.join(host2);
        guestService.join(leader);
        guestService.join(guest1);
        guestService.join(guest2);
        guestService.join(guest3);
        restaurantService.register(restaurant1);
        restaurantService.register(restaurant2);

        //when
        Long waitingId1 = waitingService.waiting(
                restaurant1.getId(), leader.getId(), guest1.getId(), guest2.getId(), guest3.getId());
        Waiting waiting1 = waitingService.findOne(waitingId1);
        Long acceptionId = restaurantService.acceptWaiting(restaurant1.getId());
        Acceptation acceptation = acceptationRepository.findById(acceptionId);
        Long waitingId2 = waitingService.waiting(
                restaurant1.getId(), guest1.getId(), leader.getId(), guest2.getId(), guest3.getId());
        Waiting waiting2 = waitingService.findOne(waitingId2);

        //then
        assertEquals(waiting1.getWaitingStatus(), WaitingStatus.ACCEPT);
        assertEquals(waiting2.getWaitingStatus(), WaitingStatus.WAITING);

        assertEquals(leader.getLeading().get(0).getRestaurant(), acceptation.getRestaurant());


        assertEquals(leader.getCurrentWaiting(), waiting2);
        assertEquals(leader.getLeading().get(0), waiting1);
        assertEquals(leader.getGuestWaiting().get(0).getWaiting(), waiting2);
        assertEquals(guest1.getCurrentWaiting(), waiting2);
        assertEquals(guest1.getLeading().get(0), waiting2);
        assertEquals(guest1.getGuestWaiting().get(0).getWaiting(), waiting1);
        assertEquals(guest2.getGuestWaiting().size(), 2);
        assertEquals(guest2.getCurrentWaiting(), waiting2);
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
}