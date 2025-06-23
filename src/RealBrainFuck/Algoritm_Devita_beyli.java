package RealBrainFuck;
import java.util.Random;

public class Algoritm_Devita_beyli {

    public static void main(String[] args) {
        int maxPosition = 1000;  // максимальная позиция (число цифр)
        int step = 3;          // шаг по 3 цифры
        Random rand = new Random();
        for (int startId = 0; startId <= maxPosition; startId += step) {
            for (int pos = startId; pos < startId + step && pos <= maxPosition; pos++) {
                double s1 = series(1, pos);
                double s2 = series(4, pos);
                double s3 = series(5, pos);
                double s4 = series(6, pos);
                double pid = 4.0 * s1 - 2.0 * s2 - s3 - s4;
                pid = pid - (int) pid + 1.0;

                // Размер hex-цифр: циклично от 1 до 10
                int hexLength = 1 + rand.nextInt(10);

                String hexDigits = ihex(pid, hexLength);

                System.out.printf("Position = %3d  Hex digits (%2d): %s%n",
                        pos, hexLength, hexDigits);
            }
            System.out.println();
        }
    }


    // Вычисляет первые nhx hex-цифр дробной части числа x
    static String ihex(double x, int nhx) {
        String hx = "0123456789ABCDEF";
        StringBuilder chx = new StringBuilder();
        double y = Math.abs(x);
        for (int i = 0; i < nhx; i++) {
            y = 16.0 * (y - Math.floor(y));
            chx.append(hx.charAt((int) y));
        }
        return chx.toString();
    }

    // Вычисляет ряд BBP
    static double series(int m, int id) {
        double eps = 1e-17;
        double s = 0.0;

        // Сумма до id
        for (int k = 0; k < id; k++) {
            double ak = 8.0 * k + m;
            double p = id - k;
            double t = expm(p, ak);
            s += t / ak;
            s -= Math.floor(s);
        }

        // Хвост ряда для k >= id
        for (int k = id; k <= id + 100; k++) {
            double ak = 8.0 * k + m;
            double t = Math.pow(16.0, id - k) / ak;
            if (t < eps) break;
            s += t;
            s -= Math.floor(s);
        }

        return s;
    }

    // Быстрое возведение 16^p mod ak
    static double expm(double p, double ak) {
        int ntp = 25;
        double[] tp = new double[ntp];
        tp[0] = 1.0;
        for (int i = 1; i < ntp; i++) tp[i] = 2.0 * tp[i - 1];

        if (ak == 1.0) return 0.0;

        int i;
        for (i = 0; i < ntp; i++) {
            if (tp[i] > p) break;
        }

        double pt = tp[i - 1];
        double p1 = p;
        double r = 1.0;

        for (int j = 1; j <= i; j++) {
            if (p1 >= pt) {
                r = 16.0 * r;
                r = r - Math.floor(r / ak) * ak;
                p1 = p1 - pt;
            }
            pt *= 0.5;
            if (pt >= 1.0) {
                r = r * r;
                r = r - Math.floor(r / ak) * ak;
            }
        }

        return r;
    }
}
