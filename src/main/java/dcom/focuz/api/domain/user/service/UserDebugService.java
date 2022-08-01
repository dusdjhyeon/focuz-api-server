package dcom.focuz.api.domain.user.service;


import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

public class UserDebugService extends UserService {
    private final HttpServletRequest request;

    public UserDebugService(UserRepository userRepository, HttpServletRequest request) {
        super(userRepository);
        this.request = request;
    }

    private Integer getUserIdInHeader() {
        String userIdString = request.getHeader("userId");

        if (userIdString == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        try {
            return Integer.parseInt(userIdString);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userID를 파싱할 수 없습니다.");
        }
    }

    @Override
    public User getCurrentUser() {
        return userRepository.getById(getUserIdInHeader());
    }
}
