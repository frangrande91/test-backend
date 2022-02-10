package moby.testbackend.service;

import moby.testbackend.exception.CandidateForTechnologyAlreadyExistsException;
import moby.testbackend.model.Candidate;
import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.Technology;
import moby.testbackend.model.dto.ExperienceDto;
import moby.testbackend.repository.CandidateForTechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static moby.testbackend.converter.CandidateForTechnologyToExperienceDto.convert;

@Service
public class CandidateForTechnologyService {

    private final CandidateForTechnologyRepository candidateForTechnologyRepository;

    @Autowired
    public CandidateForTechnologyService(CandidateForTechnologyRepository candidateForTechnologyRepository){
        this.candidateForTechnologyRepository = candidateForTechnologyRepository;
    }

    public void addCandidateForTechnology(Candidate candidate, Technology technology, Integer yearsExperience) throws CandidateForTechnologyAlreadyExistsException {
        CandidateForTechnology cxt = candidateForTechnologyRepository.findByCandidateAndTechnology(candidate, technology);
        if(!isNull(cxt))
            throw new CandidateForTechnologyAlreadyExistsException("Thechnology " + technology.getName() + " already exists for this candidate");
        else {
            candidateForTechnologyRepository.save(CandidateForTechnology.builder()
                    .candidate(candidate)
                    .technology(technology)
                    .yearsExperience(yearsExperience)
                    .build());
        }
    }

    public List<CandidateForTechnology> getCandidatesForTechnologyByCandidate(Candidate candidate) {
        return candidateForTechnologyRepository.findByCandidate(candidate);
    }

    public List<ExperienceDto> getExperiencesByCandidate(Candidate candidate) {
        List<ExperienceDto> experiences = new ArrayList<>();
        for(CandidateForTechnology cxt : candidateForTechnologyRepository.findByCandidate(candidate)) {
            experiences.add(convert(cxt));
        }
        return experiences;
    }

    public List<CandidateForTechnology> getCandidatesForTechnologyByTechnology(Technology technology) {
        return candidateForTechnologyRepository.findByTechnology(technology);
    }

    public List<CandidateForTechnology> getCandidatesForTechnologyByNameTechnology(String nameTechnology){
        return candidateForTechnologyRepository.findByNameTechnology(nameTechnology);
    }
}
