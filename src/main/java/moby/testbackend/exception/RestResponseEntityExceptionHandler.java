package moby.testbackend.exception;

import moby.testbackend.model.utils.ApiError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static moby.testbackend.utils.ResponseUtil.messageResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleContraintViolation(ConstraintViolationException ex, WebRequest request){
        List<String> errors = new ArrayList<>();
        for(ConstraintViolation<?> violation : ex.getConstraintViolations()){
            errors.add(violation.getRootBeanClass() + " " + violation.getMessage());
        }
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({CandidateForTechnologyAlreadyExistsException.class})
    public ResponseEntity<Object> candidateForTechnologyAlreadyExists(CandidateForTechnologyAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({CandidateAlreadyExistsException.class})
    public ResponseEntity<Object> candidateAlreadyExists(CandidateAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({TechnologyAlreadyExistsException.class})
    public ResponseEntity<Object> technologyAlreadyExists(TechnologyAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity<Object> userAlreadyExists(UserAlreadyExistsException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({RestrictDeleteException.class})
    public ResponseEntity<Object> restrictDelete(RestrictDeleteException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({CandidateNotExistsException.class})
    public ResponseEntity<Object> candidateNotExists(CandidateNotExistsException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({TechnologyNotExistsException.class})
    public ResponseEntity<Object> technologyNotExists(TechnologyNotExistsException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageResponse(ex.getMessage()));
    }



}
