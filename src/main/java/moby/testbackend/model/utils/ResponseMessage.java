package moby.testbackend.model.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseMessage {
   private String message;
}
