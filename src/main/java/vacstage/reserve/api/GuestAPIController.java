package vacstage.reserve.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;
import vacstage.reserve.dto.guest.*;
import vacstage.reserve.jwt.JwtAuthentication;
import vacstage.reserve.jwt.JwtAuthenticationToken;
import vacstage.reserve.service.GuestService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class GuestAPIController {

    private final GuestService guestService;

    private final AuthenticationManager authenticationManager;

    @Operation(summary = "게스트 탐색")
    @GetMapping("/guest/{id}")
    public GuestDto find(@PathVariable("id") Long id) {
        Guest guest  = guestService.findOne(id);
        return new GuestDto(guest);
    }

    @PostMapping("/guest")
    public CreateGuestResponse signUp(
            @RequestBody @Valid CreateGuestRequest request){
        Long guestId = guestService.joinAPI(request);
        Guest guest = guestService.findOne(guestId);
        return new CreateGuestResponse(guest);
    }

    @Operation(summary = "JWT 토큰 발행 로그인")
    @PostMapping("/guest/login")
    public ResponseEntity<GuestToken> signInAPI(
            @RequestBody GuestSignInRequest guestSignInRequest
            ){
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(
                guestSignInRequest.getUsername(),
                guestSignInRequest.getPassword());
        System.out.println(authenticationManager);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authenticate;
        JwtAuthentication principal = (JwtAuthentication) jwtAuthenticationToken.getPrincipal();
        Guest guest = (Guest) jwtAuthenticationToken.getDetails();

        return ResponseEntity.ok(new GuestToken(
                guest.getUsername(),
                principal.getToken(),
                guest.getAuthorities()
        ));

    }

    @GetMapping("/guest")
    public List<GuestDto> list(){
        List<Guest> guests = guestService.findGuests(new GuestSearch());
        return guests.stream()
                .map(GuestDto::new)
                .collect(Collectors.toList());
    }



}
