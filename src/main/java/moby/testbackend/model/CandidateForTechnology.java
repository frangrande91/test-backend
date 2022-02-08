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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "candidatesForTechnlogies")
public class CandidateForTechnology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCandidateForTechnology;

    @ManyToOne(fetch = FetchType.EAGER)
    private Candidate candidate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Technology technology;

    private Integer yearsExperience;
}
