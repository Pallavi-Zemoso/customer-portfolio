package com.zemoso.demo.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;


@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException exception, HttpHeaders headers,
                                                                   HttpStatus status, WebRequest request) {
        return buildErrorResponse(exception, "Access denied", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildErrorResponse(exception, "No such page", HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(exception.getMessage());
        return buildErrorResponse(exception, errorMsg, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return buildErrorResponse(exception, "Request body is empty", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoResultFoundException.class)
    public ResponseEntity<Object> handleNoResultFoundException(NoResultFoundException exception, WebRequest request) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException exception,
                                                             WebRequest request) {
        return buildErrorResponse(exception, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> processTypeMismatchException(ErrorResponse errorResponse, TypeMismatchException exception) {
        String errorMessage = TypeMismatchException.ERROR_CODE + " : Failed to convert " + ClassUtils.getDescriptiveType(exception.getValue())
                + (exception.getRequiredType() == null ? "" : " to required type " + ClassUtils.getQualifiedName(exception.getRequiredType()))
                + (exception instanceof MethodArgumentTypeMismatchException ?
                " for property: " + ((MethodArgumentTypeMismatchException) exception).getParameter().getParameterName() : ""
        );
        errorResponse.setMessage(errorMessage);
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return buildErrorResponse(exception, status, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus status, WebRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), status, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception, String message, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        exception.printStackTrace();
        return new ResponseEntity(errorResponse, status);
    }

}