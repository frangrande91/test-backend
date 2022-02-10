package moby.testbackend.testUtils;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.dto.CandidateDto;
import moby.testbackend.model.enums.DocumentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Set;

public class CandidateTestUtils {

    public static Candidate getCandidate(){
        return Candidate.builder()
                .idCandidate(1)
                .name("Bruce")
                .surname("Wayne")
                .documentType(DocumentType.DNI)
                .document("12345")
                .birthdate(null)
                .build();
    }

    public static CandidateDto getCandidateDto(){
        return CandidateDto.builder()
                .name("Bruce")
                .surname("Wayne")
                .documentType(DocumentType.DNI)
                .document("12345")
                .birthdate(null)
                .build();
    }

    public static Page<CandidateDto> getPageCandidateDto(){
        return new PageImpl<>(getListCandidateDto());
    }

    public static List<CandidateDto> getListCandidateDto(){
        return List.of(getCandidateDto());
    }

    public static Set<CandidateDto> getSetCandidateDto(){
        return Set.of(getCandidateDto());
    }

}
