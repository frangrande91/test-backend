package moby.testbackend.service;

import moby.testbackend.exception.CandidateAlreadyExistsException;
import moby.testbackend.exception.CandidateForTechnologyAlreadyExistsException;
import moby.testbackend.exception.CandidateNotExistsException;
import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.Candidate;
import moby.testbackend.model.dto.CandidateDto;
import moby.testbackend.repository.CandidateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static moby.testbackend.testUtils.CandidateForTechnologyTestUtils.getListCandidateForTechnology;
import static moby.testbackend.testUtils.CandidateForTechnologyTestUtils.getListExperienceDto;
import static moby.testbackend.testUtils.CandidateTestUtils.getCandidate;
import static moby.testbackend.testUtils.CandidateTestUtils.getCandidateDto;
import static moby.testbackend.testUtils.CandidateTestUtils.getPageCandidate;
import static moby.testbackend.testUtils.CandidateTestUtils.getPageCandidateDto;
import static moby.testbackend.testUtils.CandidateTestUtils.getSetCandidateDto;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnology;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CandidateServiceTest {

    private CandidateRepository candidateRepository;
    private TechnologyService technologyService;
    private CandidateForTechnologyService candidateForTechnologyService;
    private CandidateService candidateService;

    @Before
    public void setUp() {
        this.candidateRepository = mock(CandidateRepository.class);
        this.technologyService = mock(TechnologyService.class);
        this.candidateForTechnologyService = mock(CandidateForTechnologyService.class);
        this.candidateService = new CandidateService(candidateRepository, technologyService, candidateForTechnologyService);
    }

    @Test
    public void addCandidateOkTest() throws CandidateAlreadyExistsException {
        when(candidateRepository.findByIdCandidateOrDocument(1, "12345")).thenReturn(null);
        when(candidateRepository.save(getCandidate())).thenReturn(getCandidate());

        Candidate candidate = candidateService.addCandidate(getCandidate());

        assertNotNull(candidate);
        assertEquals(getCandidate(), candidate);
        verify(candidateRepository, times(1)).findByIdCandidateOrDocument(1, "12345");
        verify(candidateRepository, times(1)).save(getCandidate());
    }

    @Test
    public void addCandidateAlreadyExistsTest() {
        when(candidateRepository.findByIdCandidateOrDocument(1, "12345")).thenReturn(getCandidate());

        Assertions.assertThrows(CandidateAlreadyExistsException.class, () -> candidateService.addCandidate(getCandidate()));
        verify(candidateRepository, times(1)).findByIdCandidateOrDocument(1, "12345");
        verify(candidateRepository, times(0)).save(getCandidate());
    }

    @Test
    public void getAllCandidatesOkTest() {
        Pageable pageable = PageRequest.of(1, 1);
        when(candidateRepository.findAll(pageable)).thenReturn(getPageCandidate());
        when(candidateForTechnologyService.getExperiencesByCandidate(getCandidate())).thenReturn(getListExperienceDto());

        Page<CandidateDto> pageCandidateDto = candidateService.getAllCandidates(pageable);

        assertEquals(getPageCandidateDto().getTotalElements(), pageCandidateDto.getTotalElements());
        assertEquals(getPageCandidateDto().getTotalPages(), pageCandidateDto.getTotalPages());
        assertEquals(getPageCandidateDto().getContent(), pageCandidateDto.getContent());
        verify(candidateRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getCandidateByIdOkTest() throws CandidateNotExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));

        Candidate candidate = candidateService.getCandidateById(1);

        assertEquals(getCandidate(), candidate);
        verify(candidateRepository, times(1)).findById(1);
    }

    @Test
    public void getCandidateByIdNotExistsTest() {
        when(candidateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CandidateNotExistsException.class, () -> candidateService.getCandidateById(1));
        verify(candidateRepository, times(1)).findById(1);
    }

    @Test
    public void getCandidateDtoByIdOkTest() throws CandidateNotExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));
        when(candidateForTechnologyService.getExperiencesByCandidate(getCandidate())).thenReturn(getListExperienceDto());

        CandidateDto candidateDto = candidateService.getCandidateDtoById(1);

        assertEquals(getCandidateDto(), candidateDto);
        verify(candidateRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(1)).getExperiencesByCandidate(getCandidate());
    }

    @Test
    public void getCandidateDtoByIdNotExistsTest() {
        when(candidateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CandidateNotExistsException.class, () -> candidateService.getCandidateDtoById(1));
        verify(candidateRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(0)).getExperiencesByCandidate(getCandidate());
    }

    @Test
    public void getCandidatesByTechnologyOkTest() {
        when(candidateForTechnologyService.getCandidatesForTechnologyByNameTechnology("Python")).thenReturn(getListCandidateForTechnology());
        when(candidateForTechnologyService.getExperiencesByCandidate(getCandidate())).thenReturn(getListExperienceDto());

        Set<CandidateDto> candidates = candidateService.getCandidatesByTechnology("Python");

        assertEquals(getSetCandidateDto(), candidates);
        verify(candidateForTechnologyService, times(1)).getCandidatesForTechnologyByNameTechnology("Python");
    }

    @Test
    public void addTechnologyToCandidateOkTest() throws TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException, CandidateNotExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));
        when(technologyService.getTechnologyById(1)).thenReturn(getTechnology());
        doNothing().when(candidateForTechnologyService).addCandidateForTechnology(getCandidate(), getTechnology(), 1);

        Candidate candidate = candidateService.addTechnologyToCandidate(1,1,1);

        assertEquals(getCandidate(), candidate);
        verify(candidateRepository, times(1)).findById(1);
        verify(technologyService, times(1)).getTechnologyById(1);
        verify(candidateForTechnologyService, times(1)).addCandidateForTechnology(getCandidate(), getTechnology(), 1);
    }

    @Test
    public void addTechnologyToCandidateCandidateNotExistsTest() throws TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CandidateNotExistsException.class, () -> candidateService.addTechnologyToCandidate(1, 1, 1));
        verify(candidateRepository, times(1)).findById(1);
        verify(technologyService, times(0)).getTechnologyById(1);
        verify(candidateForTechnologyService, times(0)).addCandidateForTechnology(getCandidate(), getTechnology(), 1);
    }
    
    @Test
    public void addTechnologyToCandidateTechnologyNotExistsTest() throws TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));
        when(technologyService.getTechnologyById(1)).thenThrow(TechnologyNotExistsException.class);

        assertThrows(TechnologyNotExistsException.class, () -> candidateService.addTechnologyToCandidate(1, 1, 1));
        verify(candidateRepository, times(1)).findById(1);
        verify(technologyService, times(1)).getTechnologyById(1);
        verify(candidateForTechnologyService, times(0)).addCandidateForTechnology(getCandidate(), getTechnology(), 2);
    }

    @Test
    public void addTechnologyToCandidateCandidateForTechnologyAlreadyExistsTest() throws TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));
        when(technologyService.getTechnologyById(1)).thenReturn(getTechnology());
        doThrow(CandidateForTechnologyAlreadyExistsException.class).when(candidateForTechnologyService).addCandidateForTechnology(getCandidate(), getTechnology(), 1);

        assertThrows(CandidateForTechnologyAlreadyExistsException.class, () -> candidateService.addTechnologyToCandidate(1, 1, 1));
        verify(candidateRepository, times(1)).findById(1);
        verify(technologyService, times(1)).getTechnologyById(1);
        verify(candidateForTechnologyService, times(1)).addCandidateForTechnology(getCandidate(), getTechnology(), 1);
    }

    @Test
    public void updateCandidateOkTest() throws CandidateNotExistsException {
        when(candidateRepository.findByIdCandidateOrDocument(1, "12345")).thenReturn(getCandidate());
        when(candidateRepository.save(getCandidate())).thenReturn(getCandidate());

        Candidate candidate = candidateService.updateCandidate(getCandidate());

        assertEquals(getCandidate(), candidate);
        verify(candidateRepository, times(1)).findByIdCandidateOrDocument(1, "12345");
        verify(candidateRepository, times(1)).save(getCandidate());
    }

    @Test
    public void updateCandidateNotExistsTest() {
        when(candidateRepository.findByIdCandidateOrDocument(1, "12345")).thenReturn(null);

        assertThrows(CandidateNotExistsException.class, () -> candidateService.updateCandidate(getCandidate()));
        verify(candidateRepository, times(1)).findByIdCandidateOrDocument(1, "12345");
        verify(candidateRepository, times(0)).save(getCandidate());
    }

    @Test
    public void deleteCandidateOkTest() throws RestrictDeleteException, CandidateNotExistsException {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));
        when(candidateForTechnologyService.getCandidatesForTechnologyByCandidate(getCandidate())).thenReturn(List.of());
        doNothing().when(candidateRepository).deleteById(1);

        candidateService.deleteCandidate(1);

        verify(candidateRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(1)).getCandidatesForTechnologyByCandidate(getCandidate());
        verify(candidateRepository, times(1)).deleteById(1);
    }

    @Test
    public void deleteCandidateNotExistsTest() {
        when(candidateRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CandidateNotExistsException.class, () -> candidateService.deleteCandidate(1));
        verify(candidateRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(0)).getCandidatesForTechnologyByCandidate(getCandidate());
        verify(candidateRepository, times(0)).deleteById(1);
    }

    @Test
    public void deleteCandidateRestrictDeleteTest() {
        when(candidateRepository.findById(1)).thenReturn(Optional.of(getCandidate()));
        when(candidateForTechnologyService.getCandidatesForTechnologyByCandidate(getCandidate())).thenReturn(getListCandidateForTechnology());

        assertThrows(RestrictDeleteException.class, () -> candidateService.deleteCandidate(1));
        verify(candidateRepository, times(1)).findById(1);
        verify(candidateForTechnologyService, times(1)).getCandidatesForTechnologyByCandidate(getCandidate());
        verify(candidateRepository, times(0)).deleteById(1);
    }





}


