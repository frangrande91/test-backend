package moby.testbackend.exception;

public class TechnologyAlreadyExistsException extends Exception{

    public TechnologyAlreadyExistsException(String message){
        super(message);
    }
}
