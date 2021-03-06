package moby.testbackend.controller;

import io.swagger.annotations.ApiOperation;
import moby.testbackend.exception.CandidateAlreadyExistsException;
import moby.testbackend.exception.CandidateForTechnologyAlreadyExistsException;
import moby.testbackend.exception.CandidateNotExistsException;
import moby.testbackend.exception.RestrictDeleteException;
import moby.testbackend.exception.TechnologyNotExistsException;
import moby.testbackend.model.Candidate;
import moby.testbackend.model.dto.CandidateDto;
import moby.testbackend.model.utils.ResponseMessage;
import moby.testbackend.service.CandidateService;
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
import java.util.Set;

import static moby.testbackend.utils.EntityURLBuilder.buildURL;
import static moby.testbackend.utils.ResponseUtil.listResponseEntity;
import static moby.testbackend.utils.ResponseUtil.messageResponse;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    @Autowired
    public CandidateController(CandidateService candidateService){
        this.candidateService = candidateService;
    }


    @PostMapping
    public ResponseEntity<ResponseMessage> addCandidate(@RequestBody Candidate candidate) throws CandidateAlreadyExistsException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(buildURL("candidates", candidateService.addCandidate(candidate).getIdCandidate()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageResponse("Candidate has been created"));
    }

    @GetMapping
    public ResponseEntity<List<CandidateDto>> getAllCandidates(@RequestParam (value = "size", defaultValue = "10") Integer size,
                                                                @RequestParam (value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page, size);
        return listResponseEntity(candidateService.getAllCandidates(pageable));
    }

    @GetMapping("/{idCandidate}")
    public ResponseEntity<CandidateDto> getCandidateById(@PathVariable Integer idCandidate) throws CandidateNotExistsException {
        return ResponseEntity.ok(candidateService.getCandidateDtoById(idCandidate));
    }

    @ApiOperation(value = "Get all candidates by technology name", notes = "Get all candidates who have this technology with their years of experience in each version")
    @GetMapping("/technologies/{nameTechnology}")
    public ResponseEntity<Set<CandidateDto>> getCandidatesByTechnology(@PathVariable String nameTechnology){
        return ResponseEntity.ok(candidateService.getCandidatesByTechnology(nameTechnology));
    }

    @PutMapping("/{idCandidate}/technologies/{idTechnology}")
    public ResponseEntity<ResponseMessage> addTechnologyToCandidate(@PathVariable Integer idCandidate, @PathVariable Integer idTechnology, @RequestParam Integer yearsExperience) throws CandidateNotExistsException, TechnologyNotExistsException, CandidateForTechnologyAlreadyExistsException {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .location(buildURL("candidates", candidateService.addTechnologyToCandidate(idCandidate, idTechnology, yearsExperience).getIdCandidate()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(messageResponse("Technology added to candidate"));
    }

    @PutMapping()
    public ResponseEntity<ResponseMessage> updateCandidate(@RequestBody Candidate candidate) throws CandidateNotExistsException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .location(buildURL("candidates", candidateService.updateCandidate(candidate).getIdCandidate()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(messageResponse("Candidate has been updated"));
    }

    @ApiOperation(value = "Physical deletion", notes = "Does not delete candidates that have associated technologies")
    @DeleteMapping("/{idCandidate}")
    public ResponseEntity<ResponseMessage> deleteCandidate(@PathVariable Integer idCandidate) throws CandidateNotExistsException, RestrictDeleteException {
        candidateService.deleteCandidate(idCandidate);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(messageResponse("Candidate succesfully removed"));
    }
}
