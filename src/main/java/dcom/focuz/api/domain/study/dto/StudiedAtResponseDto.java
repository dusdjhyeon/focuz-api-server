package dcom.focuz.api.domain.study.dto;

import dcom.focuz.api.domain.study.StudiedAt;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StudiedAtResponseDto {
    @ApiModel(value = "스터디 시간 정보")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        @ApiModelProperty(value = "공부를 진행 한 특정 시간 대 (시 단위)")
        private LocalDateTime studyHour;

        @ApiModelProperty(value = "공부를 한 시간 (초 단위)")
        private Integer studyTime;

        public static StudiedAtResponseDto.Simple of(StudiedAt study) {
            return StudiedAtResponseDto.Simple.builder()
                        .studyHour(study.getStudyHour())
                        .studyTime(study.getStudyTime())
                        .build();
        }

        public static List<StudiedAtResponseDto.Simple> of(List<StudiedAt> studies) {
            return studies.stream().map(StudiedAtResponseDto.Simple::of).collect(Collectors.toList());
        }
    }
}
