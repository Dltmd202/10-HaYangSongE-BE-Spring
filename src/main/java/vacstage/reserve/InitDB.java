package vacstage.reserve;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.constant.District;
import vacstage.reserve.domain.Menu;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Authority;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.service.GuestService;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() throws IOException, ParseException {
        initService.guestInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        private final GuestService guestService;

        public void guestInit() throws IOException, ParseException {
            JSONParser parser = new JSONParser();
            Reader reader = new FileReader("src/main/resources/static/mockUp/service_guest.json");
            JSONArray guests = (JSONArray) parser.parse(reader);
            int guestCount = 0;

            for(int i = 0; i < guests.size(); i ++) {
                Guest guest = new Guest();

                JSONObject guestInformation = (JSONObject) guests.get(i);
                guest.setUsername((String) guestInformation.get("username"));
                guest.setPassword((String) guestInformation.get("password"));
                guest.setIsStaff((Long) guestInformation.get("is_staff") == 1L);
                guest.setFullName((String) guestInformation.get("full_name"));
                guest.setIsHost((Long) guestInformation.get("is_host") == 1L);
                guest.setAuthority(Authority.HOST);
                guestService.join(guest);
            }

            reader = new FileReader("src/main/resources/static/mockUp/service_restaurant.json");
            JSONArray restaurants = (JSONArray) parser.parse(reader);

            for(int i = 0; i < restaurants.size(); i ++) {
                Restaurant restaurant = new Restaurant();

                JSONObject restaurantInformation = (JSONObject) restaurants.get(i);
                restaurant.setName((String) restaurantInformation.get("name"));
                restaurant.setPhoneNumber((String) restaurantInformation.get("phone_number"));
                restaurant.setBranchName((String) restaurantInformation.get("branch_name"));
                restaurant.setDistrict(
                        restaurantInformation.get("district").toString().equals("SE") ? District.SE : District.WN);
                restaurant.setDetailAddress((String) restaurantInformation.get("detail_address"));
                restaurant.setWaitingAverage(Math.toIntExact((Long) restaurantInformation.get("is_host")));
                restaurant.setRestaurantPhoto((String) restaurantInformation.get("restaurant_photo"));
                restaurant.setVaccineCondition(Math.toIntExact((Long) restaurantInformation.get("is_host")));
                restaurant.setTotalSeat(Math.toIntExact((Long) restaurantInformation.get("total_seat")));
                restaurant.setRemainSeat(Math.toIntExact((Long) restaurantInformation.get("remain_seat")));

                Guest guest = em.find(Guest.class, (Long) restaurantInformation.get("host_id"));
                restaurant.setHost(guest);

                em.persist(restaurant);
            }

            reader = new FileReader("src/main/resources/static/mockUp/service_menu.json");
            JSONArray menus = (JSONArray) parser.parse(reader);

            for(int i = 0; i < menus.size(); i ++) {
                Menu menu = new Menu();

                JSONObject menuInformation = (JSONObject) menus.get(i);
                menu.setName((String) menuInformation.get("name"));
                try {
                    menu.setPrice(Math.toIntExact((Long) menuInformation.get("price")));
                } catch (NullPointerException ex){
                    menu.setPrice(0);
                }

                em.persist(menu);
            }

            reader = new FileReader("src/main/resources/static/mockUp/service_restaurant_menu.json");
            JSONArray restaurantMenus = (JSONArray) parser.parse(reader);

            for(int i = 0; i < restaurantMenus.size(); i ++) {
                JSONObject restaurantMenu = (JSONObject) restaurantMenus.get(i);

                Restaurant restaurant = em.find(Restaurant.class, (Long) restaurantMenu.get("restaurant_id"));
                Menu menu = em.find(Menu.class, restaurantMenu.get("menu_id"));

                menu.setRestaurant(restaurant);
                restaurant.getMenus().add(menu);

                em.persist(restaurant);
                em.persist(menu);
            }

        }



    }
}
