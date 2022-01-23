package vacstage.reserve.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;
import vacstage.reserve.dto.guest.GuestDto;
import vacstage.reserve.repository.GuestRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;

    @Transactional
    public Long join(Guest guest){
        validateDuplicatedGuest(guest);
        guestRepository.save(guest);
        return guest.getId();
    }

    private void validateDuplicatedGuest(Guest guest) throws IllegalStateException {
        GuestSearch guestSearch = new GuestSearch();
        guestSearch.setUsername(guest.getUsername());
        List<Guest> findGuests = guestRepository.findAll(guestSearch);
        if(!findGuests.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    public Guest findOne(Long guestId){
        return guestRepository.findById(guestId);
    }

    public List<Guest> findGuests(GuestSearch guestSearch) {
        return guestRepository.findAll(guestSearch);
    }

    @Transactional
    public void update(Long id, GuestDto request){
        Guest guest = guestRepository.findById(id);
        guest.updateGuestInformation(request);
        validateDuplicatedGuest(guest);
    }


}
