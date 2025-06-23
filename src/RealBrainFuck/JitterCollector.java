package RealBrainFuck;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Random;

public class JitterCollector {

    Algoritm_Devita_beyli calculator = new Algoritm_Devita_beyli();
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    Random random = new Random();

    // Новый метод перемешивания с xor-shift стилем
    private long mixWithXorShift(long nanoTime, long currentTimeMillis, long cpuTime, double pid,
                                 long freeMem, long totalMem, int identityHash) {
        long mix = nanoTime;
        mix ^= currentTimeMillis * 0x9E3779B97F4A7C15L;
        mix ^= cpuTime * 0xC6BC279692B5CC83L;
        mix ^= Double.doubleToLongBits(pid) * 0xD6E8FEB86659FD93L;
        mix ^= freeMem * 0xA5CB92A5C3DD83AFL;
        mix ^= totalMem * 0xB492B66FBE98F273L;
        mix ^= identityHash * 0x9E3779B97F4A7C15L;

        // «Взбалтываем»
        mix ^= (mix >>> 33);
        mix *= 0xFF51AFD7ED558CCDL;
        mix ^= (mix >>> 33);
        mix *= 0xC4CEB9FE1A85EC53L;
        mix ^= (mix >>> 33);

        // Сжимаем в 32 бита
        mix = (mix >>> 32) ^ (mix & 0xFFFFFFFFL);
        return mix;
    }

    // Метод случайного выбора вычисления значения для перемешивания
    private double computeRandomValue(int i) {
        // Случайно выбираем: true - Algoritm_Devita_beyli, false - ChudnovskyPiImproved
        boolean useDevita = random.nextBoolean();

        if (useDevita) {
            // Вызываем Algoritm_Devita_beyli
            return calculator.computePid(i);
        } else {
            // Вызываем ChudnovskyPiImproved.computePi с малым количеством знаков (например 10)
            // И преобразуем строку в число (например, первые 10 знаков после запятой)
            try {
                String piStr = ChudnovskyPiImproved.computePi(10).toString();
                // Возьмём цифры после десятичной точки (или просто parse double)
                return Double.parseDouble(piStr);
            } catch (Exception e) {
                // На случай ошибки — fallback
                return calculator.computePid(i);
            }
        }
    }

    public ArrayList<Long> collectJitterNumbers(int count) {
        ArrayList<Long> jitterNumbers = new ArrayList<>();
        long prevMix = 0;

        Object dummy = new Object(); // для identityHashCode

        for (int i = 0; i < count; i++) {
            long nanoTime = System.nanoTime();
            long currentTimeMillis = System.currentTimeMillis();
            long cpuTime = threadMXBean.getCurrentThreadCpuTime();
            long freeMem = Runtime.getRuntime().freeMemory();
            long totalMem = Runtime.getRuntime().totalMemory();
            int identityHash = System.identityHashCode(dummy);

            // Случайный выбор вычисления
            double randomValue = computeRandomValue(i);

            // Используем новый метод перемешивания
            long mixed = mixWithXorShift(nanoTime, currentTimeMillis, cpuTime, randomValue, freeMem, totalMem, identityHash);

            // Добавим микрофлуктуацию
            Thread.yield();

            // Дельта с предыдущим
            long diff = i > 0 ? Math.abs(mixed - prevMix) : mixed;
            prevMix = mixed;

            jitterNumbers.add(diff);
        }

        return jitterNumbers;
    }

    public static void main(String[] args) {
        JitterCollector collector = new JitterCollector();
        ArrayList<Long> jitterNumbers = collector.collectJitterNumbers(200);

        System.out.println("Jitter Numbers:");
        for (int i = 0; i < jitterNumbers.size(); i++) {
            System.out.print(jitterNumbers.get(i));
            if (i < jitterNumbers.size() - 1) {
                System.out.print("L,");
            }
        }
        System.out.println(); // перевод строки после всех чисел
    }
}
