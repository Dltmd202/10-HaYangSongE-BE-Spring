package vacstage.reserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vacstage.reserve.domain.Restaurant;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantRepository {

    private final EntityManager em;

    public void save(Restaurant restaurant){
        em.persist(restaurant);
    }

    public Restaurant findById(Long id){
        return em.find(Restaurant.class, id);
    }

    public List<Restaurant> findAll(){
        return em.createQuery("select r from Restaurant r", Restaurant.class)
                .getResultList();
    }


}
