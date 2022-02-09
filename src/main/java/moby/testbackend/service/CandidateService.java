package moby.testbackend.service;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.Technology;
import moby.testbackend.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final TechnologyService technologyService;
    private final CandidateForTechnologyService candidateForTechnologyService;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, TechnologyService technologyService, CandidateForTechnologyService candidateForTechnologyService){
        this.candidateRepository = candidateRepository;
        this.technologyService = technologyService;
        this.candidateForTechnologyService = candidateForTechnologyService;
    }

    public Candidate addCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Page<Candidate> getAllCandidates(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

    public Candidate getCandidateById(Integer idCandidate) {
        return candidateRepository.findById(idCandidate).orElse(null);
    }

    public Candidate addTechnologyToCandidate(Integer idCandidate, Integer idTechnology, Integer yearsExperience) {
        Candidate candidate = getCandidateById(idCandidate);
        Technology technology = technologyService.getTechnologyById(idTechnology);
        candidateForTechnologyService.addCandidateForTechnology(candidate, technology, yearsExperience);
        return candidate;
    }

    public void deleteCandidate(Integer idCandidate) {
        Candidate candidate = getCandidateById(idCandidate);
        if(candidateForTechnologyService.getCandidatesForTechnologyByCandidate(candidate).isEmpty())
            candidateRepository.deleteById(idCandidate);
    }
}
