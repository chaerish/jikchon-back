package smu.likelion.jikchon.exception;

public class CustomUnauthorizedException extends CustomException {
    public CustomUnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
