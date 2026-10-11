package sdung.ongil.global.exception;

public enum ErrorCode {

    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    INVALID_REQUEST(400, "잘못된 요청입니다."),
    UNAUTHORIZED(401, "인증이 필요합니다."),
    ODSAY_QUOTA_EXCEEDED(429, "ODsay API 일일 사용 한도를 초과했습니다. 잠시 후 다시 시도해주세요."),
    ODSAY_API_ERROR(502, "외부 경로 탐색 서비스(ODsay) 호출 중 오류가 발생했습니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}