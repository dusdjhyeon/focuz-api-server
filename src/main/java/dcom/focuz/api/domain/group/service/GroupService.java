package dcom.focuz.api.domain.group.service;

import dcom.focuz.api.domain.group.Group;
import dcom.focuz.api.domain.group.UserGroup;
import dcom.focuz.api.domain.group.UserGroupPermission;
import dcom.focuz.api.domain.group.dto.GroupRequestDto;
import dcom.focuz.api.domain.group.dto.GroupResponseDto;
import dcom.focuz.api.domain.group.repository.GroupRepository;
import dcom.focuz.api.domain.group.repository.UserGroupRepository;
import dcom.focuz.api.domain.user.Role;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.service.UserService;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserService userService;

    @Transactional
    public Integer postGroup(GroupRequestDto.Register data) {
        User user = userService.getCurrentUser();
        if (user.getRole() != Role.USER)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");

        Group group = groupRepository.save(data.toEntity());
        Integer id = group.getId();

        // 최초 유저 등록(그룹 만든 본인)
        userGroupRepository.save(
                UserGroup.builder()
                        .user(userService.getCurrentUser())
                        .group(group)
                        .permission(UserGroupPermission.OWNER)
                        .build()
        );

        return id;
    }

    @Transactional(readOnly = true)
    public GroupResponseDto.Info getGroupById(Integer id) {
        return GroupResponseDto.Info.of(groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        )));
    }
}
