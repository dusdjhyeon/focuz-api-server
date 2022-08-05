package dcom.focuz.api.domain.study;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter @Setter
@RedisHash(value = "study", timeToLive = 180)
public class TempStudy {
    @Id
    private String id;
    private Integer userId;
    private Integer studyTime;

    public TempStudy(Integer userId, Integer studyTime) {
        this.userId = userId;
        this.studyTime = studyTime;
    }
}
