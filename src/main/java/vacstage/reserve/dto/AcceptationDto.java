package vacstage.reserve.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import vacstage.reserve.domain.Restaurant;
import vacstage.reserve.domain.waiting.Waiting;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class AcceptationDto {

    private Long id;

    private Restaurant restaurant;

    private Waiting waiting;

    private LocalDateTime admission_date;
}
