package moby.testbackend.controller;

import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyAlreadyExistsException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.dto.TechnologyDto;
import moby.testbackend.model.utils.ResponseMessage;
import moby.testbackend.service.TechnologyService;
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

import static moby.testbackend.testUtils.TechnologyTestUtils.getPageTechnologyDto;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnology;
import static moby.testbackend.testUtils.TechnologyTestUtils.getTechnologyDto;
import static moby.testbackend.utils.EntityURLBuilder.buildURL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TechnologyControllerTest {

    private TechnologyService technologyService;
    private TechnologyController technologyController;

    @Before
    public void setUp() {
        technologyService = mock(TechnologyService.class);
        technologyController = new TechnologyController(technologyService);
    }


    @Test
    public void addTechnologyOkTest() throws TechnologyAlreadyExistsException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(technologyService.addTechnology(getTechnology())).thenReturn(getTechnology());

        ResponseEntity<ResponseMessage> response = technologyController.addTechnology(getTechnology());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(buildURL("technologies", getTechnology().getIdTechnology()).toString()
                , Objects.requireNonNull(response.getHeaders().get("Location")).get(0));
        verify(technologyService, times(1)).addTechnology(getTechnology());
    }

    @Test
    public void getAllTechnologiesOkTest() {
        Pageable pageable = PageRequest.of(1, 1);
        when(technologyService.getAllTechnologies(pageable)).thenReturn(getPageTechnologyDto());

        ResponseEntity<List<TechnologyDto>> response = technologyController.getAllTechnologies(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getPageTechnologyDto().getContent().size(), Objects.requireNonNull(response.getBody()).size());
        assertEquals(getPageTechnologyDto().getTotalPages()
                , Integer.parseInt(Objects.requireNonNull(response.getHeaders().get("x-Total-Count")).get(0)));
        assertEquals(getPageTechnologyDto().getTotalElements()
                , Integer.parseInt(Objects.requireNonNull(response.getHeaders().get("x-Total-Pages")).get(0)));
    }

    @Test
    public void getTechnologyByIdOkTest() {
        when(technologyService.getTechnologyDtoById(1)).thenReturn(getTechnologyDto());

        ResponseEntity<TechnologyDto> response = technologyController.getTechnologyById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(getTechnologyDto(), response.getBody());
        verify(technologyService, times(1)).getTechnologyDtoById(1);
    }

    @Test
    public void updateTechnologyOkTest() throws TechnologyNotExistsException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        when(technologyService.updateTechnology(getTechnology())).thenReturn(getTechnology());

        ResponseEntity<ResponseMessage> response = technologyController.updateTechnology(getTechnology());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(buildURL("technologies", getTechnology().getIdTechnology()).toString()
                , Objects.requireNonNull(response.getHeaders().get("Location")).get(0));
        verify(technologyService, times(1)).updateTechnology(getTechnology());
    }

    @Test
    public void deleteTechnologyOkTest() throws TechnologyNotExistsException, RestrictDeleteException {
        doNothing().when(technologyService).deleteTechnology(1);

        ResponseEntity<ResponseMessage> response = technologyController.deleteTechnology(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(technologyService, times(1)).deleteTechnology(1);
    }




}
