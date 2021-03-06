package moby.testbackend.service;

import lombok.extern.java.Log;
import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyAlreadyExistsException;
import moby.testbackend.exception.TechnologyNotExistsException;
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

@Log
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

    public Technology addTechnology(Technology technology) throws TechnologyAlreadyExistsException {
        if(!isNull(technologyRepository.findByNameAndVersion(technology.getName(), technology.getVersion())))
            throw new TechnologyAlreadyExistsException("Technology " + technology.getName() + " version " + technology.getVersion() + " already exists");
        else {
            log.info("Technology has been created");
            return technologyRepository.save(technology);
        }
    }

    public Page<TechnologyDto> getAllTechnologies(Pageable pageable) {
        List<TechnologyDto> technologiesDto = new ArrayList<>();
        for (Technology technology : technologyRepository.findAll(pageable)){
            technologiesDto.add(modelMapper.map(technology, TechnologyDto.class));
        }
        return new PageImpl<>(technologiesDto);
    }

    public Technology getTechnologyById(Integer idTechnology) throws TechnologyNotExistsException {
        return technologyRepository.findById(idTechnology).orElseThrow(() -> new TechnologyNotExistsException("Technology not exists") );
    }

    public TechnologyDto getTechnologyDtoById(Integer idTechnology) {
        Technology technology = technologyRepository.findById(idTechnology).orElse(null);
        return modelMapper.map(technology, TechnologyDto.class);
    }

    public Technology updateTechnology(Technology technology) throws TechnologyNotExistsException {
        if(technology.getIdTechnology() == null || getTechnologyById(technology.getIdTechnology()) == null)
            throw new TechnologyNotExistsException("Technology not exists");
        else {
            log.info("Technology updated succesfully");
            return technologyRepository.save(technology);
        }
    }

    public void deleteTechnology(Integer idTechnology) throws TechnologyNotExistsException, RestrictDeleteException {
        Technology technology = getTechnologyById(idTechnology);
        if(!candidateForTechnologyService.getCandidatesForTechnologyByTechnology(technology).isEmpty())
            throw new RestrictDeleteException("Can not delete this technology because it depends of another objects");
        else {
            log.info("Technology deleted succesfully");
            technologyRepository.deleteById(idTechnology);
        }
    }
}
