package dcom.focuz.api.redis;

import dcom.focuz.api.domain.study.TempStudy;
import dcom.focuz.api.domain.study.repository.TempStudyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class TempStudyTest {
    @Autowired
    private TempStudyRepository tempStudyRepository;

    @Test
    void 입력_저장_수정_삭제_테스트() throws Exception {
        TempStudy tempStudy = new TempStudy("asdf", 1, 0);
        tempStudyRepository.save(tempStudy);

        Optional<TempStudy> getTempStudy = tempStudyRepository.findById(tempStudy.getId());
        if (getTempStudy.isPresent()) {
            TempStudy study = getTempStudy.get();
            System.out.println("study.getId() = " + study.getId());
            System.out.println("study.getStudyTime() = " + study.getStudyTime());

            study.setStudyTime(10);
            tempStudyRepository.save(study);
        } else {
            throw new Exception("로직이 잘못 됨.");
        }

        Optional<TempStudy> modifiedTempStudy = tempStudyRepository.findById(tempStudy.getId());
        if (modifiedTempStudy.isPresent()) {
            TempStudy study = modifiedTempStudy.get();
            System.out.println("study.getId() = " + study.getId());
            System.out.println("study.getStudyTime() = " + study.getStudyTime());
            tempStudyRepository.delete(study);
        } else {
            throw new Exception("로직이 잘못 됨.");
        }
    }

    @Test
    void 입력만_테스트() throws Exception {
        TempStudy tempStudy = new TempStudy("asdf", 1, 0);
        tempStudyRepository.save(tempStudy);

        Optional<TempStudy> getTempStudy = tempStudyRepository.findById(tempStudy.getId());
        if (getTempStudy.isPresent()) {
            TempStudy study = getTempStudy.get();
            System.out.println("study.getId() = " + study.getId());
            System.out.println("study.getStudyTime() = " + study.getStudyTime());

            study.setStudyTime(10);
            tempStudyRepository.save(study);
        } else {
            throw new Exception("로직이 잘못 됨.");
        }

        Optional<TempStudy> modifiedTempStudy = tempStudyRepository.findById(tempStudy.getId());
        if (modifiedTempStudy.isPresent()) {
            TempStudy study = modifiedTempStudy.get();
            System.out.println("study.getId() = " + study.getId());
            System.out.println("study.getStudyTime() = " + study.getStudyTime());

            tempStudyRepository.save(study);
        } else {
            throw new Exception("로직이 잘못 됨.");
        }
    }
}
