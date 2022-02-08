package moby.testbackend.model.enums;

public enum DocumentType {
    DNI("DNI"), LE("LE"), LC("LC");

    private final String value;

    DocumentType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
