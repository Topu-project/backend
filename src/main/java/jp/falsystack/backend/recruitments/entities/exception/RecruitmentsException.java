package jp.falsystack.backend.recruitments.entities.exception;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Recruitments Service 최상위 에러
@Getter
public abstract class RecruitmentsException extends RuntimeException {

    private final Map<String, String> errors = new ConcurrentHashMap<>();

    public RecruitmentsException(String message) {
        super(message);
    }

    public RecruitmentsException(String message, Throwable cause) {
        super(message, cause);
    }

    public void addErrorMessages(Map<String, String> errors) {
        this.errors.putAll(errors);
    }

    public abstract int getStatusCode();
}
