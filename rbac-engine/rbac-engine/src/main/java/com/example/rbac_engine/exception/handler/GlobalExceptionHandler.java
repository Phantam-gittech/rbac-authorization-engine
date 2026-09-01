package com.example.rbac_engine.exception.handler;

import com.example.rbac_engine.dto.response.ApiErrorResponse;
import com.example.rbac_engine.exception.BadRequestException;
import com.example.rbac_engine.exception.DataConflictException;
import com.example.rbac_engine.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(HttpStatus status, String error ,List<String> message, String path){
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(path)
                .build();

        return new ResponseEntity<>(apiErrorResponse, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Resource Not Found",
                List.of(ex.getMessage()),
                request.getRequestURI()

        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                List.of(ex.getMessage()),
                request.getRequestURI()

        );
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<ApiErrorResponse> handleDataConflictException(DataConflictException ex, HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "Data Conflict",
                List.of(ex.getMessage()),
                request.getRequestURI()

        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request){
        List<String> messages = ex.getBindingResult().getFieldErrors()
                .stream().map(error -> error.getField()+" : "+error.getDefaultMessage()).toList();
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Validation Failed",
                messages,
                request.getRequestURI()

        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, HttpServletRequest request){
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected Error",
                List.of("Unexpected Error Occurred"),
                request.getRequestURI()
        );
    }


}
