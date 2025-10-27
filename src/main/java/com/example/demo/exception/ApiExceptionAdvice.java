package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class}, basePackages = {"com.example.HyThon.web.controller"})
public class ApiExceptionAdvice {

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final RuntimeException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.RUNTIME_EXCEPTION.getCode())
                .errorMessage(ErrorHandling.RUNTIME_EXCEPTION.getCode())
                .build();

        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorHandling.RUNTIME_EXCEPTION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final BadRequestException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.BADREQUEST_EXCEPTION.getCode())
                .errorMessage(ErrorHandling.BADREQUEST_EXCEPTION.getMessage())
                .build();
        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorHandling.BADREQUEST_EXCEPTION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.ACCESS_DENIED_EXCEPTION.getCode())
                .errorMessage((ErrorHandling.ACCESS_DENIED_EXCEPTION.getMessage()))
                .build();
        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorHandling.ACCESS_DENIED_EXCEPTION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({HttpClientErrorException.Forbidden.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final HttpClientErrorException.Forbidden e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.FORBIDDEN_EXCEPTION.getCode())
                .errorMessage((ErrorHandling.FORBIDDEN_EXCEPTION.getMessage()))
                .build();
        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorHandling.FORBIDDEN_EXCEPTION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({HttpServerErrorException.InternalServerError.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final Exception e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.INTERNAL_SERVER_ERROR.getCode())
                .errorMessage((ErrorHandling.INTERNAL_SERVER_ERROR.getMessage()))
                .build();
        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorHandling.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({SQLException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final SQLException e1, final DataIntegrityViolationException e2) {
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.KEY_DUPLICATION.getCode())
                .errorMessage((ErrorHandling.KEY_DUPLICATION.getMessage()))
                .build();
        log.error("API Exception occurred: {}", e1.getMessage(), e1, e2.getMessage(), e2);
        return ResponseEntity
                .status(ErrorHandling.KEY_DUPLICATION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({ChangeSetPersister.NotFoundException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final ChangeSetPersister.NotFoundException e) {
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.NOT_FOUND_EXCEPTION.getCode())
                .errorMessage((ErrorHandling.NOT_FOUND_EXCEPTION.getMessage()))
                .build();
        e.printStackTrace();

        return ResponseEntity
                .status(ErrorHandling.NOT_FOUND_EXCEPTION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    @ExceptionHandler({HttpClientErrorException.TooManyRequests.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final HttpClientErrorException.TooManyRequests e) {
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ErrorHandling.TOO_MANY_REQUEST_EXCEPTION.getCode())
                .errorMessage((ErrorHandling.TOO_MANY_REQUEST_EXCEPTION.getMessage()))
                .build();
        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(ErrorHandling.TOO_MANY_REQUEST_EXCEPTION.getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }


    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final ApiException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(e.getError().getCode())
                .errorMessage(e.getError().getMessage())
                .build();
        log.error("API Exception occurred: {}", e.getMessage(), e);
        return ResponseEntity
                .status(e.getError().getHttpStatus())
                .body(ApiResult.builder()
                        .status("ERROR")
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

}
