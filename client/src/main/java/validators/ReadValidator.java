package validators;

import command.ExecuteScriptCommand;
import data.Coordinates;
import data.Location;
import data.Route;
import exeptions.ExitProgramException;
import exeptions.ToMuchAttemptsException;
import exeptions.WrongArgumentsException;
import statuses.Request;

import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ReadValidator extends Validator {
    protected boolean needParse = true;

    private Route newRoute = null;

    private final static int maxCountOfAttempts = 50000;

    @Override
    public Request validate(String command, String args, boolean parse, int user_id) throws ExitProgramException {
        try {
            Route route = null;
            if (!parse)
                route = readRoute(user_id);
            else {
                route = parseRoute(ExecuteScriptCommand.readAllArguments(), user_id);
            }

            return super.validate(command, args, route);
        } catch (WrongArgumentsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    /**
     * Метод считывающий значения полей для типа Vehicle из консоли
     * @return объект Vehicle с полями, прошедшими валидацию
     * @throws WrongArgumentsException  исключение выбрасывающееся при неверном типе аргументов
     * @throws ExitProgramException исключение для выхода из программы
     */
    public Route readRoute(int user_id) throws WrongArgumentsException, ExitProgramException {
        Scanner scanner = new Scanner(System.in);

        long xC = 0;
        Double yC = 0.0;

        Double xL = 0.0;
        int yL = 0;
        int zL = 0;

        String name = null;
        Coordinates coordinates = null;
        Location from = null;
        Location to = null;
        float distance = 0;

        try {
            xC = askCoordinateXC(scanner, "");
            yC = askCoordinateYC(scanner, "");

            xL = askCoordinateXL(scanner, "");
            yL = askCoordinateYL(scanner, "");
            zL = askCoordinateZL(scanner, "");

            name = askName(scanner,"");
            distance = askDistance(scanner, "");
            coordinates = new Coordinates(xC, yC);
            from = new Location(xL, yL, zL);
            to = new Location(xL, yL, zL);


            newRoute = new Route(name, coordinates, from, to, distance, user_id);
        } catch (NumberFormatException n) {
            System.out.println("Введенные значения не соответствуют типам Java.");
        } catch (NoSuchElementException e) {
            System.out.println("Выход из программы...");
            throw new ExitProgramException();
        } catch (ToMuchAttemptsException e) {
            throw new RuntimeException(e);
        }
        return newRoute;
    }

    /**
     * Метод для получения значений полей для типа Route из файла
     * @param args аргументы считанные из файла
     * @return объект Route с полями, прошедшими валидацию
     * @throws WrongArgumentsException исключение выбрасывающееся при неверном типе аргументов
     */
    public Route parseRoute(ArrayList<String> args, int user_id) throws WrongArgumentsException {

        long xC = 0;
        Double yC = 0.0;

        Double xL = 0.0;
        int yL = 0;
        int zL = 0;

        String name = null;
        Coordinates coordinates = null;
        Location from = null;
        Location to = null;
        float distance = 0;

        if (args == null) {
            System.out.println("Аргументы для команды не указаны. " +
                    "Исправьте файл и попробуйте ещё.");
        } else if (args.size() < 6 || args.size() > 7) {
            System.out.println("В файле неверное количество аргументов " +
                    "для команды. Исправьте файл и попробуйте ещё.");
        } else {
            try {

                xC = askCoordinateXC(null, args.get(0));
                yC = askCoordinateYC(null, args.get(1));

                xL = askCoordinateXL(null, args.get(2));
                yL = askCoordinateYL(null, args.get(3));
                zL = askCoordinateZL(null, args.get(4));

                name = askName(null, args.get(5));
                distance = askDistance(null, args.get(6));
                coordinates = new Coordinates(xC, yC);
                from = new Location(xL, yL, zL);
                to = new Location(xL, yL, zL);

                newRoute = new Route(name, coordinates, from, to, distance, user_id);
            } catch (NumberFormatException n) {
                System.out.println("Введенные значения не соответствуют типам Java.");
            } catch (ToMuchAttemptsException e) {
                throw new RuntimeException(e);
            }
        }
        return newRoute;
    }

    /**
     * Запрашивает имя
     * @param str строка для имени
     * @return верное имя
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public String askName(Scanner scanner, String str) throws ToMuchAttemptsException, WrongArgumentsException {
        String name;
        if (str.isEmpty()) {
            System.out.println("Введите имя пути: ");
            System.out.print(">> ");
            str = scanner.nextLine();
            name = checkName(str, scanner);
        } else {
            name = checkName(str, scanner);
        }
        return name;
    }

    /**
     * Запрашивает XC
     * @param argForX строка для координаты X
     * @return коордианату подходящую под требования
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public Long askCoordinateXC(Scanner scanner, String argForX) throws ToMuchAttemptsException, WrongArgumentsException {
        long x = 0;
        if (argForX.isEmpty()) {
            System.out.println("Введите координату X (типа Long):");
            System.out.print(">> ");
            argForX = scanner.nextLine();
        }
        x = checkIfInteger(argForX, scanner, "Координата X");

        return x;
    }

    /**
     * Запрашивает координату YC
     * @param argForY строка для координаты Y
     * @return коордианату подходящую под требования
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public Double askCoordinateYC(Scanner scanner, String argForY) throws ToMuchAttemptsException, WrongArgumentsException {
        double y = 0;
        if (argForY.isEmpty()) {
            System.out.println("Введите координату Y (типа double):");
            System.out.print(">> ");
            argForY = scanner.nextLine();
        }
        y = checkIfDouble(argForY, scanner, "Координата Y");
        while (y <= -102) {
            System.out.println("Введенные данные не соответствуют требованиям. Попробуйте ещё.");
            System.out.print(">> ");
            y = checkIfInteger(scanner.nextLine(), scanner, "Координата Y");
        }
        return y;
    }

    /**
     * Запрашивает XL
     * @param argForX строка для координаты X
     * @return коордианату подходящую под требования
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public Double askCoordinateXL(Scanner scanner, String argForX) throws ToMuchAttemptsException, WrongArgumentsException {
        double x = 0;
        if (argForX.isEmpty()) {
            System.out.println("Введите координату X (типа double):");
            System.out.print(">> ");
            argForX = scanner.nextLine();
        }
        x = checkIfDouble(argForX, scanner, "Координата X");

        return x;
    }

    /**
     * Запрашивает YL
     * @param argForY строка для координаты Y
     * @return коордианату подходящую под требования
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public Integer askCoordinateYL(Scanner scanner, String argForY) throws ToMuchAttemptsException, WrongArgumentsException {
        int y = 0;
        if (argForY.isEmpty()) {
            System.out.println("Введите координату Y (типа int):");
            System.out.print(">> ");
            argForY = scanner.nextLine();
        }
        y = checkIfInteger(argForY, scanner, "Координата Y");

        return y;
    }

    /**
     * Запрашивает ZL
     * @param argForZ строка для координаты Z
     * @return коордианату подходящую под требования
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public Integer askCoordinateZL(Scanner scanner, String argForZ) throws ToMuchAttemptsException, WrongArgumentsException {
        int z = 0;
        if (argForZ.isEmpty()) {
            System.out.println("Введите координату Y (типа int):");
            System.out.print(">> ");
            argForZ = scanner.nextLine();
        }
        z = checkIfInteger(argForZ, scanner, "Координата Y");

        return z;
    }

    /**
     * Запрашивает distance
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public Float askDistance(Scanner scanner, String argDistanse) throws ToMuchAttemptsException, WrongArgumentsException {
        float distance = 0;
        if (argDistanse.isEmpty()) {
            System.out.println("Введите distance (типа float):");
            System.out.print(">> ");
            argDistanse = scanner.nextLine();
        }
        distance = checkIfInteger(argDistanse, scanner, "Координата Y");

        return distance;
    }


    /**
     * Проверяет, является ли данная строка str числом типа int
     * @param str строка
     * @param scanner сканер для запрашивания повторного ввода
     * @param fieldName имя поля, для которого идет проверка
     * @return числовое значение для поля
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public static Integer checkIfInteger (String str, Scanner scanner, String fieldName) throws ToMuchAttemptsException,
            WrongArgumentsException {
        int n = 0;
        str = str.toLowerCase(Locale.ROOT);
        if (!str.isEmpty() && ('i' == str.charAt(str.length() - 1) || 'l' == str.charAt(str.length() - 1))) {
            str = str.substring(0, str.length() - 1);
        }
        while (str.isEmpty() || !(Pattern.matches("-\\d+|\\d+", str))) {
            if (n > maxCountOfAttempts) {
                throw new ToMuchAttemptsException();
            }
            if (scanner.equals(null)) {
                throw new WrongArgumentsException(fieldName + " не подходящего типа. Команда не будет выполнена.");
            }
            System.out.println(fieldName + " не подходящего типа. Попробуйте еще:");
            System.out.print(">> ");
            str = scanner.nextLine();
            if (!str.isEmpty() && ('i' == str.charAt(str.length() - 1) || 'l' == str.charAt(str.length() - 1))) {
                str = str.substring(0, str.length() - 1);
            }
            n++;
        }
        return Integer.parseInt(str);
    }

    /**
     * Проверяет, является ли данная строка str числом типа double
     * @param str строка
     * @param scanner сканер для запрашивания повторного ввода
     * @param fieldName имя поля, для которого идет проверка
     * @return числовое значение для поля
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public static Double checkIfDouble (String str, Scanner scanner, String fieldName) throws ToMuchAttemptsException {
        int n = 0;
        str = str.toLowerCase(Locale.ROOT);
        str = str.replace(",", ".");

        if (!str.isEmpty() && ('f' == str.charAt(str.length() - 1) || 'd' == str.charAt(str.length() - 1))) {
            str = str.substring(0, str.length() - 1);
        }
        while (str.isEmpty() || !(Pattern.matches("^[-+]?[0-9]*[.,]?[0-9]+(?:[eE][-+]?[0-9]+)?$", str))) {
            if (n > maxCountOfAttempts) {
                throw new ToMuchAttemptsException();
            }
            System.out.println(fieldName + " в неправильном формате. Попробуйте еще:");
            System.out.print(">> ");
            str = scanner.nextLine();
            if (!str.isEmpty() && ('f' == str.charAt(str.length() - 1) || 'd' == str.charAt(str.length() - 1))) {
                str = str.substring(0, str.length() - 1);
            }
            n++;
        }
        return Double.parseDouble(str);
    }

    /**
     * Проверяет, соответствует ли строка str требованиям к полю имени
     * @param str строка
     * @param scanner сканер для запрашивания повторного ввода
     * @return верное имя
     * @throws ToMuchAttemptsException исключение для превышения попыток ввода
     */
    public static String checkName(String str, Scanner scanner) throws ToMuchAttemptsException,
            WrongArgumentsException {
        int n = 0;
        while (str == null || str.isEmpty()) {
            if (n > maxCountOfAttempts) {
                throw new ToMuchAttemptsException();
            }
            if (scanner.equals(null)) {
                throw new WrongArgumentsException("Имя в неправильном формате. Команда не будет выполнена.");
            }
            System.out.println("Имя в неправильном формате. Попробуйте еще:");
            System.out.print(">> ");
            str = scanner.nextLine();
            n++;
        }
        return str;
    }

    public boolean getNeedParse() {
        return needParse;
    }
}