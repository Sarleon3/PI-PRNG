package RealBrainFuck;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ChudnovskyIncremental {

    private final MathContext mc;
    private BigDecimal sum;
    private final BigDecimal C;
    private BigDecimal M, L, X, K;
    private int term;

    private String lastPiStr;        // Строка с последним приближением π
    private String confirmedDigits;  // Подтвержденные (неизменные) цифры после запятой
    private int outputIndex;         // Индекс следующей выдаваемой цифры из confirmedDigits

    public ChudnovskyIncremental(int digits) {
        this.mc = new MathContext(digits + 20, RoundingMode.HALF_EVEN);

        this.M = BigDecimal.ONE;
        this.L = new BigDecimal("13591409");
        this.X = BigDecimal.ONE;
        this.K = new BigDecimal("6");
        this.sum = L;
        this.C = new BigDecimal("426880").multiply(sqrt(new BigDecimal("10005"), mc), mc);
        this.term = 1;

        BigDecimal pi = C.divide(sum, mc);
        lastPiStr = pi.toPlainString();
        confirmedDigits = ""; // пока ничего не подтверждено
        outputIndex = 0;
    }

    public String nextDigits(int count) {
        // Если в confirmedDigits не хватает цифр — вычисляем следующий член ряда и обновляем подтвержденные цифры
        while (confirmedDigits.length() - outputIndex < count) {
            addNextTerm();
            updateConfirmedDigits();
        }

        // Выдаем порцию
        int endIndex = Math.min(outputIndex + count, confirmedDigits.length());
        String result = confirmedDigits.substring(outputIndex, endIndex);
        outputIndex = endIndex;
        return result;
    }

    private void updateConfirmedDigits() {
        BigDecimal pi = C.divide(sum, mc);
        String newPiStr = pi.toPlainString();

        // Сравним lastPiStr и newPiStr построчно начиная с первой цифры после запятой
        int dotIndex = lastPiStr.indexOf('.');
        int start = dotIndex + 1;

        int maxCompareLen = Math.min(lastPiStr.length(), newPiStr.length());

        int commonLength = 0;
        for (int i = start; i < maxCompareLen; i++) {
            if (lastPiStr.charAt(i) == newPiStr.charAt(i)) {
                commonLength++;
            } else {
                break;
            }
        }

        // Добавляем в confirmedDigits только те цифры, которые теперь совпали
        if (commonLength > confirmedDigits.length()) {
            confirmedDigits += newPiStr.substring(start + confirmedDigits.length(), start + commonLength);
        }

        lastPiStr = newPiStr;
    }

    private void addNextTerm() {
        // Формула для следующего члена ряда Chudnovsky
        BigDecimal k = new BigDecimal(term);

        // Обновляем M по формуле:
        // M_k = M_(k-1) * (K^3 - 16K) / k^3
        BigDecimal numerator = (K.pow(3)).subtract(new BigDecimal("16").multiply(K));
        BigDecimal denominator = k.pow(3);
        M = M.multiply(numerator, mc).divide(denominator, mc);

        L = L.add(new BigDecimal("545140134"));
        X = X.multiply(new BigDecimal("-262537412640768000"), mc);

        BigDecimal termValue = M.multiply(L, mc).divide(X, mc);
        sum = sum.add(termValue);

        K = K.add(new BigDecimal("12"));
        term++;
    }

    private static BigDecimal sqrt(BigDecimal A, MathContext mc) {
        BigDecimal x0 = BigDecimal.ZERO;
        BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
        while (!x0.equals(x1)) {
            x0 = x1;
            x1 = A.divide(x0, mc);
            x1 = x1.add(x0);
            x1 = x1.divide(BigDecimal.valueOf(2), mc);
        }
        return x1;
    }

    public static void main(String[] args) {
        ChudnovskyIncremental gen = new ChudnovskyIncremental(1000);

        System.out.print("3."); // целая часть

        for (int i = 0; i < 20; i++) {
            String digits = gen.nextDigits(5); // по 5 цифр за раз
            System.out.print(digits);

            // Можно вставить паузу для демонстрации постепенной выдачи
            // try { Thread.sleep(500); } catch (InterruptedException e) { }
        }
        System.out.println();
    }
}
