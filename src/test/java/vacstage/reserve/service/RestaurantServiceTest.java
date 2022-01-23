package vacstage.reserve.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.Menu;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RestaurantServiceTest {

    @Autowired GuestService guestService;
    @Autowired RestaurantService restaurantService;
    @Autowired WaitingService waitingService;


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