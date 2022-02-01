package vacstage.reserve.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import vacstage.reserve.dto.guest.CreateGuestRequest;
import vacstage.reserve.dto.guest.GuestSignInRequest;
import vacstage.reserve.repository.GuestRepository;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GuestAPIControllerTest {

    @Autowired public ObjectMapper objectMapper;
    @Autowired public GuestRepository guestRepository;
    @Autowired private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        String fullName1 = "tester1";
        String username1 = "test1";
        String password1 = "1234";
        int vaccineStep1 = 2;
        String phoneNumber1 = "010-4321-4321";
        LocalDateTime vaccineDate1 = LocalDateTime.now().minusDays(21);

        String fullName2 = "tester2";
        String username2 = "test2";
        String password2 = "1234";
        int vaccineStep2 = 1;
        String phoneNumber2 = "010-5678-5678";
        LocalDateTime vaccineDate2 = LocalDateTime.now().minusDays(14);

        String createGuestRequest1 = objectMapper.
                writeValueAsString(CreateGuestRequest.builder()
                        .full_name(fullName1)
                        .username(username1)
                        .password(password1)
                        .vaccine_step(vaccineStep1)
                        .vaccine_date(vaccineDate1)
                        .phone_number(phoneNumber1)
                        .build());

        String createGuestRequest2 = objectMapper.
                writeValueAsString(CreateGuestRequest.builder()
                        .full_name(fullName2)
                        .username(username2)
                        .password(password2)
                        .vaccine_step(vaccineStep2)
                        .vaccine_date(vaccineDate2)
                        .phone_number(phoneNumber2)
                        .build());



        mockMvc.perform(post("/guest")
                .content(createGuestRequest1)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON));

        mockMvc.perform(post("/guest")
                .content(createGuestRequest2)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON));
    }

    @After
    public void tearDown() throws Exception {
        guestRepository.deleteAll();
    }


    @Test
    public void 회원가입() throws Exception{
        //given
        String fullName = "이승환";
        String username = "admin";
        String password = "1234";
        int vaccineStep = 2;
        LocalDateTime vaccineDate = LocalDateTime.now().minusDays(21);
        String phoneNumber = "010-1234-1234";

        String createGuestRequest = objectMapper.
                writeValueAsString(CreateGuestRequest.builder()
                        .full_name(fullName)
                        .username(username)
                        .password(password)
                        .vaccine_step(vaccineStep)
                        .vaccine_date(vaccineDate)
                        .phone_number(phoneNumber)
                        .build());
        //when
        mockMvc.perform(post("/guest")
                        .content(createGuestRequest)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value(fullName))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.vaccine_step").value(vaccineStep))
                .andExpect(jsonPath("$.phone_number").value(phoneNumber))
                .andExpect(jsonPath("$.vaccine_elapsed").value(21));
    }

    @Test
    @WithMockUser
    public void 게스트_조회() throws Exception{
        //given
        Long id = 1L;
        String fullName = "tester1";
        String username = "test1";
        int vaccineStep = 2;
        String phoneNumber = "010-4321-4321";

        //when
        mockMvc.perform(get("/guest/" + id)
                .accept(APPLICATION_JSON))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.full_name").value(fullName))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.vaccine_step").value(vaccineStep))
                .andExpect(jsonPath("$.phone_number").value(phoneNumber))
                .andExpect(jsonPath("$.vaccine_elapsed").value(21));
    }

    @Test
    @WithMockUser
    public void 게스트_리스트_조회() throws Exception{
        //given
        String fullName1 = "tester1";
        String username1 = "test1";
        int vaccineStep1 = 2;
        String phoneNumber1 = "010-4321-4321";

        String fullName2 = "tester2";
        String username2 = "test2";
        int vaccineStep2 = 1;
        String phoneNumber2 = "010-5678-5678";

        //when
        mockMvc.perform(get("/guest/")
                .accept(APPLICATION_JSON))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].full_name").value(fullName1))
                .andExpect(jsonPath("$.[0].username").value(username1))
                .andExpect(jsonPath("$.[0].vaccine_step").value(vaccineStep1))
                .andExpect(jsonPath("$.[0].phone_number").value(phoneNumber1))
                .andExpect(jsonPath("$.[0].vaccine_elapsed").value(21))
                .andExpect(jsonPath("$.[1].full_name").value(fullName2))
                .andExpect(jsonPath("$.[1].username").value(username2))
                .andExpect(jsonPath("$.[1].vaccine_step").value(vaccineStep2))
                .andExpect(jsonPath("$.[1].phone_number").value(phoneNumber2))
                .andExpect(jsonPath("$.[1].vaccine_elapsed").value(14));
    }

    @Test
    public void 로그인() throws Exception{
        //given
        String username = "test1";
        String password = "1234";

        String singInRequest = objectMapper.
                writeValueAsString(GuestSignInRequest.builder()
                        .username(username)
                        .password(password)
                        .build());
        //when
        mockMvc.perform(post("/guest/login")
                .content(singInRequest)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON))
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.authorities.[0].authority").value("USER"));
    }

}