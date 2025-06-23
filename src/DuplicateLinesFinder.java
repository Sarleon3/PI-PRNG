import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DuplicateLinesFinder {

    public static void main(String[] args) {
        // Укажите путь к вашему файлу
        String filePath = "I:\\results.txt";
        findDuplicateLines(filePath);
    }

    private static void findDuplicateLines(String filePath) {
        HashMap<String, Integer> lineCounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineCounts.put(line, lineCounts.getOrDefault(line, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Повторяющиеся строки:");
        boolean hasDuplicates = false;
        for (String line : lineCounts.keySet()) {
            if (lineCounts.get(line) > 1) {
                System.out.println(line + " (встречается " + lineCounts.get(line) + " раз)");
                hasDuplicates = true;
            }
        }

        if (!hasDuplicates) {
            System.out.println("Повторяющихся строк не найдено.");
        }
    }
}