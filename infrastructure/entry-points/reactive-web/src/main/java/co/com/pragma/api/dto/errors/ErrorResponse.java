package co.com.pragma.api.dto.errors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponse {

    private String mainMessage;
    private int code;
    private List<String> messages;
}
