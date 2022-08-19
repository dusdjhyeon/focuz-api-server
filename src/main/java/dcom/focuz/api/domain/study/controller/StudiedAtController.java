package dcom.focuz.api.domain.study.controller;

import dcom.focuz.api.domain.study.dto.StudiedAtRequestDto;
import dcom.focuz.api.domain.study.dto.StudiedAtResponseDto;
import dcom.focuz.api.domain.study.dto.TempStudyRequestDto;
import dcom.focuz.api.domain.study.service.StudiedAtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {"Study Controller"})
@RestController
@RequestMapping("/study")
@RequiredArgsConstructor
public class StudiedAtController {
    private final StudiedAtService studiedAtService;

    @ApiOperation("현재 공부 한 시간을 추가 합니다.")
    @PostMapping(value = "")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ResponseEntity<Void> studyTime() {
        studiedAtService.updateDataBase();
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @ApiOperation("해당 범위 내, 공부 한 시간을 반환 합니다.")
    @GetMapping(value = "/search")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<StudiedAtResponseDto.Simple>> getStudies(@Valid @RequestBody StudiedAtRequestDto.Search search) {
        return ResponseEntity.ok(studiedAtService.getStudies(search));
    }

    @ApiOperation("요청 받은 시간을 레디스에 추가 합니다.")
    @PostMapping(value = "/add")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Integer> updateStudyTime(@Valid @RequestBody TempStudyRequestDto.Time time){
        return ResponseEntity.ok(studiedAtService.updateStudyTime(time));
    }
}
