package vacstage.reserve.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;
import vacstage.reserve.dto.guest.GuestDto;
import vacstage.reserve.repository.GuestRepository;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class GuestServiceTest {

    @Autowired GuestService guestService;
    @Autowired GuestRepository guestRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Guest guest1 = new Guest();
        guest1.setUsername("admin1");
        guest1.setFullName("이승환");
        guest1.setVaccineStep(2);
        guest1.setPassword("1234");
        guest1.setPhoneNumber("010-1234-1234");

        Guest guest2 = new Guest();
        guest2.setUsername("admin2");
        guest2.setFullName("이승환");
        guest2.setVaccineStep(1);
        guest2.setPassword("4321");
        guest2.setPhoneNumber("010-1234-1234");

        //when
        Long saveId1 = guestService.join(guest1);
        Long saveId2 = guestService.join(guest2);
        Guest findGuest1 = guestService.findOne(saveId1);
        Guest findGuest2 = guestService.findOne(saveId2);
        List<Guest> guests = guestService.findGuests(new GuestSearch());

        //then
        assertEquals(guest1,
                guestRepository.findById(saveId1));
        assertEquals(guest1, findGuest1);

        assertEquals(guest2,
                guestRepository.findById(saveId2));
        assertEquals(guest2, findGuest2);

        assertNotEquals(guest1.getId(), guest2.getId());


        assertEquals(guests.size(), 2);

        assertEquals(findGuest1.getUsername(), "admin1");
        assertEquals(findGuest1.getFullName(), "이승환");
        assertEquals(findGuest1.getVaccineStep(), 2);
        assertEquals(findGuest1.getPassword(), "1234");
        assertEquals(findGuest1.getPhoneNumber(), "010-1234-1234");

        assertEquals(findGuest2.getUsername(), "admin2");
        assertEquals(findGuest2.getFullName(), "이승환");
        assertEquals(findGuest2.getVaccineStep(), 1);
        assertEquals(findGuest2.getPassword(), "4321");
        assertEquals(findGuest2.getPhoneNumber(), "010-1234-1234");


    }

    @Test(expected = IllegalStateException.class)
    public void 중복_게스트_예외() throws Exception{
        //given
        Guest guest1 = new Guest();
        guest1.setUsername("admin");
        guest1.setFullName("이승환");
        guest1.setVaccineStep(2);
        guest1.setPassword("1234");
        guest1.setPhoneNumber("010-1234-1234");

        Guest guest2 = new Guest();
        guest2.setUsername("admin");
        guest2.setFullName("하유민");
        guest2.setVaccineStep(1);
        guest2.setPassword("1234");
        guest2.setPhoneNumber("010-1234-1234");

        //when
        guestService.join(guest1);
        guestService.join(guest2);

        //then
        fail("중복 회원 예외");
    }

    @Test
    public void 게스트_개별_조회() {
        //given
        Guest guest1 = new Guest();
        guest1.setUsername("admin1");
        guest1.setFullName("이승환");
        guest1.setVaccineStep(2);
        guest1.setPassword("1234");
        guest1.setPhoneNumber("010-1234-1234");

        Guest guest2 = new Guest();
        guest2.setUsername("admin2");
        guest2.setFullName("하유민");
        guest2.setVaccineStep(1);
        guest2.setPassword("1234");
        guest2.setPhoneNumber("010-1234-1234");

        //when
        Long guestId1 = guestService.join(guest1);
        Long guestId2 = guestService.join(guest2);
        Guest findGuest1 = guestService.findOne(guestId1);
        Guest findGuest2 = guestService.findOne(guestId2);

        //then
        assertEquals(findGuest1, guest1);
        assertEquals(findGuest2, guest2);
    }

    @Test
    public void 게스트_조건_조회() {
        //given
        Guest guest1 = new Guest();
        guest1.setUsername("admin");
        guest1.setFullName("이승환");
        guest1.setVaccineStep(2);
        guest1.setPassword("1234");
        guest1.setPhoneNumber("010-1234-1234");

        Guest guest2 = new Guest();
        guest2.setUsername("admin2");
        guest2.setFullName("이승환");
        guest2.setVaccineStep(1);
        guest2.setPassword("1234");
        guest2.setPhoneNumber("010-1234-1234");

        GuestSearch guestSearch = new GuestSearch();

        //when
        guestService.join(guest1);
        guestService.join(guest2);

        //then

        List<Guest> findGuests = guestService.findGuests(guestSearch);
        assertEquals(findGuests.size(), 2);

        guestSearch.setUsername("admin");
        List<Guest> findUsernameGuests = guestService.findGuests(guestSearch);
        assertEquals(findUsernameGuests.size(), 1);
        guestSearch.setUsername(null);

        guestSearch.setFullName("이승환");
        List<Guest> findFullNameGuests = guestService.findGuests(guestSearch);
        assertEquals(findFullNameGuests.size(), 2);
    }

    @Test
    public void 게스트_업데이트() throws Exception{
        //given
        Guest guest1 = new Guest();
        guest1.setUsername("admin");
        guest1.setFullName("이승환");
        guest1.setVaccineStep(2);
        guest1.setPassword("1234");
        guest1.setPhoneNumber("010-1234-1234");

        GuestDto guestUpdateDto = new GuestDto();

        //when
        guestService.join(guest1);

        guestUpdateDto.setUsername("하유민");
        guestUpdateDto.setVaccine_step(1);
        guestUpdateDto.setPhone_number("010-4321-4321");

        //then
        guest1.updateGuestInformation(guestUpdateDto);
        assertEquals(guest1.getUsername(), "하유민");
        assertEquals(guest1.getVaccineStep(), 1);
        assertEquals(guest1.getPhoneNumber(), "010-4321-4321");
    }

    @Test(expected = IllegalStateException.class)
    public void 게스트_업데이트_데이터_중복() {
        //when
        Guest guest1 = new Guest();
        guest1.setUsername("admin");
        guest1.setFullName("이승환");
        guest1.setVaccineStep(2);
        guest1.setPassword("1234");
        guest1.setPhoneNumber("010-1234-1234");

        Guest guest2 = new Guest();
        guest2.setUsername("admin2");
        guest2.setFullName("이승환");
        guest2.setVaccineStep(1);
        guest2.setPassword("1234");
        guest2.setPhoneNumber("010-1234-1234");

        GuestDto guestUpdateDto = new GuestDto();

        //given
        guestService.join(guest1);
        guestService.join(guest2);

        //then
        guestUpdateDto.setUsername("admin2");
        guestService.update(guest1.getId(), guestUpdateDto);
    }

    @Test
    public void 백신_접종_경과일() throws Exception{
        //given

        //when

        //then
        fail();
    }

}