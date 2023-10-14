package ru.practicum.main_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.debug("Получен статус 409 CONFLICT: {}", e.getMessage(), e);

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MainServerException.class)
    public ResponseEntity<ErrorResponse> handleMainServerException(MainServerException e) {
        log.debug("Получен статус 400 BAD REQUEST: {}", e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .build();

        log.warn(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e) {
        log.debug("Получен статус 409 CONFLICT: {}", e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .reason("Integrity constraint has been violated.")
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now())
                .build();

        log.warn(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.debug("Получен статус 404 NOT FOUND: {}", e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.builder()
                .reason("The required object was not found.")
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now())
                .build();

        log.warn(errorResponse.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpRequestMethodNotSupportedException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(final Exception e) {
        log.debug("Получен статус 400 BAD REQUEST: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.builder().
                status(HttpStatus.BAD_REQUEST)
                .reason("Incorrectly made request.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn(response.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleThrowable(final Throwable e) {
        log.debug("Получен статус 500 INTERNAL SERVER ERROR: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.builder().
                status(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason("Произошла непредвиденная ошибка.")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        log.warn(response.toString());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
