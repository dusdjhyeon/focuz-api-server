package dcom.focuz.api.domain.friend.service;

import dcom.focuz.api.domain.friend.Friend;
import dcom.focuz.api.domain.friend.FriendState;
import dcom.focuz.api.domain.friend.repository.FriendRepository;
import dcom.focuz.api.domain.notification.Notification;
import dcom.focuz.api.domain.notification.repository.NotificationRepository;
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
public class FriendService {
    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional
    public UserResponseDto.Simple friendRequest(Integer targetUserId) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다."
            );
        }

        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "타겟 유저가 존재하지 않습니다."
                )
        );

        if (targetUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "해당 하는 유저는 비활성화 된 유저입니다."
            );
        }

        if(friendRepository.findByUserAndTargetUser(currentUser, targetUser).isEmpty()){
            friendRepository.save(
                    Friend.builder()
                            .user(currentUser)
                            .targetUser(targetUser)
                            .state(FriendState.REQUEST)
                            .build()
            );
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,"이미 관계가 존재하는 유저입니다."
            );
        }

        notificationRepository.save(
                Notification.builder()
                        .user(targetUser)
                        .message(String.format("%s님에게서 친구 추가 요청이 왔습니다.", currentUser.getNickname()))
                        .url("/")
                        .build()
        );

        return UserResponseDto.Simple.of(targetUser);

    }

    @Transactional(readOnly = true)
    public List<UserResponseDto.Simple> getFriendRequestList() {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다."
            );
        }

        return UserResponseDto.Simple.of(
                friendRepository.findAllByStateAndTargetUser(FriendState.REQUEST, currentUser)
                        .stream()
                        .map(Friend::getUser)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public UserResponseDto.Simple acceptFriendRequest(Integer targetUserId){
        User currentUser = userService.getCurrentUser();

        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "타겟 유저가 존재하지 않습니다."
                )
        );

        Friend friend = friendRepository.findByUserAndTargetUser(targetUser, currentUser).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "친구 관계가 없습니다."
                )
        );

        if(friend.getState() == FriendState.REQUEST) {
            friendRepository.save(
                    Friend.builder()
                            .user(currentUser)
                            .targetUser(targetUser)
                            .state(FriendState.FRIEND)
                            .build()
            );

            friend.setState(FriendState.FRIEND);
            friendRepository.save(friend);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"요청 상태가 아닙니다.");
        }

        notificationRepository.save(
                Notification.builder()
                        .user(targetUser)
                        .message(String.format("%s님과 친구가 되었습니다.", currentUser.getNickname()))
                        .url("/")
                        .build()
        );

        return UserResponseDto.Simple.of(targetUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto.Simple> getFriendList() {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다."
            );
        }

        return UserResponseDto.Simple.of(
                friendRepository.findAllByStateAndTargetUser(FriendState.FRIEND, currentUser)
                        .stream()
                        .map(Friend::getUser)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteFriend(Integer targetUserId){
        User currentUser = userService.getCurrentUser();
        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "타겟 유저가 존재하지 않습니다."
                )
        );
        Friend data1 = friendRepository.findByUserAndTargetUser(currentUser, targetUser).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "친구 간 상태가 없습니다."
                )
        );
        Friend data2 = friendRepository.findByUserAndTargetUser(targetUser,currentUser).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "친구 간 상태가 없습니다."
                )
        );
        //이미 친구인지 확인
        if (data1.getState() == FriendState.FRIEND && data2.getState()==FriendState.FRIEND){
            friendRepository.delete(data1);
            friendRepository.delete(data2);
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"친구 상태가 아닙니다.");
        }
    }

    //-----차단 친구-----
    @Transactional
    public UserResponseDto.Simple blockFriend(Integer targetUserId){
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다."
            );
        }

        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "타겟 유저가 존재하지 않습니다."
                )
        );

        if (targetUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "해당 하는 유저는 비활성화 된 유저입니다."
            );
        }

        if(friendRepository.findByUserAndTargetUser(currentUser, targetUser).isEmpty()){
            friendRepository.save(
                    Friend.builder()
                            .user(currentUser)
                            .targetUser(targetUser)
                            .state(FriendState.BLOCKED)
                            .build()
            );
        }
        else{
            Friend friend = friendRepository.findByUserAndTargetUser(currentUser, targetUser).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"유저와 타겟 유저 사이 관계가 존재하지 않습니다.")
            );
            if(friend.getState()==FriendState.BLOCKED){
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "이미 차단 된 유저입니다."
                );
            } else if (friend.getState()==FriendState.FRIEND) {
                Friend data = friendRepository.findByUserAndTargetUser(targetUser,currentUser).orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "잘못된 요청입니다."
                        )
                );
                friendRepository.delete(data);
            } else{
                friend.setState(FriendState.BLOCKED);
                friendRepository.save(friend);
            }
        }

        return UserResponseDto.Simple.of(targetUser);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto.Simple> getBlockedFriendList(){
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다."
            );
        }

        return UserResponseDto.Simple.of(
                friendRepository.findAllByStateAndUser(FriendState.BLOCKED, currentUser)
                        .stream()
                        .map(Friend::getTargetUser)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void deleteRequest(Integer targetUserId){
        User currentUser = userService.getCurrentUser();
        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "타겟 유저가 존재하지 않습니다."
                )
        );
        Friend data = friendRepository.findByUserAndTargetUser(currentUser, targetUser).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저와의 관계를 찾을 수 없습니다."
                )
        );
        //요청한 상태 확인
        if(data.getState()== FriendState.REQUEST){
            friendRepository.delete(data);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "요청 상태가 아닙니다.."
            );
        }
    }

    @Transactional
    public void deleteBlock(Integer targetUserId){
        User currentUser = userService.getCurrentUser();
        User targetUser = userRepository.findById(targetUserId).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "타겟 유저가 존재하지 않습니다."
                )
        );
        Friend data = friendRepository.findByUserAndTargetUser(currentUser, targetUser).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        );
        //차단 상태 확인
        if(data.getState()== FriendState.BLOCKED){
            friendRepository.delete(data);
        }
        else{
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "요청 상태가 아닙니다.."
            );
        }

    }
}
