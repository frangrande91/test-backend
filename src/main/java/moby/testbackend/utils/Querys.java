package moby.testbackend.utils;

public class Querys {

    private Querys(){
        throw new IllegalStateException("Utility class");
    }

    public static final String CANDIDADES_BY_TECHNOLOGY = "SELECT cxt.id_candidate_for_technology, cxt.id_candidate, cxt.id_technology, cxt.years_experience " +
                                                            "FROM candidates_for_technologies AS cxt " +
                                                            "JOIN technologies AS t " +
                                                            "ON cxt.id_technology = t.id_technology " +
                                                            "WHERE t.name = ?1";
}
