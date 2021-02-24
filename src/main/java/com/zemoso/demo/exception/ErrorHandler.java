package com.zemoso.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

@Slf4j
@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(AccessDeniedException.class)
    protected ModelAndView handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        return buildErrorResponse(exception, "Access denied", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ModelAndView handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception,
                                                                        HttpServletRequest request) {
        return buildErrorResponse(exception, "Invalid request", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView handleResponseStatusException(ResponseStatusException exception, HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ModelAndView handleValidationException(ValidationException exception, HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST, request);
    }

    protected ModelAndView handleNoHandlerFoundException(NoHandlerFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(exception, "No such page", HttpStatus.NOT_FOUND, request);
    }

    protected ModelAndView handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
        String errorMsg = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(exception.getMessage());
        return buildErrorResponse(exception, errorMsg, HttpStatus.BAD_REQUEST, request);
    }

    protected ModelAndView handleHttpMessageNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {
        return buildErrorResponse(exception, "Request body is empty", HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(NoResultFoundException.class)
    public ModelAndView handleNoResultFoundException(NoResultFoundException exception, HttpServletRequest request) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleAllUncaughtException(RuntimeException exception,  HttpServletRequest request) {
        return buildErrorResponse(exception, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ModelAndView buildErrorResponse(Exception exception, HttpStatus status, HttpServletRequest request) {
        return buildErrorResponse(exception, exception.getMessage(), status, request);
    }

    private ModelAndView buildErrorResponse(Exception exception, String message, HttpStatus status, HttpServletRequest request) {
        log.error(request + ":\n" + exception.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(status.value(), message);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorObject", errorResponse);
        modelAndView.setViewName("/error/error");
        modelAndView.setStatus(status);
        return modelAndView;
    }

}