package cn.ponfee.web.framework.auth;

/**
 * Invalid Jwt Exception
 * 
 * @author Ponfee
 */
public class InvalidJwtException extends Exception {

    private static final long serialVersionUID = 536228397329313030L;

    public InvalidJwtException() {
        super();
    }

    public InvalidJwtException(String message) {
        super(message);
    }

    public InvalidJwtException(Throwable cause) {
        super(cause);
    }

    public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }

}
