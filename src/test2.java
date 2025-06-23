import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.ArrayList;

public class test2 {
    private static String PI_DIGITS;  // Будем хранить число π здесь
    private static int currentIndex = 0;  // Индекс для текущего положения окна
    private static HashSet<String> uniqueNumbers = new HashSet<>(); // Для хранения уникальных чисел
    private static ArrayList<String> duplicateNumbers = new ArrayList<>(); // Для хранения повторяющихся чисел

    // Список исключений
    private static HashSet<String> exceptions = new HashSet<>(); // Для хранения чисел-исключений
    private static ArrayList<String> highlightedExceptions = new ArrayList<>(); // Для хранения чисел-исключений, которые не сгенерированы

    private static int numberLength = 5; // Длина генерируемых чисел

    public static void main(String[] args) {
        // Загружаем числа-исключения из файла или создаем его, если он не существует
        loadOrCreateExceptionsFile("I:\\исключения.txt"); // Укажите путь к вашему файлу с исключениями

        // Загружаем число π из текстового файла
        loadPiFromFile("C:\\Users\\алексей\\Downloads\\10m.txt"); // Укажите путь к вашему файлу

        // Демонстрация работы метода
        while (currentIndex + numberLength <= PI_DIGITS.length()) {  // Генерация чисел, пока есть достаточное количество цифр
            String uniqueNumber = generateNextUniqueNumber();
            if (uniqueNumber != null) {
                System.out.println(uniqueNumber);
            }

            // Проверяем условие остановки
            if (uniqueNumbers.size() == exceptions.size()) {
                System.out.println("Количество уникальных чисел равно количеству исключений. Остановка программы.");
                break; // Останавливаем цикл
            }
        }

        // Записываем уникальные результаты в файл
        saveResultsToFile("I:\\results.txt"); // Укажите путь к вашему выходному файлу

        // Выводим выделенные исключения
        if (!highlightedExceptions.isEmpty()) {
            System.out.println("Числа-исключения, которые не были сгенерированы:");
            for (String exception : highlightedExceptions) {
                System.out.println(exception);
            }
        }
    }

    // Метод для загрузки чисел-исключений из файла или создания его
    private static void loadOrCreateExceptionsFile(String filePath) {
        try {
            // Попробуем загрузить исключения из файла
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = br.readLine()) != null) {
                exceptions.add(line.trim());  // Убираем пробелы и добавляем в набор исключений
            }
            br.close();
        } catch (IOException e) {
            // Если файл не найден, создаем новый файл
            System.out.println("Файл с исключениями не найден, создаем новый.");
            try {
                new BufferedWriter(new FileWriter(filePath)).close(); // Создаем новый файл
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new RuntimeException("Ошибка при создании файла исключений.");
            }
        }
    }

    // Метод для загрузки числа π из файла
    private static void loadPiFromFile(String filePath) {
        StringBuilder piBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                piBuilder.append(line.trim());  // Убираем пробелы и добавляем к строке
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при загрузке числа π из файла.");
        }

        PI_DIGITS = piBuilder.toString(); // Присваиваем загруженные цифры
    }

    // Метод для генерации следующего 9-значного числа
    public static String generateNextUniqueNumber() {
        // Проверка на инициализацию PI_DIGITS
        if (PI_DIGITS == null || PI_DIGITS.isEmpty()) {
            System.out.println("Ошибка: число π не инициализировано или пусто.");
            return null; // Выходим из метода, если число π не задано
        }

        // Проверка пределов индекса
        if (currentIndex + numberLength > PI_DIGITS.length()) {
            System.out.println("Индекс выходит за пределы длины числа π. Остановка генерации.");
            return null; // Если индекс выходит за пределы, возвращаем null
        }

        StringBuilder piFragment = new StringBuilder();

        // Генерация числа заданной длины на основе числа π
        for (int i = 0; i < numberLength; i++) {
            piFragment.append(PI_DIGITS.charAt(currentIndex + i));  // Берем символы по текущему индексу
        }

        // Проверка на повторение
        String generatedNumber = piFragment.toString();
        if (uniqueNumbers.contains(generatedNumber)) {
            System.out.println(generatedNumber + " - повторение!"); // Вывод информации о повторении
            duplicateNumbers.add(generatedNumber); // Сохраняем число как повторяющееся
            currentIndex++; // Увеличиваем индекс, чтобы пропустить это число
            return generateNextUniqueNumber(); // Рекурсивно вызываем метод для генерации нового числа
        }

        // Проверяем, есть ли число в исключениях
        if (exceptions.contains(generatedNumber)) {
            highlightedExceptions.add(generatedNumber); // Сохраняем число как исключение
            System.out.println(generatedNumber + " - исключение!"); // Вывод информации о числе-исключении

            // Записываем исключение в файл
            addExceptionToFile(generatedNumber);
            currentIndex++; // Увеличиваем индекс, чтобы пропустить это число
            return generateNextUniqueNumber(); // Рекурсивно вызываем метод для генерации нового числа
        }

        // Добавляем число в набор уникальных
        uniqueNumbers.add(generatedNumber);

        // Увеличиваем индекс для следующей итерации
        currentIndex++;

        return generatedNumber;
    }

    // Метод для сохранения уникальных результатов в файл
    private static void saveResultsToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Уникальные " + numberLength + "-значные числа:\n");
            for (String number : uniqueNumbers) {
                writer.write(number + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для добавления исключения в файл
    private static void addExceptionToFile(String exception) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("I:\\исключения.txt", true))) { // true для добавления в файл
            writer.write(exception + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}