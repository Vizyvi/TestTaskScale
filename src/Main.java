import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new WrongArgLengthException();
            } else if (args[0].equals("-") && args[1].equals("-")) {
                fromConsoleToConsole();
            } else if (correctPath(args[0]) && args[1].equals("-")) {
                fromFileToConsole(args[0]);
            } else if (args[0].equals("-")) {
                fromConsoleToFile(args[1]);
            } else if (!correctPath(args[0])) {
                throw new FileNotFoundException();
            } else {
                fromFileToFile(args[0], args[1]);
            }
        } catch (WrongArgLengthException ex) {
            System.out.println("Неверное количество аргументов. Необходимо 2!");
        } catch (FileNotFoundException ex) {
            System.out.println("Вы ввели несуществующий исходный файл или неверный выходной файл");
        } catch (WrongCommandException ex) {
            System.out.println("Вы ввели неверные данные");
        } catch (NumberFormatException ex) {
            System.out.println("Вы ввели неверные данные для подсчета");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void fromConsoleToFile(String outputPath) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            FileWriter fileWriter = new FileWriter(outputPath);
            String[] parts = reader.readLine().split("\\s+");
            ArrayList<Integer> list = createNumberList(parts);
            Integer result = actionSelector(parts[0], list);
            fileWriter.write(String.valueOf(result));
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void fromFileToFile(String inputPath, String outputPath) throws IOException {
        FileReader fileReader = new FileReader(inputPath);
        FileWriter fileWriter = new FileWriter(outputPath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] parts = bufferedReader.readLine().split("\\s+");
        ArrayList<Integer> list = createNumberList(parts);
        Integer result = actionSelector(parts[0], list);
        fileWriter.write(String.valueOf(result));
        fileReader.close();
        fileWriter.close();
    }

    private static void fromFileToConsole(String path) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] parts = bufferedReader.readLine().split("\\s+");
        ArrayList<Integer> list = createNumberList(parts);
        System.out.println(actionSelector(parts[0], list));
        fileReader.close();
    }

    private static void fromConsoleToConsole() throws NumberFormatException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String[] parts = reader.readLine().split("\\s+");
            ArrayList<Integer> list = createNumberList(parts);
            System.out.println(actionSelector(parts[0], list));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static Integer addition(List<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }

    private static Integer multiplication(List<Integer> list) {
        return list.stream().reduce(1, (i1, i2) -> i1 * i2);
    }

    private static Integer combined(List<Integer> list) {
        if (list.size() != 3) {
            throw new WrongCommandException();
        } else {
            Integer result = list.get(0) * list.get(1) + list.get(2);
            return result;
        }
    }

    private static Integer actionSelector(String command, List<Integer> numbers) throws WrongCommandException {
        switch (command) {
            case "add":
                return addition(numbers);
            case "mul":
                return multiplication(numbers);
            case "com":
                return combined(numbers);
            default:
                throw new WrongCommandException();
        }
    }

    private static boolean correctPath(String path) {
        try {
            FileReader file = new FileReader(path);
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }
    }

    private static ArrayList<Integer> createNumberList(String[] parts) throws NumberFormatException {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            list.add(Integer.parseInt(parts[i]));
        }
        return list;
    }

}

class WrongArgLengthException extends RuntimeException {
}

class WrongCommandException extends RuntimeException {
}
