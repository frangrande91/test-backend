package moby.testbackend.exception;

public class CandidateForTechnologyAlreadyExistsException extends Exception {

    public CandidateForTechnologyAlreadyExistsException(String message){
        super(message);
    }
}
