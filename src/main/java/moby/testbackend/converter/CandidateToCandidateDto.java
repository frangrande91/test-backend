package moby.testbackend.converter;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.dto.CandidateDto;
import moby.testbackend.model.dto.ExperienceDto;

import java.util.List;

public class CandidateToCandidateDto {

    public static CandidateDto convert(Candidate candidate, List<ExperienceDto> technologies){
        return CandidateDto.builder()
                .name(candidate.getName())
                .surname(candidate.getSurname())
                .documentType(candidate.getDocumentType())
                .document(candidate.getDocument())
                .birthdate(candidate.getBirthdate())
                .technologies(technologies)
                .build();
    }
}
