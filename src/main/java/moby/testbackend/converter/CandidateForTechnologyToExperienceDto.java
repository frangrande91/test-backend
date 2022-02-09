package moby.testbackend.converter;

import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.dto.ExperienceDto;

public class CandidateForTechnologyToExperienceDto {

    public static ExperienceDto convert(CandidateForTechnology cxt){
        return ExperienceDto.builder()
                .name(cxt.getTechnology().getName())
                .version(cxt.getTechnology().getVersion())
                .yearsExperience(cxt.getYearsExperience())
                .build();
    }
}
