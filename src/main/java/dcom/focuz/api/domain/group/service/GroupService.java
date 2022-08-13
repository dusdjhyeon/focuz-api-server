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
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import dcom.focuz.api.domain.user.repository.UserRepository;
import dcom.focuz.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public GroupResponseDto.Info findGroupById(Integer id) {
        return GroupResponseDto.Info.of(groupRepository.findAllInfoById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "그룹을 찾을 수 없습니다."
                )
        ));
    }

    // 그룹 생성
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

    // 그룹 삭제
    @Transactional
    public void deleteGroup(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
                ));

        User user = userService.getCurrentUser();

        // owner나 manager가 아니면 권한 없음
        UserGroup userGroup = userGroupRepository.findByUserAndGroup(user, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if ((userGroup.getPermission() != UserGroupPermission.OWNER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "그룹 신청 목록을 볼 권한이 없습니다.");
        }
        groupRepository.delete(group);
    }

    @Transactional(readOnly = true)
    public GroupResponseDto.Info getGroupById(Integer id) {
        return GroupResponseDto.Info.of(groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        )));
    }

    // 그룹 가입 신청 목록
    @Transactional(readOnly = true)
    public List<UserResponseDto.Simple> getRequestUserForGroupList(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User user = userService.getCurrentUser();
        
        UserGroup userGroup = userGroupRepository.findByUserAndGroup(user, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if ((userGroup.getPermission() != UserGroupPermission.MANAGER) && (userGroup.getPermission() != UserGroupPermission.OWNER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "그룹 신청 목록을 볼 권한이 없습니다.");
        }

        return userGroupRepository.findAllByGroupAndPermission(group, UserGroupPermission.NONMEMBER)
                .stream().map(UserGroup::getUser).map(UserResponseDto.Simple::of).collect(Collectors.toList());
    }

    // 그룹 가입 신청
    @Transactional
    public void requestGroupJoin(Integer groupId) {
        User user = userService.getCurrentUser();

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        Optional<UserGroup> userGroup = userGroupRepository.findByUserAndGroup(user, group);

        if (userGroup.isPresent()) {
            if (userGroup.get().getPermission() == UserGroupPermission.KICKOUTMEMBER)
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "강퇴 당한 그룹에는 다시 가입 요청을 할 수 없습니다."
                );
            else
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "이미 요청 된 상태입니다.");
        }

        userGroupRepository.save(
                UserGroup.builder()
                        .user(user)
                        .group(group)
                        .permission(UserGroupPermission.NONMEMBER)
                        .build()
        );
    }


    // 가입 승인
    @Transactional
    public void acceptGroupJoin(Integer groupId, Integer userId) {
        User currentUser = userService.getCurrentUser();

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        UserGroup currentUserGroup = userGroupRepository.findByUserAndGroup(currentUser, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if ((currentUserGroup.getPermission() != UserGroupPermission.MANAGER) && (currentUserGroup.getPermission() != UserGroupPermission.OWNER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "가입 승인을 할 권한이 없습니다.");
        }

        User requestUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "해당된 유저가 존재하지 않습니다."
        ));

        UserGroup requestUserGroup = userGroupRepository.findByUserAndGroup(requestUser, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if (requestUserGroup.getPermission() != UserGroupPermission.NONMEMBER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        requestUserGroup.setPermission(UserGroupPermission.MEMBER);

        userGroupRepository.save(requestUserGroup);
    }

    // 그룹 탈퇴
    @Transactional
    public void quitGroup(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User currentUser = userService.getCurrentUser();

        UserGroup userGroup = userGroupRepository.findByUserAndGroup(currentUser, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "잘못된 요청입니다."
        ));

        if ((userGroup.getPermission() != UserGroupPermission.MEMBER) || (userGroup.getPermission() != UserGroupPermission.MANAGER))  {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");
        }

        userGroupRepository.delete(userGroup);
    }
    
    // 멤버 강퇴
    @Transactional
    public void kickOutOfGroup(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User currentUser = userService.getCurrentUser();

        UserGroup currentUserGroup = userGroupRepository.findByUserAndGroup(currentUser, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if ((currentUserGroup.getPermission() != UserGroupPermission.OWNER) || currentUserGroup.getPermission() != UserGroupPermission.MANAGER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "강퇴시킬 권한이 없습니다.");
        }

        User kickOutUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "해당된 유저가 존재하지 않습니다."
        ));

        UserGroup kickOutUserGroup = userGroupRepository.findByUserAndGroup(kickOutUser, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "잘못된 요청입니다."
        ));

        kickOutUserGroup.setPermission(UserGroupPermission.KICKOUTMEMBER);

        userGroupRepository.save(kickOutUserGroup);
    }

    // 멤버 강퇴 리스트
    @Transactional
    public List<UserResponseDto.Simple> getKickOutMemberOfGroupList(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User currentUser = userService.getCurrentUser();

        UserGroup userGroup = userGroupRepository.findByUserAndGroup(currentUser, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if ((userGroup.getPermission() != UserGroupPermission.OWNER) || (userGroup.getPermission() != UserGroupPermission.MANAGER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "강퇴 목록을 볼 권한이 없습니다.");
        }

        return userGroupRepository.findAllByGroupAndPermission(group, UserGroupPermission.KICKOUTMEMBER)
                .stream().map(UserGroup::getUser).map(UserResponseDto.Simple::of).collect(Collectors.toList());
    }

    // 그룹 멤버 리스트
    @Transactional
    public List<UserResponseDto.Simple> getMemberListForGroup(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User user = userService.getCurrentUser();

        if (user.getRole() != Role.USER) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "권한이 없습니다.");
        }

        return userGroupRepository.findAllByGroupAndPermission(group, UserGroupPermission.MEMBER)
                .stream().map(UserGroup::getUser).map(UserResponseDto.Simple::of).collect(Collectors.toList());
    }

    // 매니저 등록
    @Transactional
    public void appointManagerOfGroup(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User owner = userService.getCurrentUser();

        UserGroup ownerGroup = userGroupRepository.findByUserAndGroup(owner, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        if (ownerGroup.getPermission() != UserGroupPermission.OWNER)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");

        User manager = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));

        UserGroup managerGroup = userGroupRepository.findByUserAndGroup(manager, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "잘못된 요청입니다."
        ));

        managerGroup.setPermission(UserGroupPermission.MANAGER);

        userGroupRepository.save(managerGroup);
    }

    // 매니저 삭제
    @Transactional
    public void dismissManagerOfGroup(Integer groupId, Integer userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        ));

        User owner = userService.getCurrentUser();

        UserGroup ownerGroup = userGroupRepository.findByUserAndGroup(owner, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.FORBIDDEN, "접근 권한이 없습니다."
        ));

        User manager = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 유저가 존재하지 않습니다."
        ));

        UserGroup managerGroup = userGroupRepository.findByUserAndGroup(manager, group).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "잘못된 요청입니다."
        ));

        managerGroup.setPermission(UserGroupPermission.MEMBER);

        userGroupRepository.save(managerGroup);
    }
}