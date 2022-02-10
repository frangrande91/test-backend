package moby.testbackend.exception;

public class RestrictDeleteException extends Exception {

    public RestrictDeleteException(String messge){
        super(messge);
    }
}
