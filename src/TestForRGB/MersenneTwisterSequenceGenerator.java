package TestForRGB;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public class MersenneTwisterSequenceGenerator {
    private static final int sequenceCount = 65536;
    private static final int numberLength = 9;
    private static final String outputFilePath = "I:\\testPi\\resultsForMersenneTwister.txt";
    private static final String rgbImagePath = "I:\\testPi\\mersenneTwister_rgb_image.png";
    private static final String spatialImagePath = "I:\\testPi\\mersenneTwister_spatial_image.png";
    private static Random mt = new Random();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        ArrayList<String> sequenceList = generateNumberSequence(sequenceCount);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        saveResultsToFile(outputFilePath, sequenceList, duration);

        // Создаем и сохраняем изображения
        generateRGBImage(sequenceList);
        generateSpatialImage(sequenceList);

        System.out.println("Время выполнения: " + duration + " мс");
    }

    // Генерация последовательности чисел
    public static ArrayList<String> generateNumberSequence(int count) {
        ArrayList<String> sequenceList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            StringBuilder number = new StringBuilder();
            for (int j = 0; j < numberLength; j++) {
                number.append(mt.nextInt(10));
            }
            sequenceList.add(number.toString());
        }
        return sequenceList;
    }

    // Сохранение результатов в файл
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

    // Генерация RGB изображения
    private static void generateRGBImage(ArrayList<String> sequenceList) {
        int width = 256;
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

