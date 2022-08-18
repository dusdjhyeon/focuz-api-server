package dcom.focuz.api.domain.study;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "study", timeToLive = 180)
public class TempStudy {
    @Id
    private String id;
    private Integer userId;
    private Integer studyTime;
}
