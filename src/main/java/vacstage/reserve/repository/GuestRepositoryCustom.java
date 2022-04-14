package vacstage.reserve.repository;

import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;

import java.util.List;

public interface GuestRepositoryCustom {

    List<Guest> findAll(GuestSearch guestSearch);

    List<Guest> findAll(GuestSearch guestSearch, int offset, int limit);

}
