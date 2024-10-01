package exeptions;

/**
 * Исключение, выбрасываемое при попытке установить невалидное имя.
 */
public class InvalidNameException extends ValidationException {
    /**
     * Instantiates a new Invalid name exception.
     */
    public InvalidNameException() {
        super("Имя должно быть не пустой строкой");
    }
}