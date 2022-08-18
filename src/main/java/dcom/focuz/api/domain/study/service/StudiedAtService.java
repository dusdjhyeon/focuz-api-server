package dcom.focuz.api.domain.study.service;

import dcom.focuz.api.domain.group.UserGroup;
import dcom.focuz.api.domain.group.repository.UserGroupRepository;
import dcom.focuz.api.domain.study.StudiedAt;
import dcom.focuz.api.domain.study.TempStudy;
import dcom.focuz.api.domain.study.dto.StudiedAtRequestDto;
import dcom.focuz.api.domain.study.dto.StudiedAtResponseDto;
import dcom.focuz.api.domain.study.dto.TempStudyRequestDto;
import dcom.focuz.api.domain.study.repository.StudiedAtRepository;
import dcom.focuz.api.domain.study.repository.TempStudyRepository;
import dcom.focuz.api.domain.user.Role;
import dcom.focuz.api.domain.user.User;
import dcom.focuz.api.domain.user.repository.UserRepository;
import dcom.focuz.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudiedAtService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final StudiedAtRepository studiedAtRepository;
    private final TempStudyRepository tempStudyRepository;
    private final RedisTemplate<String, Integer> redisTemplate;//여기있는 데이터 가져와서 studiedAtRepository, userGroupRepository에 갱신

    @Scheduled(cron = "0 * * * * *")//매 1분마다 수행
    public void updateDataBase() {
        log.info("Start Update DB");
        // 분당 공부 데이터 가져오기
        // 유저 가져와서 해당 유저가 가진 UserGroup 전체에 시간 추가 하기
        Iterator<TempStudy> studies = tempStudyRepository.findAll().iterator();

        while (studies.hasNext()) {
            TempStudy temp = studies.next();
            Optional<User> user = userRepository.findById(temp.getUserId());
            if(user.isPresent()){
                List<UserGroup> userGroups = userGroupRepository.findAllByUser(user.get());

                for(UserGroup userGroup:userGroups){
                    userGroup.setStudyTime(userGroup.getStudyTime()+temp.getStudyTime());//실제 공부 시간 더해줘야 함
                }
                userGroupRepository.saveAll(userGroups);
            }

            // StudiedAt 가져와서 StudiedAt(해당 시간(시 단위)) 없으면 추가, 있으면 수정하여 save
            Optional<StudiedAt> studiedAtOptional = studiedAtRepository.findByUserAndStudyHour(user.get(), LocalDateTime.now().truncatedTo(ChronoUnit.HOURS));
            if(studiedAtOptional.isEmpty()){
                studiedAtRepository.save(
                        StudiedAt.builder()
                        .user(user.get())
                        .studyHour(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))// 시간까지만 가져오게 잘라야 함
                        .studyTime(temp.getStudyTime())
                        .build());
            }
            else {
                StudiedAt study = studiedAtOptional.get();
                log.info(study.getStudyTime().toString());
                log.info(temp.getStudyTime().toString());
                study.setStudyTime(study.getStudyTime() + temp.getStudyTime());
                studiedAtRepository.save(study);
            }

        }

        // 분당 공부 데이터 전체 삭제
        tempStudyRepository.deleteAll();

        log.info("End Update DB");
    }

    @Transactional(readOnly = true)
    public List<StudiedAtResponseDto.Simple> getStudies(StudiedAtRequestDto.Search search) {
        User currentUser = userService.getCurrentUser();

        if (currentUser.getRole() != Role.USER) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "권한이 없습니다."
            );
        }

        //시간 기준임
        return StudiedAtResponseDto.Simple.of(
                studiedAtRepository.findAllByUserAndStudyHour(currentUser,search.getStartDate(),search.getEndDate().plusHours(1))
        );
    }

    // 공부 시간 추가 요청 받으면 Redis에 저장 하는 로직 추가하기.
    @Transactional
    public Integer updateStudyTime(TempStudyRequestDto.Seconds seconds) {//userid, studyTime,
        User user = userService.getCurrentUser();

        Optional<TempStudy> tempStudyOptional = tempStudyRepository.findByUserId(user.getId());
        if(tempStudyOptional.isEmpty()){
            tempStudyRepository.save(
                    TempStudy.builder()
                            .userId(user.getId())
                            .studyTime(seconds.getTimeSecond())
                            .build());
            return seconds.getTimeSecond();
        }
        else{
            TempStudy study = tempStudyOptional.get();
            study.setStudyTime(study.getStudyTime()+seconds.getTimeSecond());
            tempStudyRepository.save(study);
            return study.getStudyTime();
        }
    }
}
