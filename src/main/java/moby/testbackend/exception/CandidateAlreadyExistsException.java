package moby.testbackend.exception;

public class CandidateAlreadyExistsException extends Exception{

    public CandidateAlreadyExistsException(String message){
        super(message);
    }

}
