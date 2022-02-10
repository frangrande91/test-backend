package moby.testbackend.service;

import moby.testbackend.exception.CandidateAlreadyExistsException;
import moby.testbackend.exception.CandidateForTechnologyAlreadyExistsException;
import moby.testbackend.exception.CandidateNotExistsException;
import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.Candidate;
import moby.testbackend.model.Technology;
import moby.testbackend.model.dto.CandidateDto;
import moby.testbackend.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;
import static moby.testbackend.converter.CandidateToCandidateDto.convert;

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

    public Candidate addCandidate(Candidate candidate) throws CandidateAlreadyExistsException {
        if (!isNull(candidateRepository.findByIdCandidateOrDocument(candidate.getIdCandidate(), candidate.getDocument()))) {
            throw new CandidateAlreadyExistsException("Candidate already exists");
        } else {
            return candidateRepository.save(candidate);
        }
    }

    public Page<CandidateDto> getAllCandidates(Pageable pageable) {
        List<CandidateDto> candidatesDto = new ArrayList<>();
        for(Candidate candidate : candidateRepository.findAll(pageable)){
            candidatesDto.add(convert(candidate, candidateForTechnologyService.getExperiencesByCandidate(candidate)));
        }
        return new PageImpl<>(candidatesDto);
    }

    public Candidate getCandidateById(Integer idCandidate) throws CandidateNotExistsException {
        return candidateRepository.findById(idCandidate).orElseThrow(() -> new CandidateNotExistsException("Candidate not exists"));
    }

    public CandidateDto getCandidateDtoById(Integer idCandidate) throws CandidateNotExistsException {
        Candidate candidate = getCandidateById(idCandidate);
        return convert(candidate, candidateForTechnologyService.getExperiencesByCandidate(candidate));
    }

    public Candidate addTechnologyToCandidate(Integer idCandidate, Integer idTechnology, Integer yearsExperience) throws CandidateNotExistsException, TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException {
        Candidate candidate = getCandidateById(idCandidate);
        Technology technology = technologyService.getTechnologyById(idTechnology);
        candidateForTechnologyService.addCandidateForTechnology(candidate, technology, yearsExperience);
        return candidate;
    }

    public Candidate updateCandidate(Candidate candidate) throws CandidateNotExistsException {
        if(isNull(getCandidateById(candidate.getIdCandidate())))
            throw new CandidateNotExistsException("Candidate not exists");
        else
            return candidateRepository.save(candidate);
    }

    public void deleteCandidate(Integer idCandidate) throws CandidateNotExistsException, RestrictDeleteException {
        Candidate candidate = getCandidateById(idCandidate);
        if(!candidateForTechnologyService.getCandidatesForTechnologyByCandidate(candidate).isEmpty())
            throw new RestrictDeleteException("Can not delete this candidate because it depends of another objects");
        else
            candidateRepository.deleteById(idCandidate);
    }


}
