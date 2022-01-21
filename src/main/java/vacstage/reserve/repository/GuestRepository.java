package vacstage.reserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vacstage.reserve.domain.Guest;

import javax.persistence.EntityManager;
import java.lang.reflect.Member;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GuestRepository {

    private final EntityManager em;

    public void save(Guest guest){
        em.persist(guest);
    }

    public Guest findById(Long id){
        return em.find(Guest.class, id);
    }

    public List<Guest> findByFullUsername(String username){
        return em.createQuery(
                "select guest from Guest guest" +
                        " where guest.username = :username", Guest.class)
                .setParameter("username", username)
                .getResultList();
    }



}
