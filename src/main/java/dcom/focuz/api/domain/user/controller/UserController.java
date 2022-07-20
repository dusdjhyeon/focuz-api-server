package dcom.focuz.api.domain.user.controller;

import dcom.focuz.api.domain.user.dto.UserResponseDto;
import dcom.focuz.api.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"User Controller"})
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @ApiOperation("전체 유저 리스트를 뽑아 냅니다.")
    @GetMapping(value = "/list")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<UserResponseDto.Simple>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @ApiOperation("해당 아이디를 가진 유저의 정보를 반환 합니다.")
    @GetMapping(value = "/{userId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Profile> getUserById(@ApiParam(value="유저 ID", required = true) @PathVariable @Valid final Integer userId) {
        return ResponseEntity.ok(userService.findUserById(userId));
    }

    @ApiOperation("현재 유저의 전체 정보를 반환 합니다.")
    @GetMapping(value = "/my_profile/info")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<UserResponseDto.Profile> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }
}
