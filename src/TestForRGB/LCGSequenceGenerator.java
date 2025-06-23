package TestForRGB;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class LCGSequenceGenerator {
    private static final int sequenceCount = 65536; // Количество чисел для генерации
    private static final int numberLength = 8;      // Длина чисел
    private static final String outputFilePath = "I:\\testPi\\resultsForLCG.txt"; // Путь для сохранения результата
    private static final String rgbImagePath = "I:\\testPi\\LCG_rgb_image.png";
    private static final String spatialImagePath = "I:\\testPi\\LCG_spatial_image.png";

    // Инициализация генератора с произвольными значениями
    private static LinearCongruentialGenerator lcg = new LinearCongruentialGenerator();

    public static void main(String[] args) {
        // Пример изменения параметров
        int seed = 1;
        long randMax = (long) Math.pow(2, 32);
        lcg.setSeed(seed);
        lcg.setMod(randMax);
        lcg.setMultiplier(214013);
        lcg.setIncrement(2531011);

        long startTime = System.currentTimeMillis();

        // Генерация последовательности чисел
        ArrayList<String> sequenceList = generateNumberSequence(sequenceCount);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Сохранение результатов и времени выполнения
        saveResultsToFile(outputFilePath, sequenceList, duration);

        generateRGBImage(sequenceList);
        generateSpatialImage(sequenceList);

        System.out.println("Время выполнения: " + duration + " мс");
    }

    // Метод для генерации последовательности чисел
    public static ArrayList<String> generateNumberSequence(int count) {
        ArrayList<String> sequenceList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StringBuilder number = new StringBuilder();
            for (int j = 0; j < numberLength; j++) {
                number.append(lcg.next() % 10);
            }
            sequenceList.add(number.toString());
        }
        return sequenceList;
    }

    // Остальные методы остаются без изменений
    private static void saveResultsToFile(String filePath, ArrayList<String> sequenceList, long duration) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Время выполнения: " + duration + " мс\n\n");
            writer.write("Последовательность чисел:\n");
            for (String number : sequenceList) {
                writer.write(number + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void generateRGBImage(ArrayList<String> sequenceList) {
        int width = 512;  // Ширина изображения
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

    class LinearCongruentialGenerator {
        private long a = 1, c = 0, m = (long) Math.pow(2, 31), seed = 1;

        public void setMultiplier(long a) {
            this.a = a;
        }

        public void setIncrement(long c) {
            this.c = c;
        }

        public void setMod(long m) {
            this.m = m;
        }

        public void setSeed(long seed) {
            this.seed = seed;
        }

        public long next() {
            seed = (a * seed + c)% m;
            return seed;
        }
    }