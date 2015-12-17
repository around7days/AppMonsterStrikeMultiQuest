package main.base;

import javax.servlet.ServletException;

/**
 * Ajax Error返却用Exceptionクラス
 */
@SuppressWarnings("javadoc")
public class AjaxErrorException extends ServletException {
    public AjaxErrorException() {
    }

    public AjaxErrorException(String message) {
        super(message);
    }

    public AjaxErrorException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public AjaxErrorException(Throwable rootCause) {
        super(rootCause);
    }

    public Throwable getRootCause() {
        return getCause();
    }
}
