package validators;

import exeptions.ExitProgramException;
import exeptions.WrongArgumentsException;
import statuses.Request;

public class UpdateByIdValidator extends ReadValidator {
    @Override
    public Request validate(String command, String args, boolean parse, int user_id) throws ExitProgramException {
        try {
            checkIfOneArgument(command, args);
            return super.validate(command, args, parse, user_id);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("ID должен быть целым числом");
            return null;
        }
    }
}