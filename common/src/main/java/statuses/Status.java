package statuses;

import java.io.Serial;
import java.io.Serializable;

/**
 * Статус после выполнения команды. Содержит результат и ответ от сервера
 */
public abstract class Status implements Serializable {
    @Serial
    private static final long serialVersionUID = 103;
    private String response;
    private final String result;

    Status(String result) {
        this.result = result;
    }
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResult() {
        return result;
    }
}