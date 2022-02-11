package moby.testbackend.testUtils;

import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.dto.ExperienceDto;

import java.util.List;

import static moby.testbackend.testUtils.CandidateTestUtils.getCandidate;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnology;

public class CandidateForTechnologyTestUtils {

    public static CandidateForTechnology getCandidateForTechnology() {
        return CandidateForTechnology.builder()
                .candidate(getCandidate())
                .technology(getTechnology())
                .yearsExperience(1)
                .build();
    }

    public static List<CandidateForTechnology> getListCandidateForTechnology() {
        return List.of(getCandidateForTechnology());
    }

    public static ExperienceDto getExperienceDto() {
        return ExperienceDto.builder()
                .name("Python")
                .version("3.10")
                .yearsExperience(1)
                .build();
    }

    public static List<ExperienceDto> getListExperienceDto() {
        return List.of(getExperienceDto());
    }
}
