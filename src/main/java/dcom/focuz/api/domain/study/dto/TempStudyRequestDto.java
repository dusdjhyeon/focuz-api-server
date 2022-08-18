package dcom.focuz.api.domain.study.dto;

import dcom.focuz.api.domain.group.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TempStudyRequestDto {
    @ApiModel(value="스터디 초 단위 기록 받기")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Seconds {
        @PositiveOrZero
        @ApiModelProperty(value = "공부 시간")
        private Integer timeSecond;
    }
}
