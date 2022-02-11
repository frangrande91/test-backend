package moby.testbackend.controller;

import io.swagger.annotations.ApiOperation;
import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyAlreadyExistsException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.Technology;
import moby.testbackend.model.dto.TechnologyDto;
import moby.testbackend.model.utils.ResponseMessage;
import moby.testbackend.service.TechnologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static moby.testbackend.utils.EntityURLBuilder.buildURL;
import static moby.testbackend.utils.ResponseUtil.listResponseEntity;
import static moby.testbackend.utils.ResponseUtil.messageResponse;

@RestController
@RequestMapping("/technologies")
public class TechnologyController {

    private final TechnologyService technologyService;

    @Autowired
    public TechnologyController(TechnologyService technologyService){
        this.technologyService = technologyService;
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> addTechnology(@RequestBody Technology technology) throws TechnologyAlreadyExistsException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(buildURL("technologies", technologyService.addTechnology(technology).getIdTechnology()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageResponse("Technology has been created"));
    }

    @GetMapping
    public ResponseEntity<List<TechnologyDto>> getAllTechnologies(@RequestParam (value = "size", defaultValue = "10") Integer size,
                                                                  @RequestParam (value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page, size);
        return listResponseEntity(technologyService.getAllTechnologies(pageable));
    }

    @GetMapping("/{idTechnology}")
    public ResponseEntity<TechnologyDto> getTechnologyById(@PathVariable Integer idTechnology){
        return ResponseEntity.ok(technologyService.getTechnologyDtoById(idTechnology));
    }

    @PutMapping()
    public ResponseEntity<ResponseMessage> updateTechnology(@RequestBody Technology technology) throws TechnologyNotExistsException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .location(buildURL("technologies", technologyService.updateTechnology(technology).getIdTechnology()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageResponse("Technology has been updated"));
    }

    @ApiOperation(value = "Physical deletion", notes = "Does not delete technologies that have associated candidates")
    @DeleteMapping("/{idTechnology}")
    public ResponseEntity<ResponseMessage> deleteTechnology(@PathVariable Integer idTechnology) throws TechnologyNotExistsException, RestrictDeleteException {
        technologyService.deleteTechnology(idTechnology);
        return ResponseEntity.ok(messageResponse("Technology has been deleted"));
    }
}
