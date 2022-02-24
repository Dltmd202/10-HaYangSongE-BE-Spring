package vacstage.reserve.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import vacstage.reserve.dto.restaurant.*;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RestaurantRepositorySupport {

    private final EntityManager em;

    private final RestaurantRepository restaurantRepository;

    private RestaurantDto findBaseRestaurantDto(Long id){
        return em.createQuery(
                "select new vacstage.reserve.dto.restaurant.RestaurantDto(" +
                        "r.id, r.name, r.phoneNumber, r.branchName, r.district, r.detailAddress, r.waitingAverage," +
                        "r.restaurantPhoto, r.vaccineCondition, r.totalSeat, r.remainSeat, h)" +
                        " from Restaurant r" +
                        " join r.host h" +
                        " where r.id = :id", RestaurantDto.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    private List<RestaurantDto> findQueryRestaurantDtos(){
        return em.createQuery(
                        "select new vacstage.reserve.dto.restaurant.RestaurantDto(" +
                                "r.id, r.name, r.phoneNumber, r.branchName, r.district, r.detailAddress, r.waitingAverage," +
                                "r.restaurantPhoto, r.vaccineCondition, r.totalSeat, r.remainSeat, h)" +
                                " from Restaurant r" +
                                " join r.host h ", RestaurantDto.class)
                .getResultList();
    }

    private List<RestaurantListDto> findQueryRestaurantDtos(int offset, int limit, String key, String district){
        String query = "select new vacstage.reserve.dto.restaurant.RestaurantListDto(r)" +
                " from Restaurant r" +
                " where r.district in ";

        if(district.equals("SE")){
            query += " (vacstage.reserve.constant.District.SE) ";
        } else if(district.equals("WN")){
            query += " (vacstage.reserve.constant.District.WN) ";
        } else {
            query += " (vacstage.reserve.constant.District.WN, vacstage.reserve.constant.District.SE) ";
        }
        if(!key.equals("")){
            query += " and r.name like :key";
            return em.createQuery(
                            query, RestaurantListDto.class)
                    .setParameter("key", "%" + key + "%")
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        } else {
            return em.createQuery(
                            query, RestaurantListDto.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
        }
    }

    private List<MenuDto> findMenu(Long restaurantId){
        return em.createQuery(
                "select new vacstage.reserve.dto.restaurant.MenuDto(m.id, m.name, m.price)" +
                        " from Menu m" +
                        " where m.restaurant.id = :restaurantId", MenuDto.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
    }

    private List<RestaurantAcceptationDto> findAcceptation(Long restaurantId){
        List<RestaurantAcceptationDto> acceptationDtos = em.createQuery(
                        "select new vacstage.reserve.dto.restaurant.RestaurantAcceptationDto(" +
                                "a.admission_date, a.waiting.id, a.waiting.date) " +
                                " from Acceptation a" +
                                " where a.restaurant.id = :restaurantId", RestaurantAcceptationDto.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
        acceptationDtos.forEach(a -> {
            a.setLeader(findWaitingLeaderDto(a.getWaiting_id()));
            a.setMember(findWaitingGuestDto(a.getWaiting_id()));
        });
        return acceptationDtos;
    }

    public List<WaitingGuestDto> findWaitingGuestDto(Long waitingId){
        return em.createQuery(
                "select new vacstage.reserve.dto.restaurant.WaitingGuestDto(g.id, g.username) " +
                        " from GuestWaiting gw" +
                        " join gw.guest g" +
                        " where gw.waiting.id = :waitingId", WaitingGuestDto.class)
                .setParameter("waitingId", waitingId)
                .getResultList();
    }

    public WaitingGuestDto findWaitingLeaderDto(Long waitingId){
        return em.createQuery(
                        "select new vacstage.reserve.dto.restaurant.WaitingGuestDto(g.id, g.username) " +
                                " from GuestWaiting gw" +
                                " join gw.guest g" +
                                " where gw.waiting.id = :waitingId", WaitingGuestDto.class)
                .setParameter("waitingId", waitingId)
                .getSingleResult();
    }

    public List<RestaurantWaitingDto> findWaitingDtos(Long restaurantId){
        List<RestaurantWaitingDto> restaurantWaitingDtos = em.createQuery(
                        "select new vacstage.reserve.dto.restaurant.RestaurantWaitingDto(w.id, w.date)" +
                                " from Waiting w" +
                                " where w.restaurant.id = :restaurantId", RestaurantWaitingDto.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList();
        restaurantWaitingDtos.forEach(w -> {
            w.setLeader(findWaitingLeaderDto(w.getId()));
            w.setMember(findWaitingGuestDto(w.getId()));
        });
        return restaurantWaitingDtos;
    }


    public RestaurantDto findOneRestaurantDto(Long id){
        RestaurantDto baseRestaurantDto = findBaseRestaurantDto(id);
        baseRestaurantDto.setMenus(findMenu(id));
        baseRestaurantDto.setAcceptation(findAcceptation(id));
        baseRestaurantDto.setWaitings(findWaitingDtos(id));
        return baseRestaurantDto;
    }

    public List<RestaurantDto> findRestaurantDtos(){
        List<RestaurantDto> baseRestaurantDtos = findQueryRestaurantDtos();
        baseRestaurantDtos.forEach(r -> {
            r.setMenus(findMenu(r.getId()));
            r.setAcceptation(findAcceptation(r.getId()));
            r.setWaitings(findWaitingDtos(r.getId()));
        });
        return baseRestaurantDtos;
    }


    public List<RestaurantListDto> findRestaurantListDtos(int offset, int limit, String key, String district) {
        List<RestaurantListDto> baseRestaurantDtos = findQueryRestaurantDtos(offset, limit, key, district);
        return baseRestaurantDtos;
    }
}
