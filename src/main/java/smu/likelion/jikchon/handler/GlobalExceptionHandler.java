package smu.likelion.jikchon.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import smu.likelion.jikchon.base.BaseResponse;
import smu.likelion.jikchon.exception.CustomBadRequestException;
import smu.likelion.jikchon.exception.CustomForbiddenException;
import smu.likelion.jikchon.exception.CustomNotFoundException;
import smu.likelion.jikchon.exception.CustomUnauthorizedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse<?> customNotFoundHandler(CustomNotFoundException e) {
        return BaseResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(CustomUnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<?> customUnauthorizedHandler(CustomUnauthorizedException e) {
        return BaseResponse.fail(e.getErrorCode());
    }
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<?> customBadRequestHandler(CustomBadRequestException e) {
        return BaseResponse.fail(e.getErrorCode());
    }

    @ExceptionHandler(CustomForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse<?> customForbiddenHandler(CustomBadRequestException e) {
        return BaseResponse.fail(e.getErrorCode());
    }
}
