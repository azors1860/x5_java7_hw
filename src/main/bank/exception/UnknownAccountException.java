package exception;

/**
 * Выбрасывается, чтобы указать, что аккаунт не найден.
 */
public class UnknownAccountException extends Exception {
    public UnknownAccountException(String message) {
        super(message);
    }
}
