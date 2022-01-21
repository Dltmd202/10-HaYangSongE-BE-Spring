package vacstage.reserve.dto;

import vacstage.reserve.domain.Menu;
import vacstage.reserve.domain.Restaurant;

import javax.persistence.*;

public class RestaurantMenuDto {

    private Long id;

    private Restaurant restaurant;

    private Menu menu;

}
