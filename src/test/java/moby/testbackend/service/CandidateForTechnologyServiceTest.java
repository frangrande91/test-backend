package moby.testbackend.service;

import moby.testbackend.exception.CandidateForTechnologyAlreadyExistsException;
import moby.testbackend.model.CandidateForTechnology;
import moby.testbackend.model.dto.ExperienceDto;
import moby.testbackend.repository.CandidateForTechnologyRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static moby.testbackend.testUtils.CandidateForTechnologyTestUtils.getCandidateForTechnology;
import static moby.testbackend.testUtils.CandidateForTechnologyTestUtils.getListCandidateForTechnology;
import static moby.testbackend.testUtils.CandidateForTechnologyTestUtils.getListExperienceDto;
import static moby.testbackend.testUtils.CandidateTestUtils.getCandidate;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnology;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CandidateForTechnologyServiceTest {

    private CandidateForTechnologyRepository candidateForTechnologyRepository;
    private CandidateForTechnologyService candidateForTechnologyService;

    @Before
    public void setUp() {
        candidateForTechnologyRepository = mock(CandidateForTechnologyRepository.class);
        candidateForTechnologyService = new CandidateForTechnologyService(candidateForTechnologyRepository);
    }


    @Test
    public void addCandidateForTechnologyOkTest() throws CandidateForTechnologyAlreadyExistsException {
        when(candidateForTechnologyRepository.findByCandidateAndTechnology(getCandidate(), getTechnology())).thenReturn(null);
        when(candidateForTechnologyRepository.save(getCandidateForTechnology())).thenReturn(getCandidateForTechnology());

        candidateForTechnologyService.addCandidateForTechnology(getCandidate(), getTechnology(), 1);

        verify(candidateForTechnologyRepository, times(1)).findByCandidateAndTechnology(getCandidate(), getTechnology());
        verify(candidateForTechnologyRepository, times(1)).save(getCandidateForTechnology());
    }

    @Test
    public void addCandidateForTechnologyAlreadyExistsTest() {
        when(candidateForTechnologyRepository.findByCandidateAndTechnology(getCandidate(), getTechnology())).thenReturn(getCandidateForTechnology());

        assertThrows(CandidateForTechnologyAlreadyExistsException.class, () -> candidateForTechnologyService.addCandidateForTechnology(getCandidate(), getTechnology(), 1));
        verify(candidateForTechnologyRepository, times(1)).findByCandidateAndTechnology(getCandidate(), getTechnology());
        verify(candidateForTechnologyRepository, times(0)).save(getCandidateForTechnology());
    }

    @Test
    public void getCandidatesForTechnologyByCandidateOkTest() {
        when(candidateForTechnologyRepository.findByCandidate(getCandidate())).thenReturn(getListCandidateForTechnology());

        List<CandidateForTechnology> cxt = candidateForTechnologyService.getCandidatesForTechnologyByCandidate(getCandidate());

        assertEquals(getListCandidateForTechnology().size(), cxt.size());
        assertEquals(getListCandidateForTechnology().get(0), cxt.get(0));
        verify(candidateForTechnologyRepository, times(1)).findByCandidate(getCandidate());
    }

    @Test
    public void getExperiencesByCandidateOkTest() {
        when(candidateForTechnologyRepository.findByCandidate(getCandidate())).thenReturn(getListCandidateForTechnology());

        List<ExperienceDto> experiencesDto = candidateForTechnologyService.getExperiencesByCandidate(getCandidate());

        assertEquals(getListExperienceDto().size(), experiencesDto.size());
        assertEquals(getListExperienceDto().get(0), experiencesDto.get(0));
        verify(candidateForTechnologyRepository, times(1)).findByCandidate(getCandidate());
    }

    @Test
    public void getCandidatesForTechnologyByTechnologyOkTest() {
        when(candidateForTechnologyRepository.findByTechnology(getTechnology())).thenReturn(getListCandidateForTechnology());

        List<CandidateForTechnology> cxt = candidateForTechnologyService.getCandidatesForTechnologyByTechnology(getTechnology());

        assertEquals(getListCandidateForTechnology().size(), cxt.size());
        assertEquals(getListCandidateForTechnology().get(0), cxt.get(0));
        verify(candidateForTechnologyRepository, times(1)).findByTechnology(getTechnology());
    }

    @Test
    public void getCandidatesForTechnologyByNameTechnologyOkTest() {
        when(candidateForTechnologyRepository.findByNameTechnology("Python")).thenReturn(getListCandidateForTechnology());

        List<CandidateForTechnology> cxt = candidateForTechnologyService.getCandidatesForTechnologyByNameTechnology("Python");

        assertEquals(getListCandidateForTechnology().size(), cxt.size());
        assertEquals(getListCandidateForTechnology().get(0), cxt.get(0));
        verify(candidateForTechnologyRepository, times(1)).findByNameTechnology("Python");
    }
}
