package dcom.focuz.api.domain.study.service;

import dcom.focuz.api.domain.study.dto.StudiedAtRequestDto;
import dcom.focuz.api.domain.study.dto.StudiedAtResponseDto;
import dcom.focuz.api.domain.study.repository.StudiedAtRepository;
import dcom.focuz.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudiedAtService {
    private final UserService userService;
    private final StudiedAtRepository studiedAtRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Scheduled(cron = "0 * * * * *")
    public void updateDataBase() {
        log.info("Start Update DB");

        // 로직 추가

        log.info("End Update DB");
    }

    @Transactional(readOnly = true)
    public List<StudiedAtResponseDto.Simple> getStudies(StudiedAtRequestDto.Search search) {
        return null;
    }
}
