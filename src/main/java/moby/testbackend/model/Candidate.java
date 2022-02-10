package moby.testbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import moby.testbackend.model.enums.DocumentType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCandidate;

    @NotBlank(message = "Name must not be blank or null")
    private String name;

    @NotBlank(message = "Surname must not be blank or null")
    private String surname;

    private DocumentType documentType;

    @NotBlank(message = "Document must not be blank or null")
    private String document;

    @Past(message = "The date of birth must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;
}
