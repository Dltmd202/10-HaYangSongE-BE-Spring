package vacstage.reserve.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import vacstage.reserve.domain.Menu;

@Data
@Builder
@AllArgsConstructor
public class MenuDto {

    private Long id;

    private String name;

    private int price;

    public MenuDto(Menu menu){
        name = menu.getName();
        price = menu.getPrice();
    }



}
