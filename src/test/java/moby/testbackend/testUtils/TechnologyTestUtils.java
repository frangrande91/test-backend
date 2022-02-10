package moby.testbackend.testUtils;

import moby.testbackend.model.Technology;
import moby.testbackend.model.dto.TechnologyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class TechnologyTestUtils {

    public static Technology getTechnology() {
        return Technology.builder()
                .idTechnology(1)
                .name("Python")
                .version("3.10")
                .build();
    }

    public static TechnologyDto getTechnologyDto(){
        return TechnologyDto.builder()
                .name("Python")
                .version("3.10")
                .build();
    }

    public static Page<TechnologyDto> getPageTechnologyDto() {
        return new PageImpl<>(getListTechnologyDto());
    }

    public static List<TechnologyDto> getListTechnologyDto() {
        return List.of(getTechnologyDto());
    }

}
