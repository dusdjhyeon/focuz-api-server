package dcom.focuz.api.domain.group.dto;

import dcom.focuz.api.domain.group.Group;
import dcom.focuz.api.domain.group.UserGroup;
import dcom.focuz.api.domain.group.UserGroupPermission;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

        public static Simple of(Group group) {
            return Simple.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .build();
        }
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
        private Set<UserResponseDto.Simple> users;

        public static Info of(Group group) {
            return Info.builder()
                    .id(group.getId())
                    .name(group.getName())
                    .description(group.getDescription())
                    .users(group.getUsers()
                            .stream()
                            .filter(userGroup -> !(userGroup.getPermission() == UserGroupPermission.NONMEMBER || userGroup.getPermission() == UserGroupPermission.KICKOUTMEMBER))
                            .map(UserGroup::getUser)
                            .map(UserResponseDto.Simple::of)
                            .collect(Collectors.toSet()))
                    .build();
        }
    }
}
