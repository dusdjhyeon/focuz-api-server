package dcom.focuz.api.domain.user.service;

import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.dto.UserRequestDto;
import dcom.focuz.api.domain.user.dto.UserResponseDto;
import dcom.focuz.api.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserResponseDto.Simple> getAllUser() {
        return UserResponseDto.Simple.of(userRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Profile findUserById(Integer id) {
        return UserResponseDto.Profile.of(userRepository.findUserAllById(id).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }

    @Transactional(readOnly = true)
    public UserResponseDto.Profile getMyProfile() {
        return UserResponseDto.Profile.of(userRepository.findUserAllById(getCurrentUser().getId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."
                )
        ));
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "로그인 되지 않았습니다."
            );
        }

        return (User) authentication.getPrincipal();
    }
}