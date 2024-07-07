package jp.falsystack.backend.recruitments.controllers;

import jp.falsystack.backend.recruitments.controllers.out.RecruitmentsErrorResponse;
import jp.falsystack.backend.recruitments.entities.exception.RecruitmentsException;
import jp.falsystack.backend.web.out.TopuServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    private static Map<String, String> convertFieldErrorsListToFieldErrorsMap(List<FieldError> fieldErrors) {
        Map<String, String> validationErrors = new ConcurrentHashMap<>();
        fieldErrors.forEach(fieldError -> validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return validationErrors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public TopuServiceResponse invalidRequestHandler(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());

        return TopuServiceResponse.from(null, RecruitmentsErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .validationErrors(convertFieldErrorsListToFieldErrorsMap(ex.getFieldErrors()))
                .build());
    }

    @ExceptionHandler(RecruitmentsException.class)
    public TopuServiceResponse recruitmentsException(RecruitmentsException ex) {
        log.error(ex.getMessage());

        return TopuServiceResponse.from(null, RecruitmentsErrorResponse.builder()
                .code(ex.getStatusCode())
                .message(ex.getMessage())
                .build());
    }
}
