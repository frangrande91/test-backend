package moby.testbackend.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moby.testbackend.model.enums.DocumentType;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDto {

    private String name;
    private String surname;
    private DocumentType documentType;
    private String document;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    private List<ExperienceDto> technologies;
}
