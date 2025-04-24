package com.visitas.visitas.visitas.infrastructure.exceptionshandler;
import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.exceptions.InvalidPaginationException;
import com.visitas.visitas.visitas.domain.exceptions.NullException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NullException.class)
    public ResponseEntity<ExceptionResponse> handleNullException(NullException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage(),
                LocalDateTime.now()));
    }
    @ExceptionHandler(InvalidPaginationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPaginationException(InvalidPaginationException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage(),
                LocalDateTime.now()));
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(exception.getMessage(), LocalDateTime.now())
        );
    }
    @ExceptionHandler(InvalidException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidException(InvalidException exception) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(exception.getMessage(),
                LocalDateTime.now()));
    }
}
