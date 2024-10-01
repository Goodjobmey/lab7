package exeptions;

/**
 * Исключение выбрасывающееся, когда превышено количество попыток для ввода
 */
public class ToMuchAttemptsException extends Exception{
    public ToMuchAttemptsException(){
        super();
    }
}