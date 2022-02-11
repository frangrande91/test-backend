package moby.testbackend.service;

import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyAlreadyExistsException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.Technology;
import moby.testbackend.model.dto.TechnologyDto;
import moby.testbackend.repository.TechnologyRepository;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static moby.testbackend.testUtils.CandidateForTechnologyTestUtils.getListCandidateForTechnology;
import static moby.testbackend.testUtils.TechnologyTestUtils.getPageTechnology;
import static moby.testbackend.testUtils.TechnologyTestUtils.getPageTechnologyDto;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnology;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnologyDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TechnologyServiceTest {

    private TechnologyRepository technologyRepository;
    private CandidateForTechnologyService candidateForTechnologyService;
    private ModelMapper modelMapper;
    private TechnologyService technologyService;

    @Before
    public void setUp() {
        technologyRepository = mock(TechnologyRepository.class);
        candidateForTechnologyService = mock(CandidateForTechnologyService.class);
        modelMapper = mock(ModelMapper.class);
        technologyService = new TechnologyService(technologyRepository, candidateForTechnologyService, modelMapper);
    }

    @Test
    public void addTechnologyOkTest() throws TechnologyAlreadyExistsException {
        when(technologyRepository.findByNameAndVersion("Python", "3.10")).thenReturn(null);
        when(technologyRepository.save(getTechnology())).thenReturn(getTechnology());

        Technology technology = technologyService.addTechnology(getTechnology());

        assertEquals(getTechnology(), technology);
        verify(technologyRepository, times(1)).findByNameAndVersion("Python", "3.10");
        verify(technologyRepository, times(1)).save(getTechnology());
    }

    @Test
    public void addTechnologyAlreadyExists() {
        when(technologyRepository.findByNameAndVersion("Python", "3.10")).thenReturn(getTechnology());

        assertThrows(TechnologyAlreadyExistsException.class, () -> technologyService.addTechnology(getTechnology()));
        verify(technologyRepository, times(1)).findByNameAndVersion("Python", "3.10");
        verify(technologyRepository, times(0)).save(getTechnology());
    }

    @Test
    public void getAllTechnologiesOkTest() {
        Pageable pageable = PageRequest.of(1, 1);
        when(technologyRepository.findAll(pageable)).thenReturn(getPageTechnology());
        when(modelMapper.map(getTechnology(), TechnologyDto.class)).thenReturn(getTechnologyDto());

        Page<TechnologyDto> pageTechnologyDto = technologyService.getAllTechnologies(pageable);

        assertEquals(getPageTechnologyDto().getContent(), pageTechnologyDto.getContent());
        assertEquals(getPageTechnologyDto().getTotalPages(), pageTechnologyDto.getTotalPages());
        assertEquals(getPageTechnologyDto().getTotalElements(), pageTechnologyDto.getTotalElements());
        verify(technologyRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getTechnologyByIdOkTest() throws TechnologyNotExistsException {
        when(technologyRepository.findById(1)).thenReturn(Optional.of(getTechnology()));

        Technology technology = technologyService.getTechnologyById(1);

        assertEquals(getTechnology(), technology);
        verify(technologyRepository, times(1)).findById(1);
    }

    @Test
    public void getTechnologyByIdNotExistsTest() {
        when(technologyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TechnologyNotExistsException.class, () -> technologyService.getTechnologyById(1));
        verify(technologyRepository, times(1)).findById(1);
    }

    @Test
    public void getTechnologyDtoByIdOkTest() {
        when(technologyRepository.findById(1)).thenReturn(Optional.of(getTechnology()));
        when(modelMapper.map(getTechnology(), TechnologyDto.class)).thenReturn(getTechnologyDto());

        TechnologyDto technologyDto = technologyService.getTechnologyDtoById(1);

        assertEquals(getTechnologyDto(), technologyDto);
        verify(technologyRepository, times(1)).findById(1);
    }

   @Test
   public void updateTechnologyOkTest() throws TechnologyNotExistsException {
        when(technologyRepository.findById(1)).thenReturn(Optional.of(getTechnology()));
        when(technologyRepository.save(getTechnology())).thenReturn(getTechnology());

        Technology technology = technologyService.updateTechnology(getTechnology());

        assertEquals(getTechnology(), technology);
        verify(technologyRepository, times(1)).findById(1);
        verify(technologyRepository, times(1)).save(getTechnology());
   }

    @Test
    public void updateTechnologyNotExistsTest() {
        when(technologyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TechnologyNotExistsException.class, () -> technologyService.updateTechnology(getTechnology()));
        verify(technologyRepository, times(1)).findById(1);
        verify(technologyRepository, times(0)).save(getTechnology());
    }

    @Test
    public void deleteTechnologyOkTest() throws TechnologyNotExistsException, RestrictDeleteException {
        when(technologyRepository.findById(1)).thenReturn(Optional.of(getTechnology()));
        when(candidateForTechnologyService.getCandidatesForTechnologyByTechnology(getTechnology())).thenReturn(List.of());
        doNothing().when(technologyRepository).deleteById(1);

        technologyService.deleteTechnology(1);

        verify(technologyRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(1)).getCandidatesForTechnologyByTechnology(getTechnology());
        verify(technologyRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteTechnologyNotExistsTest() {
        when(technologyRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TechnologyNotExistsException.class, () -> technologyService.deleteTechnology(1));
        verify(technologyRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(0)).getCandidatesForTechnologyByTechnology(getTechnology());
        verify(technologyRepository, times(0)).deleteById(1);
    }

    @Test
    public void deleteTechnologyRestrictDeleteTest() {
        when(technologyRepository.findById(1)).thenReturn(Optional.of(getTechnology()));
        when(candidateForTechnologyService.getCandidatesForTechnologyByTechnology(getTechnology())).thenReturn(getListCandidateForTechnology());

        assertThrows(RestrictDeleteException.class, () -> technologyService.deleteTechnology(1));
        verify(technologyRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(1)).getCandidatesForTechnologyByTechnology(getTechnology());
        verify(technologyRepository, times(0)).deleteById(1);
    }





}
