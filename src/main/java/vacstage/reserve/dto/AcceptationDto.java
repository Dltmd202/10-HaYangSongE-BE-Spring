package vacstage.reserve.dto;

import lombok.*;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.Waiting;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class AcceptationDto {

    private Long id;

    private Restaurant restaurant;

    private Waiting waiting;

    private LocalDateTime admission_date;
}
