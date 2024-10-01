package validators;

import statuses.Request;

public interface IValidator {
    Request validate(String command, String args);
}