package vacstage.reserve.domain;

import lombok.Getter;
import lombok.Setter;
import vacstage.reserve.domain.guest.Guest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Restaurant {

    @Id @GeneratedValue
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest host;

    private String phoneNumber;

    private String branchName;

    private String district;

    private String detailAddress;

    private String waitingAverage;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    private String restaurantPhoto;

    @OneToMany(mappedBy = "restaurant")
    private List<Acceptation> acceptation;

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


}
