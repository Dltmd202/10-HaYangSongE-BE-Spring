package vacstage.reserve.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import vacstage.reserve.constant.District;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.guest.Guest;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Data
public class RestaurantDto {

    private Long id;
    private String name;
    private RestaurantHostDto host;
    private String phoneNumber;
    private String branchName;
    private String district;
    private String detailAddress;
    private int waitingAverage;
    private List<MenuDto> menus;
    private String restaurantPhoto;
    private List<RestaurantAcceptationDto> acceptation;
    private List<RestaurantWaitingDto> waitings;
    private int vaccineCondition;
    private int totalSeat;
    private int remainSeat;

    public RestaurantDto(Restaurant restaurant){
        id = restaurant.getId();
        name = restaurant.getName();
        host = new RestaurantHostDto(restaurant.getHost().getId(), restaurant.getHost().getUsername());
        phoneNumber = restaurant.getPhoneNumber();
        branchName = restaurant.getBranchName();
        district = restaurant.getDistrict().toString();
        detailAddress = restaurant.getDetailAddress();
        waitingAverage = restaurant.getWaitingAverage();
        menus = restaurant.getMenus().stream()
                .map(MenuDto::new)
                .collect(Collectors.toList());
        restaurantPhoto = restaurant.getRestaurantPhoto();
        acceptation = restaurant.getAcceptation().stream()
                .map(RestaurantAcceptationDto::new)
                .collect(Collectors.toList());
        waitings = restaurant.getWaitings().stream()
                .map(RestaurantWaitingDto::new)
                .collect(Collectors.toList());
        vaccineCondition = restaurant.getVaccineCondition();
        totalSeat = restaurant.getTotalSeat();
        remainSeat = restaurant.getRemainSeat();
    }

    public RestaurantDto(
            Long id,
            String name,
            String phoneNumber,
            String branchName,
            District district,
            String detailAddress,
            int waitingAverage,
            String restaurantPhoto,
            int vaccineCondition,
            int totalSeat,
            int remainSeat,
            Guest host) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.branchName = branchName;
        this.district = district.toString();
        this.detailAddress = detailAddress;
        this.waitingAverage = waitingAverage;
        this.restaurantPhoto = restaurantPhoto;
        this.vaccineCondition = vaccineCondition;
        this.totalSeat = totalSeat;
        this.remainSeat = remainSeat;
        this.host = new RestaurantHostDto(host.getId(), host.getUsername());
    }
}
