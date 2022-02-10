package moby.testbackend.exception;

public class CandidateNotExistsException extends Exception{

    public CandidateNotExistsException(String message){
        super(message);
    }
}
