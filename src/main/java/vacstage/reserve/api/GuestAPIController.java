package vacstage.reserve.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vacstage.reserve.domain.guest.Guest;
import vacstage.reserve.domain.guest.GuestSearch;
import vacstage.reserve.dto.guest.*;
import vacstage.reserve.dto.wrapper.ApiListResponse;
import vacstage.reserve.dto.wrapper.ApiResponse;
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
    public ResponseEntity<ApiResponse<GuestDto>> find(@PathVariable("id") Long id) {
        Guest guest  = guestService.findOne(id);
        return ResponseEntity.ok(ApiResponse.of(new GuestDto(guest)));
    }

    @PostMapping("/guest")
    public ResponseEntity<ApiResponse<CreateGuestResponse>> signUp(
            @RequestBody @Valid CreateGuestRequest request){
        Long guestId = guestService.joinAPI(request);
        Guest guest = guestService.findOne(guestId);
        return ResponseEntity.ok(ApiResponse.of(new CreateGuestResponse(guest)));
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
                guest.getId(),
                guest.getUsername(),
                principal.getToken(),
                guest.getAuthorities()
        ));

    }

    @GetMapping("/guest/mypage")
    public ResponseEntity<ApiResponse<GuestDto>> myPage(
            @AuthenticationPrincipal JwtAuthentication token
    ){
        Guest guest = guestService.findOne(token.getId());
        return ResponseEntity.ok(ApiResponse.of(new GuestDto(guest)));
    }

    @GetMapping("/guest")
    public ResponseEntity<ApiListResponse<List<GuestDto>>> list(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ){
        List<Guest> guests = guestService.findGuests(new GuestSearch(), offset, limit);
        List<GuestDto> guestDtos = guests.stream()
                .map(GuestDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiListResponse.of(guestDtos, offset, limit));
    }



}
