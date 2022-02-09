package moby.testbackend.service;

import moby.testbackend.model.Technology;
import moby.testbackend.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    @Autowired
    public TechnologyService(TechnologyRepository technologyRepository){
        this.technologyRepository = technologyRepository;
    }

    public Technology getTechnologyById(Integer idTechnology){
        return technologyRepository.findById(idTechnology).orElse(null);
    }
}
