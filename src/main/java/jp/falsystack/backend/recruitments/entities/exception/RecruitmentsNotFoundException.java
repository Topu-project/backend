package jp.falsystack.backend.recruitments.entities.exception;

import org.springframework.http.HttpStatus;

public class RecruitmentsNotFoundException extends RecruitmentsException {

    private static final String MESSAGE = "해당 모집글을 찾을 수 없습니다.";

    public RecruitmentsNotFoundException() {
        super(MESSAGE);
    }

    public RecruitmentsNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int getStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}
