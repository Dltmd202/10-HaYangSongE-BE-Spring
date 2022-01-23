package vacstage.reserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vacstage.reserve.domain.waiting.Waiting;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingRepository {

    private final EntityManager em;

    public void save(Waiting waiting){
        em.persist(waiting);
    }

    public Waiting findById(Long id){
        return em.find(Waiting.class, id);
    }

    public List<Waiting> findAll(){
        return em.createQuery("select w from Waiting w", Waiting.class)
                .getResultList();
    }
}
