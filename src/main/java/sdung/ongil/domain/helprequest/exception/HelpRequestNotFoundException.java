package sdung.ongil.domain.helprequest.exception;

public class HelpRequestNotFoundException extends RuntimeException {
    public HelpRequestNotFoundException(String requestId) {
        super("해당 요청을 찾을 수 없습니다. requestId=" + requestId);
    }
}
