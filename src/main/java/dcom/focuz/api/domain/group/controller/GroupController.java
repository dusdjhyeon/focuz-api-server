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
    public ResponseEntity<GroupResponseDto.Info> getGroupById(@ApiParam(value="그룹 ID", required = true) @PathVariable @Valid final Integer groupId) {
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @ApiOperation("그룹을 등록 합니다.")
    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> postGroup(@Valid @RequestBody final GroupRequestDto.Register data) {
        return ResponseEntity.status(HttpStatus.CREATED).body("/group/" + groupService.postGroup(data));
    }

    @ApiOperation("그룹을 삭제 합니다.")
    @DeleteMapping(value = "/delete/{groupId}")
    public ResponseEntity<Void> deleteGroup(@ApiParam(value="그룹 ID", required = true) @PathVariable final Integer groupId) {
        groupService.deleteGroup(groupId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("그룹 가입 신청 목록을 보여줍니다.")
    @GetMapping(value = "/requestlist/{groupId}")
    public ResponseEntity<List<UserResponseDto.Simple>> getRequestUserForGroupList(@ApiParam(value="그룹 ID", required = true) @PathVariable final Integer groupId) {
        return ResponseEntity.ok(groupService.getRequestUserForGroupList(groupId));
    }

    @ApiOperation("현재 유저가 해당 그룹에 가입 요청을 보냅니다.")
    @PostMapping(value = "/join/{groupId}")
    public ResponseEntity<Void> requestGroupJoin(@ApiParam(value = "그룹 ID", required = true) @PathVariable final Integer groupId) {
        groupService.requestGroupJoin(groupId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("현재 유저를 해당 그룹의 멤버로 등록 합니다.")
    @PostMapping(value = "/accept/{groupId}")
    public ResponseEntity<Void> acceptGroupJoin(@ApiParam(value="그룹 ID", required = true) @PathVariable final Integer groupId,
                                                @ApiParam(value = "유저 ID", required = true) @PathVariable final Integer userId) {
        groupService.acceptGroupJoin(groupId, userId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}