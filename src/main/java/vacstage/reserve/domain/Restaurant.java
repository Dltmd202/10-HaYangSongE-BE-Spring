package vacstage.reserve.domain;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.constant.District;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.waiting.Waiting;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Restaurant {

    @Id @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_seq_generator"
    )
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest host;

    private String phoneNumber;

    private String branchName;

    @Enumerated(EnumType.STRING)
    private District district;

    private String detailAddress;

    private int waitingAverage;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    private String restaurantPhoto;

    @OneToMany(mappedBy = "restaurant")
    private List<Acceptation> acceptation = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Waiting> waitings = new ArrayList<>();

    private int vaccineCondition;

    private int totalSeat;

    private int remainSeat;

    public void setHost(Guest guest){
        this.host = guest;
        guest.setIsHost(true);
    }

    public void addMenu(Menu menu){
        menu.setRestaurant(this);
        menus.add(menu);
    }

    public void registerWaiting(Waiting waiting){
        waiting.setRestaurant(this);
        waitings.add(waiting);
    }

    /*
     * 생성 메서드
     */
    public static Restaurant createBaseRestaurant(Guest guest, List<Menu> menus){
        Restaurant restaurant = new Restaurant();
        restaurant.setHost(guest);
        guest.setIsHost(true);
        for(Menu menu: menus){
            restaurant.addMenu(menu);
        }
        return restaurant;
    }

    public static Restaurant createBaseRestaurant(Guest guest, Menu... menus){
        Restaurant restaurant = new Restaurant();
        restaurant.setHost(guest);
        guest.setIsHost(true);
        for(Menu menu: menus){
            restaurant.addMenu(menu);
        }
        return restaurant;
    }


}
