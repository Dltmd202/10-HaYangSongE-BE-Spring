package vacstage.reserve.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Guest {

    @Id @GeneratedValue
    @Column(name = "guest_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private String fullName;

    private int vaccineStep;

    private LocalDateTime vaccineDate;

    private String phoneNumber;

    private Boolean isStaff;

    private Boolean isHost;
}
