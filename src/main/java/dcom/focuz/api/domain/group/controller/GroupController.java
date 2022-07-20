package dcom.focuz.api.domain.group.controller;

import dcom.focuz.api.domain.group.dto.GroupRequestDto;
import dcom.focuz.api.domain.group.dto.GroupResponseDto;
import dcom.focuz.api.domain.group.service.GroupService;
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

@Api(tags = {"Group Controller"})
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @ApiOperation("해당 아이디를 가진 그룹의 정보를 반환 합니다.")
    @GetMapping(value = "/{groupId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<GroupResponseDto.Info> getGroupById(@ApiParam(value="그룹 ID", required = true) @PathVariable final String groupId) {
        return null;
    }

    @ApiOperation("그룹을 등록 합니다.")
    @PostMapping("/")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> postGroup(@Valid final GroupRequestDto.Register data) {
        return ResponseEntity.status(HttpStatus.CREATED).body("/group/" + groupService.postRegister(data));
    }
}
