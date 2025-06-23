import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.math.BigInteger;
import java.util.Random;

public class RandomSequenceTest {

    public static void main(String[] args) {
        System.out.println("LCG Generator:");
        testLCG(1000000, 9);

        System.out.println("\nMersenne Twister Generator:");
        testMersenneTwister(1000000, 9);

        System.out.println("\nSecure Hash Generator (SHA-256):");
        testSecureHashGenerator(1000000, 9);

        System.out.println("\nFisher-Yates Shuffle Generator:");
        testFisherYatesShuffle(1000000, 9);
    }

    // Test Linear Congruential Generator
    public static void testLCG(int sequenceCount, int sequenceLength) {
        LinearCongruentialGenerator2 lcg = new LinearCongruentialGenerator2(1664525, 1013904223, (long) Math.pow(2, 32), 1);
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        for (int i = 0; i < sequenceCount; i++) {
            StringBuilder sequence = new StringBuilder();
            for (int j = 0; j < sequenceLength; j++) {
                sequence.append(lcg.next() % 10); // генерируем 5-значные последовательности
            }
            uniqueSequences.add(sequence.toString());
            totalSequences++;
        }
        printResults(totalSequences, uniqueSequences.size());
    }

    // Test Mersenne Twister Generator
    public static void testMersenneTwister(int sequenceCount, int sequenceLength) {
        Random mt = new Random();
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        for (int i = 0; i < sequenceCount; i++) {
            StringBuilder sequence = new StringBuilder();
            for (int j = 0; j < sequenceLength; j++) {
                sequence.append(mt.nextInt(10)); // 5-значные последовательности
            }
            uniqueSequences.add(sequence.toString());
            totalSequences++;
        }
        printResults(totalSequences, uniqueSequences.size());
    }

    // Test Secure Hash Generator (SHA-256)
    public static void testSecureHashGenerator(int sequenceCount, int sequenceLength) {
        HashSet<String> uniqueSequences = new HashSet<>();
        int totalSequences = 0;

        for (int i = 0; i < sequenceCount; i++) {
            String hash = generateRandomHash().substring(0, sequenceLength); // Берем только первые 5 символов
            uniqueSequences.add(hash);
            totalSequences++;
        }
        printResults(totalSequences, uniqueSequences.size());
    }

    // Test Fisher-Yates Shuffle
    public static void testFisherYatesShuffle(int sequenceCount, int sequenceLength) {
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
        printResults(totalSequences, uniqueSequences.size());
    }

    // Generate a secure hash sequence
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

    // Fisher-Yates shuffle method
    private static void shuffle(int[] array, Random rand) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    // Print the results
    private static void printResults(int totalCount, int uniqueCount) {
        int repetitions = totalCount - uniqueCount;
        System.out.println("Общее количество последовательностей: " + totalCount);
        System.out.println("Количество уникальных последовательностей: " + uniqueCount);
        System.out.println("Количество повторений: " + repetitions);
    }
}

// Linear Congruential Generator
class LinearCongruentialGenerator {
    private long a;
    private long c;
    private long m;
    private long seed;

    public LinearCongruentialGenerator(long a, long c, long m, long seed) {
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
