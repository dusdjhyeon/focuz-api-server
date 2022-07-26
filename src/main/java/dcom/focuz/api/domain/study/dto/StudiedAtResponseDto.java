package dcom.focuz.api.domain.study.dto;

import dcom.focuz.api.domain.study.StudiedAt;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class StudiedAtResponseDto {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private LocalDateTime studyHour;
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
