package moby.testbackend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "candidatesForTechnologies")
public class CandidateForTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCandidateForTechnology;

    @NotNull(message = "Candidate must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    private Candidate candidate;

    @NotNull(message = "Technology must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    private Technology technology;

    @PositiveOrZero(message = "Years experience must not be less than 0")
    @NotBlank(message = "Years experience must not be blank o null")
    private Integer yearsExperience;
}
