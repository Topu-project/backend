package jp.falsystack.backend.recruitments.controllers.out;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class RecruitmentsErrorResponse {

    private final int code;
    private final String message;
    private Map<String, String> validationErrors = new ConcurrentHashMap<>();

    @Builder
    private RecruitmentsErrorResponse(int code, String message, Map<String, String> validationErrors) {
        this.code = code;
        this.message = message;
        this.validationErrors = validationErrors;
    }
}
