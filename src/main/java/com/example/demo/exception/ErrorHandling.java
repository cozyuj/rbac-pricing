package com.example.demo.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorHandling {
    /*
     * System Exception
     */
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "ER001","예상치 못한 오류가 발생했습니다."),
    BADREQUEST_EXCEPTION(HttpStatus.BAD_REQUEST, "ER400","잘못된 요청입니다."), //400
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "ER401","해당 서비스는 로그인이 필요합니다."), //401
    FORBIDDEN_EXCEPTION(HttpStatus.FORBIDDEN,"ER403","해당 권한이 없습니다."), //403
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND,"ER404", "요청하신 페이지가 없습니다."), //404
    DUPLICATE_REQUEST(HttpStatus.CONFLICT, "ER409", "중복된 요청입니다."), // 409
    TOO_MANY_REQUEST_EXCEPTION(HttpStatus.TOO_MANY_REQUESTS,"ER429","너무 많은 요청이 발생했습니다. 잠시후 다시 시도해주세요."),//429
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ER005","예상치 못한 오류가 발생했습니다."), //500
    KEY_DUPLICATION(HttpStatus.INTERNAL_SERVER_ERROR, "E0010", "데이터가 중복됐습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private String message;

    ErrorHandling(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }

    ErrorHandling(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
