package moby.testbackend.repository;

import moby.testbackend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Integer> {

    Candidate findByIdCandidateOrDocument(Integer idCandidate, String document);
}
