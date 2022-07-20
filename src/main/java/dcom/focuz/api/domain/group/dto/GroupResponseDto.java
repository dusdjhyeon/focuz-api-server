package dcom.focuz.api.domain.group.dto;

import dcom.focuz.api.domain.group.Group;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class GroupResponseDto {
    @ApiModel(value = "그룹 프로필 요약 정보")
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    static public class Simple {
        private Integer id;
        private String name;
        private String description;
    }

    @ApiModel(value = "그룹 프로필 세부 정보")
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    static public class Info {
        private Integer id;
        private String name;
        private String description;
        private Set<UserResponseDto.Simple> users = new HashSet<>();

        public static Info of(Group group) {
            return Info.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getName())
                    .build();
        }
    }
}
