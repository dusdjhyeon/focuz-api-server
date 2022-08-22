package dcom.focuz.api.domain.user.dto;

import dcom.focuz.api.domain.group.UserGroup;
import dcom.focuz.api.domain.group.UserGroupPermission;
import dcom.focuz.api.domain.group.dto.GroupResponseDto;
import dcom.focuz.api.domain.user.Role;
import dcom.focuz.api.domain.user.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponseDto {
    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Simple {
        private Integer id;
        private String name;
        private String nickname;
        private String motto;
        private String profileImage;

        public static Simple of(User user) {
            return Simple.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .motto(user.getMotto())
                    .profileImage(user.getProfileImage())
                    .build();
        }

        public static List<Simple> of(List<User> users) {
            return users.stream().map(Simple::of).collect(Collectors.toList());
        }
    }

    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Group {
        private Integer id;
        private String name;
        private String nickname;
        private String motto;
        private String profileImage;
        private Long studyTime;

        public static Group of(UserGroup userGroup) {
            User user = userGroup.getUser();

            return Group.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .motto(user.getMotto())
                    .profileImage(user.getProfileImage())
                    .studyTime(userGroup.getStudyTime())
                    .build();
        }

        public static List<Group> of(List<UserGroup> users) {
            return users.stream().map(Group::of).collect(Collectors.toList());
        }
    }


    @Builder
    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private Integer id;
        private String email;
        private String name;
        private String nickname;
        private String motto;
        private String profileImage;
        private Role role;
        private List<GroupResponseDto.Info> groups;

        public static Profile of(User user) {
            return Profile.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .nickname(user.getNickname())
                    .motto(user.getMotto())
                    .profileImage(user.getProfileImage())
                    .role(user.getRole())
                    .groups(user.getGroups().stream().map(UserGroup::getGroup).map(GroupResponseDto.Info::of).collect(Collectors.toList()))
                    .build();
        }
    }
}
