package exception;

/**
 * Выбрасывается, чтобы показать, что возникли проблемы при прочтении файла
 * или преобразовании информации из файла в объекты аккаунта.
 */
public class IncorrectFileDb extends Exception {

    public IncorrectFileDb(String message) {
        super(message);
    }

    public IncorrectFileDb(String message, Throwable e) {
        super(message, e);
    }
}
