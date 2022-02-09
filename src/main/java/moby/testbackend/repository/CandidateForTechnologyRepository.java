package moby.testbackend.repository;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateForTechnologyRepository extends JpaRepository<CandidateForTechnology, Integer> {

    CandidateForTechnology findByCandidateAndTechnology(Candidate candidate, Technology technology);
    List<CandidateForTechnology> findByCandidate(Candidate candidate);
    List<CandidateForTechnology> findByTechnology(Technology technology);
}
