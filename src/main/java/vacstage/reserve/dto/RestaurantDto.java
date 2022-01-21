package vacstage.reserve.dto;

import vacstage.reserve.domain.Acceptation;
import vacstage.reserve.domain.Guest;
import vacstage.reserve.domain.RestaurantMenu;
import vacstage.reserve.domain.Waiting;

import java.util.List;

public class RestaurantDto {

    private Long id;

    private String name;

    private Guest host;

    private String phoneNumber;

    private String branchName;

    private String district;

    private String detailAddress;

    private String waitingAverage;

    private List<RestaurantMenu> restaurantMenu;

    private String restaurantPhoto;

    private List<Acceptation> acceptation;

    private List<Waiting> waitings;

    private int vaccineCondition;

    private int totalSeat;

    private int remainSeat;


}
