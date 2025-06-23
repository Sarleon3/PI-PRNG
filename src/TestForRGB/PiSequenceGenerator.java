package TestForRGB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class PiSequenceGenerator {

    private static String PI_DIGITS;               // Хранение числа π
    private static int currentIndex = 0;           // Индекс для текущего положения окна
    private static int sequenceCount = 50000;     // Количество чисел для генерации
    private static int numberLength = 9;           // Длина чисел
    private static String piFilePath = "C:\\Users\\алексей\\Downloads\\10m.txt"; // Путь к файлу с числом π
    private static String outputFilePath = "I:\\testPi\\resultsforPi.txt"; // Путь для сохранения результата
    private static String rgbImagePath = "I:\\testPi\\PI_rgb_image.png"; // Путь для сохранения RGB изображения
    private static String spatialImagePath = "I:\\testPi\\PI_spatial_image.png"; // Путь для сохранения пространственного изображения

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis(); // Начало измерения времени

        // Загружаем число π из текстового файла
        loadPiFromFile(piFilePath);

        // Генерируем последовательность чисел
        ArrayList<String> sequenceList = generateNumberSequence(sequenceCount);

        long endTime = System.currentTimeMillis(); // Конец измерения времени
        long duration = endTime - startTime;       // Время выполнения в миллисекундах

        // Сохраняем результаты и время выполнения в файл
        saveResultsToFile(outputFilePath, sequenceList, duration);

        // Создаем и сохраняем изображения
        generateRGBImage(sequenceList);
        generateSpatialImage(sequenceList);

        System.out.println("Время выполнения: " + duration + " мс");
    }

    // Метод для загрузки числа π из файла
    private static void loadPiFromFile(String filePath) {
        try {
            // Читаем весь файл за один раз
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            PI_DIGITS = new String(bytes).replaceAll("[^0-9]", ""); // Убираем все ненужные символы (например, точки)
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при загрузке числа π из файла.");
        }
    }

    // Метод для генерации последовательности чисел
    public static ArrayList<String> generateNumberSequence(int count) {
        ArrayList<String> sequenceList = new ArrayList<>(count);
        for (int i = 0; i < count && currentIndex + numberLength <= PI_DIGITS.length(); i++) {
            String number = PI_DIGITS.substring(currentIndex, currentIndex + numberLength);
            sequenceList.add(number);
            currentIndex++;
        }
        return sequenceList;
    }

    // Метод для сохранения результатов и времени выполнения в файл
    private static void saveResultsToFile(String filePath, ArrayList<String> sequenceList, long duration) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Время выполнения: " + duration + " мс\n\n"); // Запись времени выполнения в начало файла
            writer.write("Последовательность чисел:\n");
            for (String number : sequenceList) {
                writer.write(number + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для генерации RGB-изображения
    private static void generateRGBImage(ArrayList<String> sequenceList) {
        int width = 256;  // Ширина изображения
        int height = sequenceList.size() / width + 1;  // Высота изображения

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int x = 0;
        int y = 0;

        for (String number : sequenceList) {
            int i = Integer.parseInt(number);  // Преобразуем строку в число
            Color color = getColor(i);

            image.setRGB(x, y, color.getRGB());

            // Переходим к следующему пикселю
            x++;
            if (x == width) {
                x = 0;
                y++;
            }
        }

        try {
            ImageIO.write(image, "PNG", new File(rgbImagePath));  // Сохраняем изображение
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для получения цвета на основе числа
    private static Color getColor(int i) {
        float r = getBytePart(i, 0) / 255f;
        float g = getBytePart(i, 1) / 255f;
        float b = getBytePart(i, 2) / 255f;
        return new Color(r, g, b);
    }

    // Метод для получения значения определенного байта
    private static int getBytePart(int i, int byteIndex) {
        return ((i >> (8 * byteIndex)) & 0xFF);
    }

    // Генерация пространственного изображения
    public static void generateSpatialImage(ArrayList<String> sequenceList) {
        int width = 256;  // 256 возможных значений для X (8 бит)
        int height = 256; // 256 возможных значений для Y (8 бит)

        int[][] hits = new int[width][height];  // Массив для подсчета попаданий
        int maxHits = 0;

        // Обрабатываем каждое число из последовательности
        for (String number : sequenceList) {
            int i = Integer.parseInt(number); // Преобразуем строку в число
            int x = (i >> 8) & 0xFF;  // Берем старшие 8 бит для X (сдвигаем вправо на 8 бит)
            int y = i & 0xFF;          // Берем младшие 8 бит для Y (первые 8 бит)

            hits[x][y]++;  // Увеличиваем количество попаданий в точку

            // Обновляем максимальное количество попаданий
            if (hits[x][y] > maxHits) {
                maxHits = hits[x][y];
            }
        }

        // Создаем изображение размером 256x256
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Преобразуем количество попаданий в интенсивность и заполняем изображение
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Нормализуем интенсивность: чем больше попаданий, тем темнее пиксель
                float intensity = 1.0f - (float) hits[x][y] / maxHits;
                Color color = new Color(intensity, intensity, intensity); // Смотрим на интенсивность как на оттенок серого
                image.setRGB(x, y, color.getRGB());
            }
        }

        // Сохраняем изображение
        try {
            ImageIO.write(image, "PNG", new File(spatialImagePath));  // Сохраняем изображение
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
