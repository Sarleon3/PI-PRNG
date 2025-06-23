package RealBrainFuck;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public class ChudnovskyPiImproved {

    // Факториал через BigInteger
    public static BigInteger factorial(int n) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    // Вычисление M_k = (6k)! / ((3k)! * (k!)^3)
    public static BigDecimal computeM(int k, MathContext mc) {
        BigInteger fact6k = factorial(6 * k);
        BigInteger fact3k = factorial(3 * k);
        BigInteger factk = factorial(k);

        BigInteger denominator = fact3k.multiply(factk.pow(3));

        BigDecimal M = new BigDecimal(fact6k).divide(new BigDecimal(denominator), mc);

        return M;
    }

    public static BigDecimal computePi(int digits) {
        int scale = digits + (digits / 5);  // запас точности
        MathContext mc = new MathContext(scale, RoundingMode.HALF_UP);

        BigDecimal C = new BigDecimal("426880").multiply(BigDecimalSqrt.sqrt(new BigDecimal("10005"), scale));

        BigDecimal S = BigDecimal.ZERO;

        int iterations = digits / 5;

        for (int k = 0; k < iterations; k++) {
            BigDecimal M = computeM(k, mc);
            BigDecimal L = new BigDecimal("13591409").add(new BigDecimal("545140134").multiply(new BigDecimal(k)));
            BigDecimal X = new BigDecimal("-262537412640768000").pow(k, mc);

            BigDecimal term = M.multiply(L, mc).divide(X, mc);
            S = S.add(term, mc);

            // Можно добавить проверку на маленький член ряда
            if (term.abs().compareTo(BigDecimal.ONE.scaleByPowerOfTen(-scale)) < 0) {
                break;
            }
        }

        BigDecimal pi = C.divide(S, mc);

        return pi.setScale(digits, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        // Выполняем вычисления от 100 до 1000 знаков с шагом 100
       for (int digits = 10; digits <= 10000; digits+=5){
            long startTime = System.currentTimeMillis(); // время начала

            BigDecimal pi = computePi(digits); // предполагается, что такой метод существует

            long endTime = System.currentTimeMillis(); // время окончания
            long duration = endTime - startTime; // длительность в миллисекундах

            System.out.println("Число Пи (" + digits + " знаков):\n"+ pi);
            System.out.println("Время вычисления: " + duration + " миллисекунд");
            System.out.println("----------------------------");
        }
    }
    }
