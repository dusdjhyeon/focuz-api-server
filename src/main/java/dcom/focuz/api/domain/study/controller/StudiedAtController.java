package dcom.focuz.api.domain.study.controller;

import dcom.focuz.api.domain.study.dto.StudiedAtRequestDto;
import dcom.focuz.api.domain.study.dto.StudiedAtResponseDto;
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
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Void> studyTime() {
        return null;
    }

    @ApiOperation("해당 범위 내, 공부 한 시간을 반환 합니다.")
    @GetMapping(value = "/search")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<List<StudiedAtResponseDto.Simple>> getStudies(@Valid StudiedAtRequestDto.Search search) {
        return null;
    }
}
