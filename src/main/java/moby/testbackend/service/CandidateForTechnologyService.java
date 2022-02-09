package moby.testbackend.service;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.Technology;
import moby.testbackend.repository.CandidateForTechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class CandidateForTechnologyService {

    private final CandidateForTechnologyRepository candidateForTechnologyRepository;

    @Autowired
    public CandidateForTechnologyService(CandidateForTechnologyRepository candidateForTechnologyRepository){
        this.candidateForTechnologyRepository = candidateForTechnologyRepository;
    }

    public void addCandidateForTechnology(Candidate candidate, Technology technology, Integer yearsExperience) {
        if(isNull(candidateForTechnologyRepository.findByCandidateAndTechnology(candidate, technology)))
            candidateForTechnologyRepository.save(CandidateForTechnology.builder()
                                                    .candidate(candidate)
                                                    .technology(technology)
                                                    .yearsExperience(yearsExperience)
                                                    .build());
    }

    public List<CandidateForTechnology> getCandidatesForTechnologyByCandidate(Candidate candidate) {
        return candidateForTechnologyRepository.findByCandidate(candidate);
    }
}
