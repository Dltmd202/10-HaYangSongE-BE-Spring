package vacstage.reserve.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;
import vacstage.reserve.domain.guest.QGuest;

import javax.persistence.EntityManager;
import java.util.List;

import static vacstage.reserve.domain.guest.QGuest.guest;


@Repository
@RequiredArgsConstructor
public class GuestRepositoryCustomImpl implements GuestRepositoryCustom{

    private final EntityManager em;

    public List<Guest> findAll(GuestSearch guestSearch) {
        QGuest quest = guest;


        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .select(guest)
                .from(guest)
                .where(usernameLike(guestSearch.getUsername()), fullNameLike(guestSearch.getFullName()))
                .limit(1000)
                .fetch();
    }

    public List<Guest> findAll(GuestSearch guestSearch, int offset, int limit) {
        QGuest quest = guest;


        JPAQueryFactory query = new JPAQueryFactory(em);
        return query
                .select(guest)
                .from(guest)
                .where(usernameLike(guestSearch.getUsername()), fullNameLike(guestSearch.getFullName()))
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    private BooleanExpression usernameLike(String username) {
        if(!StringUtils.hasText(username)){
            return null;
        }
        return guest.username.like(username);
    }

    private BooleanExpression fullNameLike(String fullName){
        if(!StringUtils.hasText(fullName)){
            return null;
        }
        return guest.fullName.like(fullName);
    }
}