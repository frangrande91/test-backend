package moby.testbackend.controller;

import moby.testbackend.exception.CandidateAlreadyExistsException;
import moby.testbackend.exception.CandidateForTechnologyAlreadyExistsException;
import moby.testbackend.exception.CandidateNotExistsException;
import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.dto.CandidateDto;
import moby.testbackend.model.utils.ResponseMessage;
import moby.testbackend.service.CandidateService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static moby.testbackend.testUtils.CandidateTestUtils.getCandidate;
import static moby.testbackend.testUtils.CandidateTestUtils.getCandidateDto;
import static moby.testbackend.testUtils.CandidateTestUtils.getPageCandidateDto;
import static moby.testbackend.testUtils.CandidateTestUtils.getSetCandidateDto;
import static moby.testbackend.utils.EntityURLBuilder.buildURL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CandidateControllerTest {

    private CandidateService candidateService;
    private CandidateController candidateController;

    @Before
    public void setUp(){
        candidateService = mock(CandidateService.class);
        candidateController = new CandidateController(candidateService);
    }

    @Test
    public void addCandidateOkTest() throws CandidateAlreadyExistsException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(candidateService.addCandidate(getCandidate())).thenReturn(getCandidate());

        ResponseEntity<ResponseMessage> response = candidateController.addCandidate(getCandidate());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(buildURL("candidates", getCandidate().getIdCandidate()).toString()
                , Objects.requireNonNull(response.getHeaders().get("Location")).get(0));
        verify(candidateService, times(1)).addCandidate(getCandidate());
    }

    @Test
    public void getAllCandidatesOkTest() {
        Pageable pageable = PageRequest.of(1,1);
        when(candidateService.getAllCandidates(pageable)).thenReturn(getPageCandidateDto());

        ResponseEntity<List<CandidateDto>> response = candidateController.getAllCandidates(1,1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getPageCandidateDto().getContent().size(), Objects.requireNonNull(response.getBody()).size());
        assertEquals(getPageCandidateDto().getTotalPages()
                , Integer.parseInt(Objects.requireNonNull(response.getHeaders().get("x-Total-Count")).get(0)));
        assertEquals(getPageCandidateDto().getTotalElements()
                , Integer.parseInt(Objects.requireNonNull(response.getHeaders().get("x-Total-Pages")).get(0)));
    }

    @Test
    public void getCandidateByIdOkTest() throws CandidateNotExistsException {
        when(candidateService.getCandidateDtoById(1)).thenReturn(getCandidateDto());

        ResponseEntity<CandidateDto> response = candidateController.getCandidateById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getCandidateDto(), response.getBody());
        verify(candidateService, times(1)).getCandidateDtoById(1);
    }

    @Test
    public void getCandidatesByTechnologyOkTest(){
        when(candidateService.getCandidatesByTechnology("Java")).thenReturn(getSetCandidateDto());

        ResponseEntity<Set<CandidateDto>> response = candidateController.getCandidatesByTechnology("Java");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getSetCandidateDto().size(), Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    public void addTechnologyToCandidateOkTest() throws TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException, CandidateNotExistsException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(candidateService.addTechnologyToCandidate(1, 1, 1)).thenReturn(getCandidate());

        ResponseEntity<ResponseMessage> response = candidateController.addTechnologyToCandidate(1, 1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildURL("candidates", getCandidate().getIdCandidate()).toString()
                , Objects.requireNonNull(response.getHeaders().get("Location")).get(0));
        verify(candidateService, times(1)).addTechnologyToCandidate(1, 1, 1);
    }

    @Test
    public void updateCandidateOkTest() throws CandidateNotExistsException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(candidateService.updateCandidate(getCandidate())).thenReturn(getCandidate());

        ResponseEntity<ResponseMessage> response = candidateController.updateCandidate(getCandidate());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildURL("candidates", getCandidate().getIdCandidate()).toString()
                , Objects.requireNonNull(response.getHeaders().get("Location")).get(0));
        verify(candidateService, times(1)).updateCandidate(getCandidate());
    }

    @Test
    public void deleteCandidateOkTest() throws RestrictDeleteException, CandidateNotExistsException {
        doNothing().when(candidateService).deleteCandidate(1);

        ResponseEntity<ResponseMessage> response = candidateController.deleteCandidate(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(candidateService, times(1)).deleteCandidate(1);
    }

}
