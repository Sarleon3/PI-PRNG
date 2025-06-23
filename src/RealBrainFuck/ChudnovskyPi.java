package RealBrainFuck;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ChudnovskyPi {

    public static BigDecimal sqrt(BigDecimal A, int scale) {
        BigDecimal x = new BigDecimal(Math.sqrt(A.doubleValue()));
        BigDecimal TWO = BigDecimal.valueOf(2);
        BigDecimal lastX;

        do {
            lastX = x;
            x = A.divide(x, scale, RoundingMode.HALF_UP);
            x = x.add(lastX);
            x = x.divide(TWO, scale, RoundingMode.HALF_UP);
        } while (x.subtract(lastX).abs().compareTo(BigDecimal.ONE.scaleByPowerOfTen(-scale)) > 0);

        return x;
    }

    public static BigDecimal computePi(int digits) {
        int scale = digits + 50;  // разумный запас
        MathContext mc = new MathContext(scale, RoundingMode.HALF_UP);

        BigDecimal C = new BigDecimal("426880").multiply(sqrt(new BigDecimal("10005"), scale));

        BigDecimal M = BigDecimal.ONE;
        BigDecimal L = new BigDecimal("13591409");
        BigDecimal X = BigDecimal.ONE;
        BigDecimal S = L;

        int iterations = 1000;  // можно и меньше, но 1000 — для высокой точности

        for (int k = 1; k < iterations; k++) {
            BigDecimal K = BigDecimal.valueOf(k);

            BigDecimal numerator = BigDecimal.valueOf((6 * k - 5) * (2 * k - 1) * (6 * k - 1));
            BigDecimal denominator = K.pow(3, mc);
            M = M.multiply(numerator, mc).divide(denominator, mc);

            L = L.add(new BigDecimal("545140134"));
            X = X.multiply(new BigDecimal("-262537412640768000"));

            BigDecimal term = M.multiply(L, mc).divide(X, mc);
            S = S.add(term);

            // Прерывание, если терм очень мал, чтобы не считать лишние итерации
            if (term.abs().compareTo(BigDecimal.ONE.scaleByPowerOfTen(-scale)) < 0) {
                break;
            }
        }

        BigDecimal pi = C.divide(S, mc);

        return pi.setScale(digits, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        String prevPiStr = "";

        for (int digits = 100; digits <= 100; digits += 100) {
            BigDecimal pi = computePi(digits);
            String piStr = pi.toPlainString();

            int diffIndex = 0;
            while (diffIndex < prevPiStr.length() && diffIndex < piStr.length()
                    && prevPiStr.charAt(diffIndex) == piStr.charAt(diffIndex)) {
                diffIndex++;
            }

            String newPart = piStr.substring(diffIndex);

            System.out.println("Pi с точностью " + digits + " знаков:");
            System.out.println("Новые знаки: " + newPart);
            System.out.println();

            prevPiStr = piStr;
        }
    }
}
