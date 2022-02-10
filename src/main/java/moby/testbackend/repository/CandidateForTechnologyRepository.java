package moby.testbackend.repository;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static moby.testbackend.utils.Querys.CANDIDADES_BY_TECHNOLOGY;

@Repository
public interface CandidateForTechnologyRepository extends JpaRepository<CandidateForTechnology, Integer> {

    CandidateForTechnology findByCandidateAndTechnology(Candidate candidate, Technology technology);
    List<CandidateForTechnology> findByCandidate(Candidate candidate);
    List<CandidateForTechnology> findByTechnology(Technology technology);

    @Query(value = CANDIDADES_BY_TECHNOLOGY, nativeQuery = true)
    List<CandidateForTechnology> findByNameTechnology(String nameTechnology);
}
