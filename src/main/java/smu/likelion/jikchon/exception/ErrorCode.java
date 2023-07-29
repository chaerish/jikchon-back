package smu.likelion.jikchon.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /**
     * 성공 코드
     */
    OK(200, "요청이 정상적으로 수행되었습니다."),

    /**
     * 400 BAD REQUEST
     */
    BAD_REQUEST(400,"잘못된 요청입니다."),
    INVALID_PARAMETER(40001, "요청 파라미터 오류"),

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND(404, "요청한 자원을 찾을 수 없습니다."),
    NOT_FOUND_COUNTRY(40401, "존재하지 않는 국가코드 입니다."),
    NOT_FOUND_SAMPLE(40402, "존재하지 않는 샘플 입니다.");

    private final int code;
    private final String message;
}
