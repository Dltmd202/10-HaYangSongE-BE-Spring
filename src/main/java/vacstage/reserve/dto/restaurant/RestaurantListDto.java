package vacstage.reserve.dto.restaurant;

import lombok.Getter;
import vacstage.reserve.domain.Restaurant;

@Getter
public class RestaurantListDto {

    private Long id;
    private String name;
    private String phoneNumber;
    private String branchName;
    private String district;
    private String detailAddress;
    private int waitingAverage;
    private String restaurantPhoto;
    private int vaccineCondition;
    private int totalSeat;
    private int remainSeat;

    public RestaurantListDto(Long id, String name, String phoneNumber, String branchName, String district, String detailAddress, String waitingAverage, String restaurantPhoto, int vaccineCondition, int totalSeat, int remainSeat) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.branchName = branchName;
        this.district = district;
        this.detailAddress = detailAddress;
        this.waitingAverage = Integer.parseInt(waitingAverage);
        this.restaurantPhoto = restaurantPhoto;
        this.vaccineCondition = vaccineCondition;
        this.totalSeat = totalSeat;
        this.remainSeat = remainSeat;
    }

    public RestaurantListDto(Restaurant restaurant){
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.branchName = restaurant.getBranchName();
        this.district = restaurant.getDistrict().toString();
        this.detailAddress = restaurant.getDetailAddress();
        this.waitingAverage = restaurant.getWaitingAverage();
        this.restaurantPhoto = restaurant.getRestaurantPhoto();
        this.vaccineCondition = restaurant.getVaccineCondition();
        this.totalSeat = restaurant.getTotalSeat();
        this.remainSeat = restaurant.getRemainSeat();
    }
}
