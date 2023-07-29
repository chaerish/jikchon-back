package smu.likelion.jikchon.exception;

public class CustomForbiddenException extends CustomException{
    public CustomForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
