package vacstage.reserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vacstage.reserve.domain.Menu;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final EntityManager em;

    public void save(Menu menu){
        em.persist(menu);
    }

    public Menu findById(Long id){
        return em.find(Menu.class, id);
    }
}
