import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        // Задаем путь к файлу с числами π
        String filePath = "C:\\Users\\алексей\\Downloads\\10m.txt";

        // Читаем число π из файла
        String piDigits = readPiFromFile(filePath);
        if (piDigits == null || piDigits.isEmpty()) {
            System.out.println("Не удалось прочитать число π из файла.");
            return;
        }

        // Убираем точку (если она есть)
        piDigits = piDigits.replace(".", "");

        // Подсчет уникальных последовательностей
        int sequenceLength = 9;
        int[] result = countUniqueSequences(piDigits, sequenceLength);
        int totalCount = result[0];
        int uniqueCount = result[1];
        int repetitions = totalCount - uniqueCount;

        // Вывод результатов
        System.out.println("Общее количество последовательностей: " + totalCount);
        System.out.println("Количество уникальных последовательностей: " + uniqueCount);
        System.out.println("Количество повторений: " + repetitions);
    }

    // Метод для чтения числа π из текстового файла
    public static String readPiFromFile(String filePath) {
        StringBuilder piDigits = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                piDigits.append(line.trim()); // Убираем пробелы и переносы строк
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return piDigits.toString();
    }

    // Метод для подсчета уникальных последовательностей
    private static int[] countUniqueSequences(String piDigits, int length) {
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        // Извлечение 9-значных последовательностей
        for (int i = 0; i <= 1000000 /*piDigits.length() - length*/; i++) {
            String sequence = piDigits.substring(i, i + length);
            uniqueSequences.add(sequence);
            totalSequences++;
        }

        return new int[]{totalSequences, uniqueSequences.size()};
    }
}