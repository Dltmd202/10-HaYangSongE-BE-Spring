package vacstage.reserve.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import vacstage.reserve.constant.WaitingStatus;
import vacstage.reserve.domain.waiting.QWaiting;
import vacstage.reserve.domain.waiting.Waiting;
import vacstage.reserve.domain.waiting.WaitingSearch;

import javax.persistence.EntityManager;
import java.util.List;

import static vacstage.reserve.domain.waiting.QWaiting.waiting;

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

    public List<Waiting> findAll(WaitingSearch waitingSearch){
        QWaiting waiting = QWaiting.waiting;

        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .select(waiting)
                .from(waiting)
                .where(waitingStatusLike(waitingSearch.getWaitingStatus()),
                        waiting.date.after(waitingSearch.getSearchFromDateTime()),
                        waiting.date.before(waitingSearch.getSearchToDateTime()))
                .limit(waitingSearch.getLimit())
                .fetch();
    }

    private BooleanExpression waitingStatusLike(WaitingStatus waitingStatus){
        if(ObjectUtils.isEmpty(waitingStatus)){
            return null;
        }
        return waiting.waitingStatus.eq(waitingStatus);
    }
}
