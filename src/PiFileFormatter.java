import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PiFileFormatter {
    private static final int LINE_LENGTH = 60;  // Длина строки для каждой линии в формате
    private static final String INPUT_FILE_PATH = "C:\\Users\\алексей\\Downloads\\10m.txt";  // Путь к файлу с числом π
    private static final String OUTPUT_FILE_PATH = "C:\\MyProject\\untitled\\src\\FormattedPi.java";  // Относительный путь для сохранения результата

    public static void main(String[] args) {
        try {
            String formattedPi = formatPiFromFile(INPUT_FILE_PATH);
            saveFormattedPiToFile(OUTPUT_FILE_PATH, formattedPi);
            System.out.println("Форматирование завершено и сохранено в " + OUTPUT_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для форматирования числа π
    private static String formatPiFromFile(String filePath) throws IOException {
        StringBuilder piBuilder = new StringBuilder();
        StringBuilder formattedBuilder = new StringBuilder();
        int lineCharCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                piBuilder.append(line.trim());  // Убираем пробелы и добавляем к строке
            }
        }

        formattedBuilder.append("public static final String PI_DIGITS = \n    \"");

        for (int i = 0; i < piBuilder.length(); i++) {
            formattedBuilder.append(piBuilder.charAt(i));
            lineCharCount++;

            // Разбиваем строки для читаемости
            if (lineCharCount == LINE_LENGTH) {
                formattedBuilder.append("\" +\n    \"");
                lineCharCount = 0;
            }
        }
        // Завершаем строку
        formattedBuilder.append("\";\n");
        return formattedBuilder.toString();
    }

    // Метод для сохранения отформатированной строки в файл
    private static void saveFormattedPiToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
