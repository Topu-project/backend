package jp.falsystack.backend.recruitments.controllers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jp.falsystack.backend.recruitments.controllers.out.RecruitmentsErrorResponse;
import jp.falsystack.backend.recruitments.entities.exception.RecruitmentsException;
import jp.falsystack.backend.web.out.TopuServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  private static Map<String, String> convertFieldErrorsListToFieldErrorsMap(
      List<FieldError> fieldErrors) {
    Map<String, String> validationErrors = new ConcurrentHashMap<>();
    fieldErrors.forEach(
        fieldError -> validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));
    return validationErrors;
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<TopuServiceResponse<Object, RecruitmentsErrorResponse>> invalidRequestHandler(
      MethodArgumentNotValidException ex) {
    log.error(ex.getMessage());

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(TopuServiceResponse.from(null, RecruitmentsErrorResponse.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .validationErrors(convertFieldErrorsListToFieldErrorsMap(ex.getFieldErrors()))
            .build()));
  }

  @ExceptionHandler(RecruitmentsException.class)
  public ResponseEntity<TopuServiceResponse<Object, RecruitmentsErrorResponse>> recruitmentsException(
      RecruitmentsException ex) {
    log.error(ex.getMessage());

    return ResponseEntity
        .status(ex.getStatusCode())
        .body(TopuServiceResponse.from(null, RecruitmentsErrorResponse.builder()
            .code(ex.getStatusCode())
            .message(ex.getMessage())
            .build()));
  }
}
