package vacstage.reserve.dto.waiting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterWaitingDto {

    @NotNull
    private Long restaurant;

    @NotBlank
    private String leader;

    private List<WaitingMemberDto> member;

}
