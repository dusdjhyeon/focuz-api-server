package dcom.focuz.api.domain.group.service;

import dcom.focuz.api.domain.group.dto.GroupRequestDto;
import dcom.focuz.api.domain.group.dto.GroupResponseDto;
import dcom.focuz.api.domain.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    @Transactional
    public Integer postRegister(GroupRequestDto.Register data) {
        Integer id = groupRepository.save(data.toEntity()).getId();
        // 유저 로직 추가 필요.

        return id;
    }

    @Transactional(readOnly = true)
    public GroupResponseDto.Info getGroupById(Integer id) {
        return GroupResponseDto.Info.of(groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "해당하는 ID를 가진 그룹이 존재하지 않습니다."
        )));
    }
}
