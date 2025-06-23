package RealBrainFuck;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ChudnovskyPiImprovedtesting {

    public static BigDecimal computePi(int digits) {
        int scale = digits + (digits / 5);  // запас точности
        MathContext mc = new MathContext(scale, RoundingMode.HALF_UP);

        BigDecimal C = new BigDecimal("426880").multiply(BigDecimalSqrt.sqrt(new BigDecimal("10005"), scale));

        BigDecimal S = BigDecimal.ZERO;

        int iterations = digits / 5;

        // Начальные значения для рекуррентных соотношений
        BigDecimal M = BigDecimal.ONE;  // M_0 = 1
        BigDecimal L = new BigDecimal("13591409");  // L_0
        BigDecimal X = BigDecimal.ONE;  // X_0 = (-N)^0 = 1

        for (int k = 0; k < iterations; k++) {
            if (k > 0) {
                // Рекуррентная формула для M_k
                M = M.multiply(new BigDecimal((6 * k - 5) * (6 * k - 4) * (6 * k - 3) * (6 * k - 2) * (6 * k - 1) * (6 * k)))
                        .divide(new BigDecimal((3 * k - 2) * (3 * k - 1) * (3 * k) * k * k * k), mc);

                // L_k = 13591409 + 545140134 * k
                L = new BigDecimal("13591409").add(new BigDecimal("545140134").multiply(new BigDecimal(k)));

                // X_k = (-262537412640768000)^k
                X = X.multiply(new BigDecimal("-262537412640768000"), mc);
            }

            BigDecimal term = M.multiply(L, mc).divide(X, mc);
            S = S.add(term, mc);

            // Прерываем, если член ряда стал достаточно мал
            if (term.abs().compareTo(BigDecimal.ONE.scaleByPowerOfTen(-scale)) < 0) {
                break;
            }
        }

        BigDecimal pi = C.divide(S, mc);
        return pi.setScale(digits, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        int digits = 1000;
        long startTime = System.currentTimeMillis();

        BigDecimal pi = computePi(digits);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Число Пи (" + digits + " знаков):");
        System.out.println(pi);
        System.out.println("Время вычисления: " + duration + " миллисекунд");
        System.out.println("----------------------------");
    }
}