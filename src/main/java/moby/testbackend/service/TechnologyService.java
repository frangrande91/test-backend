package moby.testbackend.service;

import moby.testbackend.model.Candidate;
import moby.testbackend.model.Technology;
import moby.testbackend.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final CandidateForTechnologyService candidateForTechnologyService;

    @Autowired
    public TechnologyService(TechnologyRepository technologyRepository, CandidateForTechnologyService candidateForTechnologyService){
        this.technologyRepository = technologyRepository;
        this.candidateForTechnologyService = candidateForTechnologyService;
    }

    public Technology addTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public Page<Technology> getAllTechnologies(Pageable pageable) {
        return technologyRepository.findAll(pageable);
    }

    public Technology getTechnologyById(Integer idTechnology){
        return technologyRepository.findById(idTechnology).orElse(null);
    }

    public Technology updateTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public void deleteTechnology(Integer idTechnology) {
        Technology technology = getTechnologyById(idTechnology);
        if(isNull(candidateForTechnologyService.getCandidatesForTechnologyByTechnology(technology)))
            technologyRepository.deleteById(idTechnology);
    }
}
