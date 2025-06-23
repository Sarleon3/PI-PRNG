import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;

public class CollisionProbabilityTest {

    public static String piDigits = readPiFromFile("C:\\Users\\алексей\\Downloads\\10m.txt");

    public static void main(String[] args) {
        int testCount = 50;         // Количество тестов для каждого генератора
        int sequenceCount = 100000;  // Количество последовательностей в каждом тесте
        int sequenceLength = 9;      // Длина каждой последовательности

        System.out.println("Средняя вероятность коллизии для каждого генератора после " + testCount + " тестов:\n");
        System.out.println("LCG Generator:");
        calculateAverageCollisionProbability(testCount, sequenceCount, sequenceLength, "LCG");

        System.out.println("\nMersenne Twister Generator:");
        calculateAverageCollisionProbability(testCount, sequenceCount, sequenceLength, "MersenneTwister");

        System.out.println("\nSecure Hash Generator (SHA-256):");
        calculateAverageCollisionProbability(testCount, sequenceCount, sequenceLength, "SHA256");

        System.out.println("\nFisher-Yates Shuffle Generator:");
        calculateAverageCollisionProbability(testCount, sequenceCount, sequenceLength, "FisherYates");

        System.out.println("\nПроверка последовательностей в числе π:");
        calculateAverageCollisionProbability(1, sequenceCount, sequenceLength, "PiDigits");
    }

    // Метод для подсчета и вывода средней вероятности коллизии
    public static void calculateAverageCollisionProbability(int testCount, int sequenceCount, int sequenceLength, String generatorType) {
        double totalCollisionProbability = 0;

        for (int i = 0; i < testCount; i++) {
            double collisionProbability = 0;

            switch (generatorType) {
                case "LCG":
                    collisionProbability = testLCG(sequenceCount, sequenceLength);
                    break;
                case "MersenneTwister":
                    collisionProbability = testMersenneTwister(sequenceCount, sequenceLength);
                    break;
                case "SHA256":
                    collisionProbability = testSecureHashGenerator(sequenceCount, sequenceLength);
                    break;
                case "FisherYates":
                    collisionProbability = testFisherYatesShuffle(sequenceCount, sequenceLength);
                    break;
                case "PiDigits":
                    collisionProbability = testPiDigits(sequenceCount, sequenceLength);
                    break;
            }
            totalCollisionProbability += collisionProbability;
        }

        double averageCollisionProbability = totalCollisionProbability / testCount;
        System.out.printf("Средняя вероятность коллизии: %.10f%%%n", averageCollisionProbability);
    }

    // Методы тестирования для каждого генератора
    public static double testLCG(int sequenceCount, int sequenceLength) {
        LinearCongruentialGenerator2 lcg = new LinearCongruentialGenerator2(1664525, 1013904223, (long) Math.pow(2, 32), 1);
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        for (int i = 0; i < sequenceCount; i++) {
            StringBuilder sequence = new StringBuilder();
            for (int j = 0; j < sequenceLength; j++) {
                sequence.append(lcg.next() % 10);
            }
            uniqueSequences.add(sequence.toString());
            totalSequences++;
        }
        return calculateCollisionProbability(totalSequences, uniqueSequences.size());
    }

    public static double testMersenneTwister(int sequenceCount, int sequenceLength) {
        Random mt = new Random();
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        for (int i = 0; i < sequenceCount; i++) {
            StringBuilder sequence = new StringBuilder();
            for (int j = 0; j < sequenceLength; j++) {
                sequence.append(mt.nextInt(10));
            }
            uniqueSequences.add(sequence.toString());
            totalSequences++;
        }
        return calculateCollisionProbability(totalSequences, uniqueSequences.size());
    }

    public static double testSecureHashGenerator(int sequenceCount, int sequenceLength) {
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        for (int i = 0; i < sequenceCount; i++) {
            String hash = generateRandomHash().substring(0, sequenceLength);
            uniqueSequences.add(hash);
            totalSequences++;
        }
        return calculateCollisionProbability(totalSequences, uniqueSequences.size());
    }

    public static double testFisherYatesShuffle(int sequenceCount, int sequenceLength) {
        int[] array = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;
        Random rand = new Random();

        for (int i = 0; i < sequenceCount; i++) {
            shuffle(array, rand);
            StringBuilder sequence = new StringBuilder();
            for (int j = 0; j < sequenceLength; j++) {
                sequence.append(array[j]);
            }
            uniqueSequences.add(sequence.toString());
            totalSequences++;
        }
        return calculateCollisionProbability(totalSequences, uniqueSequences.size());
    }



    // Вспомогательные методы
    private static String generateRandomHash() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] seed = new byte[32];
            random.nextBytes(seed);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(seed);
            byte[] digest = md.digest();

            return new BigInteger(1, digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static void shuffle(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static double testPiDigits(int sequenceCount, int sequenceLength) {
        if (piDigits == null) return 0;
        piDigits = piDigits.replace(".", "");

        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;
        for (int i = 0; i <= Math.min(sequenceCount, piDigits.length() - sequenceLength); i++) {

            String sequence = piDigits.substring(i, i + sequenceLength);
            uniqueSequences.add(sequence);
            totalSequences++;
        }
        return calculateCollisionProbability(totalSequences, uniqueSequences.size());
    }

    private static String readPiFromFile(String filePath) {
        StringBuilder piDigits = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                piDigits.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return piDigits.toString();
    }

    private static double calculateCollisionProbability(int totalCount, int uniqueCount) {
        int repetitions = totalCount - uniqueCount;
        return ((double) repetitions / totalCount) * 100;
    }
}

// Linear Congruential Generator class
class LinearCongruentialGenerator2 {
    private long a;
    private long c;
    private long m;
    private long seed;

    public LinearCongruentialGenerator2(long a, long c, long m, long seed) {
        this.a = a;
        this.c = c;
        this.m = m;
        this.seed = seed;
    }

    public long next() {
        seed = (a * seed + c) % m;
        return seed;
    }
}
