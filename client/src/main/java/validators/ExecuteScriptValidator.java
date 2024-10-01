package validators;

import exeptions.WrongArgumentsException;
import statuses.Request;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ExecuteScriptValidator extends Validator {

    private Set<String> scriptFilenames;
    public Request validate(String commandName, String args) {
        scriptFilenames = new HashSet<>();
        try {
            checkIfOneArgument(commandName, args);
            if (!checkIfNoRecursion(args)) {
                return null;
            }
            return new Request(commandName, args, null);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean checkIfNoRecursion(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            Scanner scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("execute_script")) {
                    String[] parts = line.split(" ");
                    if (parts.length > 1) {
                        String scriptFilename = parts[1];
                        if (scriptFilenames.contains(scriptFilename)) {
                            System.out.println("Обнаружена рекурсия в файле " + filename);
                            return false;
                        }
                        scriptFilenames.add(scriptFilename);
                        return checkIfNoRecursion(scriptFilename);
                    }
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла " + filename);
            return false;

        }
    }
}