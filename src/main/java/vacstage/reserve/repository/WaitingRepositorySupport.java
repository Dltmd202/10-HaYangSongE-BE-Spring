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
import vacstage.reserve.dto.waiting.WaitingDto;
import vacstage.reserve.dto.waiting.WaitingMemberDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static vacstage.reserve.domain.waiting.QWaiting.waiting;

@Repository
@RequiredArgsConstructor
public class WaitingRepositorySupport {

    private final EntityManager em;

    public void save(Waiting waiting){
        em.persist(waiting);
    }

    public WaitingDto findWaitingDto(Long id){
        WaitingDto baseWaitingDto = findBaseWaitingDto(id);
        baseWaitingDto.setMember(findWaitingMember(id));
        return baseWaitingDto;
    }

    private List<WaitingMemberDto> findWaitingMember(Long waitingId){
        return em.createQuery(
                "select new vacstage.reserve.dto.waiting.WaitingMemberDto(" +
                        "g.username) " +
                        " from GuestWaiting gw" +
                        " join gw.guest g" +
                        " where gw.waiting.id = :waitingId", WaitingMemberDto.class)
                .setParameter("waitingId", waitingId)
                .getResultList();
    }

    private WaitingDto findBaseWaitingDto(Long id){
        return em.createQuery(
                "select new vacstage.reserve.dto.waiting.WaitingDto(" +
                        "w.id, r.id, l.username, w.waitingStatus, w.date)" +
                        " from Waiting w" +
                        " join w.leader l" +
                        " join w.restaurant r" +
                        " where w.id = :id", WaitingDto.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public Optional<Waiting> findById(Long id){
        QWaiting waiting = QWaiting.waiting;

        JPAQueryFactory query = new JPAQueryFactory(em);
        Waiting findWaiting = query
                .select(waiting)
                .from(waiting)
                .where(waiting.id.eq(id))
                .fetch().get(0);
        if(findWaiting == null){
            return Optional.empty();
        }
        return Optional.ofNullable(findWaiting);
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
