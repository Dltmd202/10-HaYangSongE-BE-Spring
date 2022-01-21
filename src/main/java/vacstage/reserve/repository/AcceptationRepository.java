package vacstage.reserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vacstage.reserve.domain.Acceptation;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class AcceptationRepository {

    private final EntityManager em;

    public void save(Acceptation acceptation){
        em.persist(acceptation);
    }

    public Acceptation findById(Long id){
        return em.find(Acceptation.class, id);
    }
}
