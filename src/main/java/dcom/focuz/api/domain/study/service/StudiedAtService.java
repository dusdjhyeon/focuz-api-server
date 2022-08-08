package dcom.focuz.api.domain.study.service;

import dcom.focuz.api.domain.group.UserGroup;
import dcom.focuz.api.domain.group.repository.UserGroupRepository;
import dcom.focuz.api.domain.study.StudiedAt;
import dcom.focuz.api.domain.study.TempStudy;
import dcom.focuz.api.domain.study.dto.StudiedAtRequestDto;
import dcom.focuz.api.domain.study.dto.StudiedAtResponseDto;
import dcom.focuz.api.domain.study.repository.StudiedAtRepository;
import dcom.focuz.api.domain.study.repository.TempStudyRepository;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.repository.UserRepository;
import dcom.focuz.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudiedAtService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final StudiedAtRepository studiedAtRepository;
    private final TempStudyRepository tempStudyRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Scheduled(cron = "0 * * * * *")
    public void updateDataBase() {
        log.info("Start Update DB");
        // 레디스에서 분당 공부 데이터 가져오기
        // 유저 가져와서 해당 유저가 가진 UserGroup 전체에 시간 추가 하기
        // StudiedAt 가져와서 해당 시간(시 단위) 없으면 추가, 있으면 수정하여 save
        // 분당 공부 데이터 전체 삭제
        log.info("End Update DB");
    }

    @Transactional(readOnly = true)
    public List<StudiedAtResponseDto.Simple> getStudies(StudiedAtRequestDto.Search search) {
        return null;
    }

    // 공부 시간 추가 요청 받으면 Redis에 저장 하는 로직 추가하기.
}
