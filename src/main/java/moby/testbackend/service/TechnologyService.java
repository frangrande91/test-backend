package moby.testbackend.service;

import moby.testbackend.model.Technology;
import moby.testbackend.model.dto.TechnologyDto;
import moby.testbackend.repository.TechnologyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;
    private final CandidateForTechnologyService candidateForTechnologyService;
    private final ModelMapper modelMapper;

    @Autowired
    public TechnologyService(TechnologyRepository technologyRepository, CandidateForTechnologyService candidateForTechnologyService, ModelMapper modelMapper){
        this.technologyRepository = technologyRepository;
        this.candidateForTechnologyService = candidateForTechnologyService;
        this.modelMapper = modelMapper;
    }

    public Technology addTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    public Page<TechnologyDto> getAllTechnologies(Pageable pageable) {
        List<TechnologyDto> technologiesDto = new ArrayList<>();
        for (Technology technology : technologyRepository.findAll(pageable)){
            technologiesDto.add(modelMapper.map(technology, TechnologyDto.class));
        }
        return new PageImpl<>(technologiesDto);
    }

    public Technology getTechnologyById(Integer idTechnology){
        return technologyRepository.findById(idTechnology).orElse(null);
    }

    public TechnologyDto getTechnologyDtoById(Integer idTechnology) {
        Technology technology = technologyRepository.findById(idTechnology).orElse(null);
        return modelMapper.map(technology, TechnologyDto.class);
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
