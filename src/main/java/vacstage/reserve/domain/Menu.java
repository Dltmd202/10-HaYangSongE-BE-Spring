package vacstage.reserve.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Menu {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;

    private int price;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public static Menu createMenu(String name, int price){
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        return menu;
    }

}
