package dcom.focuz.api.domain.study.dto;

import dcom.focuz.api.domain.group.Group;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class StudiedAtRequestDto {
    @ApiModel(value="스터디 내역 검색")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Search {
        @NotEmpty @PastOrPresent
        @ApiModelProperty(value = "시작 시간")
        private LocalDateTime startDate;

        @NotEmpty @PastOrPresent
        @ApiModelProperty(value = "종료 시간")
        private LocalDateTime endDate;
    }
}
